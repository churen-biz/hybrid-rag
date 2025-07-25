package biz.churen.ee.rag.api.service.llm.qwen;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import biz.churen.ee.rag.api.HybridRagApiApplication;
import biz.churen.ee.rag.sdk.LLM;
import jakarta.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HybridRagApiApplication.class)
public class QwenChatServiceTest {

    @Resource
    private QwenChatService qwenChatService;

    @Test
    public void chat() {
        String ai = qwenChatService.chat(LLM.QWEN_PLUS.getModel(), "hello");
        System.out.println(ai);
    }
}
