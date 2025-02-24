package org.asia.mystompserver.controller;

import org.asia.mystompserver.domain.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    // @SendTo("/topic/messages")
    public String sendMessage(String message) {
        logger.info("接收到客户端的消息:{}", message);
        return "服务端收到：" + message;
    }

    @PostMapping("/to-client")
    public String toClient(@RequestBody Student student) {
        logger.info("主动发消息到客户端:{}", student);
        sendToClient(student);

        return "success";
    }

    public void sendToClient(Student student) {
        messagingTemplate.convertAndSend("/topic/messages", student);
    }
}
