package biz.churen.ee.rag.api.service.llm.qwen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class QwenClientEnv {

    @Value("${env.Qwen.apiKey}")
    // @Value("${ENV_QWEN_API_KEY}")
    private String apiKey;

    @Value("${env.Qwen.baseUrl}")
    // @Value("${ENV_QWEN_BASE_URL}")
    private String baseUrl;
}
