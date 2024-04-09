package com.backend.wear.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageSendDto {

    //보낸사람 pk
    Long userId;

    //채팅방 아이디
    Long chatRoomId;

    //채팅 메시지
    String message;

    //보낸시간
    String timeStamp;

    //내가 보냈는지 여부
    Boolean isMine;

    //메시지 보낸 사람 프로필 사진
    String profilePic;
}
