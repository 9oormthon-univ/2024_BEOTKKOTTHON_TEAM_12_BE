package com.backend.wear.service;

import com.backend.wear.dto.*;
import com.backend.wear.entity.ChatMessage;
import com.backend.wear.entity.ChatRoom;
import com.backend.wear.entity.Product;
import com.backend.wear.entity.User;
import com.backend.wear.repository.ChatRoomRepository;
import com.backend.wear.repository.ProductRepository;
import com.backend.wear.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatService(ChatRoomRepository chatRoomRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {

        this.chatRoomRepository = chatRoomRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    //채팅방 생성
    public ChatRoomDto createRoom(Long productId, Long customerId) {

        //이미 상품에 대해 채팅방이 존재하는지
        Optional<ChatRoom> existingRoomOpt = chatRoomRepository.findByProductIdAndCustomerId(productId, customerId);

        if (existingRoomOpt.isPresent()) {
            ChatRoom existingRoom = existingRoomOpt.get();
            // 이미 존재하는 채팅방이면 해당 채팅방의 ID를 반환
            return ChatRoomDto.builder()
                    .chatRoomId(existingRoom.getId())
                    .productId(existingRoom.getProduct().getId())
                    .productImage(existingRoom.getProduct().getProductImage())
                    .productName(existingRoom.getProduct().getProductName())
                    .sellerId(existingRoom.getSeller().getId())
                    .sellerNickName(existingRoom.getSeller().getNickName())
                    .sellerProfileImage(existingRoom.getSeller().getProfileImage())
                    .sellerLevel(existingRoom.getSeller().getLevel().getLabel())
                    .customerId(existingRoom.getCustomer().getId())
                    .customerNickName(existingRoom.getCustomer().getNickName())
                    .customerProfileImage(existingRoom.getCustomer().getProfileImage())
                    .customerLevel(existingRoom.getCustomer().getLevel().getLabel())
                    .is_created(false)
             //       .messageList(getAllMessages(existingRoom.getId()))
                    .build();
        }

        //상품
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        //구매자
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("구매자를 찾을 수 없습니다."));

        //판매자
        User seller = productRepository.findById(productId).get().getUser();

        if(customer.getId().equals(seller.getId()))
            throw new IllegalArgumentException("판매자와 구매자는 똑같은 아이디 불가능");

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setCustomer(customer);
        chatRoom.setSeller(seller);
        chatRoom.setProduct(product);

        chatRoomRepository.save(chatRoom);

        return ChatRoomDto.builder()
                .chatRoomId(chatRoom .getId())
                .productId(chatRoom .getProduct().getId())
                .productImage(chatRoom .getProduct().getProductImage())
                .productName(chatRoom .getProduct().getProductName())
                .sellerId(chatRoom .getSeller().getId())
                .sellerNickName(chatRoom .getSeller().getNickName())
                .sellerProfileImage(chatRoom .getSeller().getProfileImage())
                .sellerLevel(chatRoom .getSeller().getLevel().getLabel())
                .customerId(chatRoom .getCustomer().getId())
                .customerNickName(chatRoom .getCustomer().getNickName())
                .customerProfileImage(chatRoom .getCustomer().getProfileImage())
                .customerLevel(chatRoom .getCustomer().getLevel().getLabel())
                .is_created(true)
                .build();
    }

    //채팅방 입장
    public List <ChatMessageSendDto> enterChatRoom(Long roomId, Long productId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅 내역이 없습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("채팅 내역이 없습니다."));

        User seller = chatRoom.getSeller();

        User customer = chatRoom.getCustomer();

        boolean isCreated=false; //새로 생긴 방인지
        if (chatRoom.getMessageList().isEmpty())
            isCreated=true;

        List<ChatMessage> chatMessageList = chatRoom.getMessageList();
        List <ChatMessageSendDto> list=new ArrayList<>();

        for(ChatMessage chatMessage: chatMessageList){
            User user = userRepository.findById(chatMessage.getUserId())
                    .get();

            ChatMessageSendDto dto = ChatMessageSendDto.builder()
                    .userId(chatMessage.getUserId())
                    .chatRoomId(chatMessage.getChatRoom().getId())
                    .message(chatMessage.getMessage())
                    .timeStamp(chatMessage.getTimeStamp())
                    .isMine(chatMessage.isMine())
                    .profilePic(user.getProfileImage())
                    .build();

            list.add(dto);
        }

        return list;
    }

    // 모든 메시지 리스트 반환
    public List<ChatMessageSendDto> getAllMessages(Long chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).get();

        List<ChatMessage> chatMessageList = chatRoom.getMessageList();
        List <ChatMessageSendDto> list=new ArrayList<>();

        for(ChatMessage chatMessage: chatMessageList){
            User user = userRepository.findById(chatMessage.getUserId())
                    .get();

            ChatMessageSendDto dto = ChatMessageSendDto.builder()
                    .userId(chatMessage.getUserId())
                    .chatRoomId(chatMessage.getChatRoom().getId())
                    .message(chatMessage.getMessage())
                    .timeStamp(chatMessage.getTimeStamp())
                    .isMine(chatMessage.isMine())
                    .profilePic(user.getProfileImage())
                    .build();

            list.add(dto);
        }

        return list;
    }

    //채팅 리스트 불러오기
    public List<ChatRoomProfileDto> findAllRoom(Long userId) {

        List<ChatRoom> chatRoomList = chatRoomRepository
                .findBySellerIdOrCustomerId(userId, userId);

        List<ChatRoomProfileDto> chatRoomProfileDtoList = new ArrayList<>();

        for (ChatRoom r : chatRoomList) {
            ChatRoomProfileDto dto;

            //내가 구매자
            if (r.getCustomer().getId().equals(userId)) {

                User seller = r.getSeller();
                dto = ChatRoomProfileDto.builder()
                        .chatRoomId(r.getId())
                        .productId(r.getProduct().getId())
                        .userNickName(seller.getNickName())
                        .userProfileImage(seller.getProfileImage())
                        .userLevel(seller.getLevel().getLabel())
                        .build();

                chatRoomProfileDtoList.add(dto);
            } else {
                User customer = r.getCustomer();
                dto = ChatRoomProfileDto.builder()
                        .chatRoomId(r.getId())
                        .productId(r.getProduct().getId())
                        .userNickName(customer.getNickName())
                        .userProfileImage(customer.getProfileImage())
                        .userLevel(customer.getLevel().getLabel())
                        .build();

                chatRoomProfileDtoList.add(dto);
            }
        }

        return chatRoomProfileDtoList;
    }
}