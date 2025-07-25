package biz.churen.ee.rag.api.service.llm.qwen.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;

import jakarta.annotation.Resource;


@Configuration
public class QwenConfiguration {
    @Resource
    private QwenClientEnv qwenClientEnv;

    @Bean
    public OpenAIClient qwenClient() {
        return OpenAIOkHttpClient.builder()
                .apiKey(qwenClientEnv.getApiKey())
                .baseUrl(qwenClientEnv.getBaseUrl())
                .build();
    }
}
