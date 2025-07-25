package biz.churen.ee.rag.api.service.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletion.Choice.Logprobs;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionTokenLogprob;

import biz.churen.ee.rag.sdk.LLM;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HelperService {
    @Resource
    private OpenAIClient qwenClient;

    /**
     * @return 返回置信区间 [0.0, 100.0]
     */
    public Pair<Boolean, Float> confidenceScore(String question, String chunks) {
        String PROMPT = """
                You retrieved this article: {0}.
                The question is: {1}.
                Before even answering the question, consider whether you have sufficient information in the article to answer the question fully.
                Your output should JUST be the boolean true or false, of if you have sufficient information in the article to answer the question.
                Respond with just one word, the boolean true or false. You must output the word 'True', or the word 'False', nothing else.
                """;
        try {
            ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder();
            builder.model(LLM.QWEN_TURBO_LATEST.getModel());
            builder.addUserMessage(StrUtil.indexedFormat(PROMPT, chunks, question));
            builder.logprobs(true);
            builder.topLogprobs(3);

            ChatCompletion chatCompletion = qwenClient.chat().completions().create(builder.build());
            List<ChatCompletionTokenLogprob> logProbs = chatCompletion.choices().get(0)
                    .logprobs().flatMap(Logprobs::content).orElse(new ArrayList<>());
            double score;
            BigDecimal val = new BigDecimal(0);
            for (ChatCompletionTokenLogprob logProb : logProbs) {
                String token = logProb.token();
                score = logProb.logprob();
                val = BigDecimal.valueOf(Math.exp(score) * 100).setScale(2, RoundingMode.HALF_UP);
                log.info("confidenceScore token: {}, score: {}, val: {}", token, score, val);
                return Pair.of(BooleanUtil.toBooleanObject(token), val.floatValue());
            }
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
        }
        return Pair.of(Boolean.FALSE, 0f);
    }

    /**
     * @return 使用 logProbs 参数
     */
    @SuppressWarnings("DuplicatedCode")
    public int logProb(OpenAIClient client, LLM llm,
            String systemMessage, String userMessage,
            BiFunction<String, Double, Integer> function
    ) {
        try {
            ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder();
            builder.model(llm.getModel());
            builder.addSystemMessage(systemMessage);
            builder.addUserMessage(userMessage);
            builder.logprobs(true);
            builder.topLogprobs(3);
            builder.temperature(0);

            ChatCompletion chatCompletion = client.chat().completions().create(builder.build());
            List<ChatCompletionTokenLogprob> logProbs = chatCompletion.choices().get(0)
                    .logprobs().flatMap(Logprobs::content).orElse(new ArrayList<>());
            double logProbVal;
            for (ChatCompletionTokenLogprob logProb : logProbs) {
                String token = logProb.token();
                logProbVal = logProb.logprob();
                log.info("logProb token: {}, logProb: {}", token, logProbVal);
                return function.apply(token, logProbVal);
            }
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
        }
        return 0;
    }
}
