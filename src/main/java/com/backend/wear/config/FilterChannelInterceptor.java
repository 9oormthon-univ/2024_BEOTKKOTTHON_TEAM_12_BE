package com.backend.wear.config;

import com.backend.wear.dto.ChatMessageDto;
import com.backend.wear.entity.ChatMessage;
import com.backend.wear.entity.ChatRoom;
import com.backend.wear.repository.ChatMessageRepository;
import com.backend.wear.repository.ChatRoomRepository;
import com.backend.wear.repository.ProductRepository;
import com.backend.wear.service.MessageService;
import com.backend.wear.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class FilterChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // stomp 연결 시도 시 호출
    // 메시지가 채널로 전송되기 전 호출되는 메서드
    // 프런트 -> 서버로 메시지가 전송될 때
    @Override
    @Transactional
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        System.out.println(this.getClass().getName()+" 호출 완료");
        System.out.println("full message:" + message);  
        System.out.println("auth:" + headerAccessor.getNativeHeader("Authorization"));
        System.out.println(headerAccessor.getHeader("nativeHeaders").getClass());

//        String messageId = headerAccessor.getNativeHeader("id").get(0);
//        String chatRoomIdString = headerAccessor.getNativeHeader("chat_room_id").get(0); // 오타 수정
//
//        long chatRoomId = Long.parseLong(chatRoomIdString);
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).get();
//
//        if (chatRoom == null) {
//            // 채팅방이 없을 때 처리
//            return null;
//        }

//        String userType = "";
//        // 판매자
//     //   chatRoom.getProduct().getUser().getId()
//
//        if(chatRoom.getSeller().getId().equals(userId))
//            userType ="CUSTOMER";    // 구매자(Customer)
//        else
//            userType="SELLER";
//
//        ChatMessage chatMessage=new ChatMessage();
//        chatMessage.setMessage(messageContent); //메시지
//        chatMessage.setChatRoom(chatRoom); //채팅방
//        chatMessage.setUserId(userId); //유저 pk
//        chatMessage.setUserType(userType); //유저 타입 (판매자 인지 구매자 인지)
//
//        //채팅메시지 정보 db에 저장
//        chatMessageRepository.save(chatMessage);
//
//        // 추출한 값들을 사용하여 필요한 작업 수행
//        System.out.println("Message: " + messageContent);
//        System.out.println("Chat Room ID: " + chatRoomId);

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            System.out.println("msg: " + "conne");
        }

        //throw new MessagingException("no permission! ");
        return message;
    }

    // 발생한 예외에 관계없이 전송이 완료된 후 호출
    // 클라이언트 -> 서버 메시지 전송 완료 후
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex){
        System.out.println(this.getClass().getName()+" 호출 완료");

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        System.out.println("full message:" + message);
        System.out.println("auth:" + headerAccessor.getNativeHeader("Authorization"));
        System.out.println(headerAccessor.getHeader("nativeHeaders").getClass());
    }
}