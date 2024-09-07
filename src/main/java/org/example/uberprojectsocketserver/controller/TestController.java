package org.example.uberprojectsocketserver.controller;

import org.example.uberprojectsocketserver.dto.ChatRequest;
import org.example.uberprojectsocketserver.dto.ChatResponse;
import org.example.uberprojectsocketserver.dto.TestRequest;
import org.example.uberprojectsocketserver.dto.TestResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public TestController(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate=simpMessagingTemplate;
    }

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponse pingCheck(TestRequest message){
        System.out.println("Message received from client : "+message.getData());
        return TestResponse.builder().data("Received").build();
    }

    @SendTo("/topic/scheduled")
    @Scheduled(fixedDelay = 2000)
    public void sendPeriodicMessage(){
        System.out.println("Sending scheduled message");
        simpMessagingTemplate.convertAndSend("/topic/scheduled","Periodic message sent "+ System.currentTimeMillis());
    }

    // send message in group
    @MessageMapping("chat/{room}")
    @SendTo("/topic/message/{room}")
    public ChatResponse chatMessage(@DestinationVariable String room, ChatRequest request){
        return ChatResponse.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timeStamp(""+ System.currentTimeMillis())
                .build();
    }

    // send message to specific user
    @MessageMapping("chat/{room}/{userId}")
    public void privateChatMessage(@DestinationVariable String room,@DestinationVariable String userId, ChatRequest request){
        ChatResponse response = ChatResponse.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timeStamp(""+ System.currentTimeMillis())
                .build();
        simpMessagingTemplate.convertAndSendToUser(userId, "/queue/privateMessage/" + room, response);
    }
    
}
