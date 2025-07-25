package biz.churen.ee.rag.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import biz.churen.ee.rag.api.service.llm.qwen.QwenChatService;
import biz.churen.ee.rag.sdk.LLM;
import biz.churen.ee.rag.sdk.Message;


@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private QwenChatService qwenChatService;

    @RequestMapping(value = "/qwen", method = {RequestMethod.GET})
    public Message<String> chatV1(@RequestParam(value = "userMessage") String userMessage) {
        return Message.ok(qwenChatService.chat(LLM.QWEN_PLUS.getModel(), userMessage));
    }
}
