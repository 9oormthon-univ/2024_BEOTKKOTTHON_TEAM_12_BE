package com.backend.wear.service;

import com.backend.wear.dto.*;
import com.backend.wear.entity.EnvironmentLevel;
import com.backend.wear.entity.Style;
import com.backend.wear.entity.University;
import com.backend.wear.entity.User;
import com.backend.wear.repository.StyleRepository;
import com.backend.wear.repository.UniversityRepository;
import com.backend.wear.repository.UserRepository;
import com.univcert.api.UnivCert;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final StyleRepository styleRepository;
    private final UniversityRepository universityRepository;

    @Autowired
    public LoginService(UserRepository userRepository,
                        StyleRepository styleRepository,
                        UniversityRepository universityRepository){
        this.userRepository=userRepository;
        this.styleRepository=styleRepository;
        this.universityRepository=universityRepository;
    }

    public LoginResponseDto loginByUser(LoginDto loginDto){
        String userCreatedId= loginDto.getUserCreatedId();
        User user= userRepository.findByUserCreatedId(userCreatedId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "사용자를 찾지 못하였습니다."));

        String userPassword=loginDto.getUserPassword();
        String password=user.getUserPassword();

        //비밀번호 입력 안 함
        if(userPassword.isEmpty()){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //비밀번호 일치X
        if(!userPassword.equals(password))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        else {//로그인 성공, 유저 pk 반환
            LoginResponseDto dto=LoginResponseDto.builder()
                    .id(user.getId())
                    .message("로그인 성공")
                    .build();
            return dto;
        }
    }

    public void userSignUp(SignUpDto signUpDto, String universityName,
                           String email){
        if(signUpDto.getUserPassword().equals(signUpDto.getUserCheckPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        University university = universityRepository.findByUniversityName(universityName);
        if(university==null){ //새로운 대학
            university= new University();
            university.setUniversityName(universityName);
            universityRepository.save(university);
        }

        User user=new User();
        user.setUserName("wear");
        user.setNickName("wear");

        user.setUserCreatedId(signUpDto.getUserId());
        user.setUserPassword(signUpDto.getUserPassword());
        user.setPoint(20);
        user.setLevel(EnvironmentLevel.SAPLING);
        user.setProfileImage("\"C:\\Users\\user\\Downloads\\KakaoTalk_20240324_044410124.png\"");

        setProfileStyle(user, signUpDto.getStyleList());

        userRepository.save(user);
    }

    //스타일 태그 이름으로 Style 저장
//    @Transactional
    public void setProfileStyle (User user, List<String> style){
        List<Style> newStyles = new ArrayList<>();

        for(String s: style){
            Style styleTag =new Style();
            styleTag.setUser(user);
            styleTag.setStyleName(s);

            newStyles.add(styleTag);
        }

        user.setStyle(newStyles);
    }
}
