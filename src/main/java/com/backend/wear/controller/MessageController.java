package com.backend.wear.controller;

import com.backend.wear.dto.ChatMessageSendDto;
import com.backend.wear.entity.ChatMessage;
import com.backend.wear.entity.MyMessage;
import com.backend.wear.repository.ChatRoomRepository;
import com.backend.wear.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins={"http://43.201.189.171:8080", "http://localhost:5173",
        "http://wear-frontend.s3-website.ap-northeast-2.amazonaws.com","http://localhost:8080"})
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final MessageService messageService;
    private static Logger log = LoggerFactory.getLogger(MessageController.class);

    //메시지 보내기
    // pub/api/chat/message
    @MessageMapping("/api/chat/message")
    public void sendMessage (ChatMessageSendDto dto, SimpMessageHeaderAccessor accessor) {

        //sub/api/chat/room/{roomId}

        // 메시지 저장 로지


        sendingOperations.convertAndSend("/sub/api/chat/room/"+dto.getChatRoomId(), dto);
    }
}