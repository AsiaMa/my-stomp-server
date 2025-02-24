package org.asia.mystompserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 处理客户端消息，并广播给订阅了 "/topic/greetings" 的所有客户端
    @MessageMapping("/hello")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        logger.info("接收到客户端的消息:{}", message);
        return "服务端收到：" + message;
    }

    @GetMapping("/to-client")
    public String toClient(String text) {
        logger.info("主动发消息到客户端:{}", text);
        sendToClient(text);

        return "success";
    }

    public void sendToClient(String text) {
        messagingTemplate.convertAndSend("/topic/messages", text);
    }
}
