package biz.churen.ee.rag.api.service.llm.qwen;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;

@Service
public class QwenChatService {
    private static final Logger logger = LoggerFactory.getLogger(QwenChatService.class);

    @Resource
    private OpenAIClient qwenClient;

    public String chat(String model, String userMessage) {
        try {
            ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder();
            builder.model(model);
            builder.addUserMessage(userMessage);

            ChatCompletion chatCompletion = qwenClient.chat().completions().create(builder.build());
            // System.out.println(chatCompletion);
            return chatCompletion.choices().get(0).message().content().orElse(StrUtil.EMPTY);
        } catch (Exception th) {
            logger.error(th.getMessage(), th);
            return StrUtil.EMPTY;
        }
    }

    public String chat(String model, String systemPrompt, List<String> userMessages) {
        try {
            ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder();
            builder.model(model);
            builder.addSystemMessage(systemPrompt);
            userMessages.forEach(builder::addUserMessage);

            ChatCompletion chatCompletion = qwenClient.chat().completions().create(builder.build());
            // System.out.println(chatCompletion);
            return chatCompletion.choices().get(0).message().content().orElse(StrUtil.EMPTY);
        } catch (Exception th) {
            logger.error(th.getMessage(), th);
            return StrUtil.EMPTY;
        }
    }
}
