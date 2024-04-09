package com.backend.wear.controller;

import com.backend.wear.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.backend.wear.entity.ChatRoom;
import com.backend.wear.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins={"http://43.201.189.171:8080", "http://localhost:5173",
        "http://wear-frontend.s3-website.ap-northeast-2.amazonaws.com","http://localhost:8080"})
public class ChatRoomController {
    private final ChatService chatService;
    private static Logger log = LoggerFactory.getLogger(ChatRoomController.class);

    @Autowired
    public ChatRoomController(ChatService chatService){
        this.chatService=chatService;
    }

    // 채팅방  생성
    // api/chat/room/create?productId={productId}&customerId={customerId}
    @PostMapping("/room/create") //상품 아이디, 클릭한 유저 아이디
    public ResponseEntity<?> createRoom(@RequestParam Long productId, @RequestParam Long customerId) {

        try {
            //채팅방 pk 반환
            ChatRoomDto chatRoom =  chatService.createRoom(productId, customerId);

            if(chatRoom.is_created()){
                return ResponseEntity.ok().body(chatRoom);
            }

            else{
                Long roomId=chatRoom.getChatRoomId();
                log.info("존재하는 채팅방, roomId: "+roomId);

                return ResponseEntity.ok().body(chatRoom);
            }
        }

        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 채팅방 입장

    // api/chat/room/enter?roomId={roomId}&productId={productId}
    @GetMapping("/room/enter")
    public ResponseEntity<?> enterRoom(@RequestParam Long roomId,
                                       @RequestParam Long productId)
    {
        try{
            List<ChatMessageSendDto> list=chatService.enterChatRoom(roomId, productId);

            log.info("채팅방 입장 완료");

            return ResponseEntity.ok().body(list);
        }

        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //사용자 모든 채팅방 조회
    @GetMapping("/rooms")
    // chat/room?userId={userId}
    public ResponseEntity<?> roomList(@RequestParam Long userId){

        List<ChatRoomProfileDto> chatRoomList=chatService.findAllRoom(userId);

        if(!chatRoomList.isEmpty()){
            log.info("모든 채팅방 조회 완료.");
            return ResponseEntity.ok().body(chatRoomList);
        }

        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("채팅 내역이 없습니다.");
    }
}