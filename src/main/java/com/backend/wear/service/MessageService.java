package com.backend.wear.service;

import com.backend.wear.controller.ChatRoomController;
import com.backend.wear.dto.ChatMessageSendDto;
import com.backend.wear.entity.ChatMessage;
import com.backend.wear.entity.ChatRoom;
import com.backend.wear.entity.MyMessage;
import com.backend.wear.repository.ChatMessageRepository;
import com.backend.wear.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private static Logger log = LoggerFactory.getLogger(MessageService.class);
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public MessageService(ChatMessageRepository chatMessageRepository,
                          ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    // 메시지 저장
    @Transactional
    public void saveMessage(ChatMessageSendDto dto) {

    }
}