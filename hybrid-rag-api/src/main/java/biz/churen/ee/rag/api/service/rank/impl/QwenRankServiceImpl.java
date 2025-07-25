package biz.churen.ee.rag.api.service.rank.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.openai.client.OpenAIClient;

import biz.churen.ee.rag.api.service.helper.HelperService;
import biz.churen.ee.rag.api.service.helper.ThreadPool;
import biz.churen.ee.rag.api.service.rank.RankService;
import biz.churen.ee.rag.sdk.LLM;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QwenRankServiceImpl implements RankService {
    @Resource
    private OpenAIClient qwenClient;
    @Resource
    private HelperService helperService;


    @Override
    public List<Integer> score(String query, List<String> passages) {
        if (StrUtil.isBlank(query) || CollUtil.isEmpty(passages)) {
            return new ArrayList<>();
        }
        List<Integer> result = new ArrayList<>();
        passages.forEach(e -> result.add(0));

        String SYSTEM_MESSAGE =
                "You are an expert tasked with determining whether the passage is relevant to the query";
        String USER_MESSAGE = """
                Respond with "True" if PASSAGE is relevant to QUERY and "False" otherwise.
                <PASSAGE>
                {passage}
                </PASSAGE>
                <QUERY>
                {query}
                </QUERY>
                """;
        List<Callable<Integer>> callables = passages.stream().map(m -> {
            Map<String, String> map = new HashMap<>();
            map.put("passage", m);
            map.put("query", query);
            String um = StrUtil.format(USER_MESSAGE, map, true);
            return (Callable<Integer>) () -> logProb(SYSTEM_MESSAGE, um, new BiFunction<String, Double, Integer>() {
                @Override
                public Integer apply(String token, Double logProb) {
                    BigDecimal val = BigDecimal.valueOf(Math.exp(logProb) * 100).setScale(2, RoundingMode.HALF_UP);
                    return BooleanUtil.toBooleanObject(token) ? val.intValue() : (100 - val.intValue());
                }
            });
        }).collect(Collectors.toList());

        return getScore(query, passages, result, callables);
    }

    @Override
    public List<Integer> scoreV2(String query, List<String> passages) {
        if (StrUtil.isBlank(query) || CollUtil.isEmpty(passages)) {
            return new ArrayList<>();
        }
        List<Integer> result = new ArrayList<>();
        passages.forEach(e -> result.add(0));

        String SYSTEM_MESSAGE =
                "You are an expert at rating passage relevance. Respond with only a number from 0-100.";
        String USER_MESSAGE = """
                Rate how well this passage answers or relates to the query. Use a scale from 0 to 100.

                Query: {query}

                Passage: {passage}

                Provide only a number between 0 and 100 (no explanation, just the number):""";
        List<Callable<Integer>> callables = passages.stream().map(m -> {
            Map<String, String> map = new HashMap<>();
            map.put("passage", m);
            map.put("query", query);
            String um = StrUtil.format(USER_MESSAGE, map, true);
            return (Callable<Integer>) () -> logProb(SYSTEM_MESSAGE, um, new BiFunction<String, Double, Integer>() {
                @Override
                public Integer apply(String token, Double logProb) {
                    boolean matched = ReUtil.isMatch(Pattern.compile("^(100|[1-9]?[0-9])$"), token);
                    if (matched) {
                        return ReUtil.getFirstNumber(token);
                    } else {
                        return 0;
                    }
                }
            });
        }).collect(Collectors.toList());

        return getScore(query, passages, result, callables);
    }

    @Nullable
    private List<Integer> getScore(String query, List<String> passages, List<Integer> result,
            List<Callable<Integer>> callables) {
        try {
            List<Future<Integer>> futures = ThreadPool.EXECUTOR_SERVICE_4.invokeAll(callables, 60, TimeUnit.SECONDS);
            for (int i = 0; i < futures.size(); i++) {
                result.set(i, futures.get(i).get());
                log.info("i: {} query: {}, passage: {}  score: {}",
                        i,
                        StrUtil.subPre(query, 8),
                        StrUtil.subPre(passages.get(i), 8),
                        result.get(i)
                );
            }
        } catch (Throwable ex) {
            log.error(ex.getMessage(), ex);
        }
        return result;
    }

    /**
     * @return 使用 logProbs 参数
     */
    @SuppressWarnings("DuplicatedCode")
    private int logProb(String systemMessage, String userMessage, BiFunction<String, Double, Integer> function) {
        try {
            return helperService.logProb(qwenClient, LLM.QWEN_TURBO_LATEST, systemMessage, userMessage, function);
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
        }
        return 0;
    }
}
