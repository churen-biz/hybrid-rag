package biz.churen.ee.rag.api.service.llm.qwen;


import org.junit.Test;

import biz.churen.ee.rag.api.HybridRagApiApplicationTest;
import biz.churen.ee.rag.sdk.LLM;
import jakarta.annotation.Resource;

public class QwenChatServiceTest extends HybridRagApiApplicationTest {

    @Resource
    private QwenChatService qwenChatService;

    @Test
    public void chat() {
        String ai = qwenChatService.chat(LLM.QWEN_PLUS, "hello");
        System.out.println(ai);
    }
}
