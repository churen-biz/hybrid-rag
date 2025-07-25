package biz.churen.ee.rag.api.service.rank.impl;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import biz.churen.ee.rag.api.HybridRagApiApplicationTest;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;

public class QwenRankServiceImplTest extends HybridRagApiApplicationTest {
    @Resource
    private QwenRankServiceImpl qwenRankServiceImpl;

    @Test
    public void score() {
        List<Integer> scores = qwenRankServiceImpl.score("APP", Arrays.asList("手机", "苹果", "应用", "香蕉"));
        System.out.println(JSONUtil.toJsonPrettyStr(scores));
    }

    @Test
    public void scoreV2() {
        List<Integer> scores = qwenRankServiceImpl.scoreV2("APP", Arrays.asList("手机", "苹果", "应用", "香蕉"));
        System.out.println(JSONUtil.toJsonPrettyStr(scores));
    }
}
