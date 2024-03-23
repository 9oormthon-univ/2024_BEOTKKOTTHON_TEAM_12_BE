package com.backend.wear.controller;

//import com.backend.wear.dto.LoginDto;
import com.backend.wear.dto.*;
import com.backend.wear.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LonginController {

    private final LoginService loginService;

    @Autowired
    public LonginController (LoginService loginService){

        this.loginService=loginService;
    }

//    @Value("${8664efc6-ab71-47b9-8cb0-5aa20dccc59c}")
//    private String API_KEY;

    // api/signup
    @GetMapping("/login")
    public ResponseEntity<?> myLogin(@RequestBody LoginDto loginDto){
        try{
            LoginResponseDto dto=loginService.loginByUser(loginDto);
             return ResponseEntity.ok(dto);
        }

        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    //대학 인증 메일 발송
//    @PostMapping("/university/certify")
//    public ResponseEntity<?> certifyUniversity
//    (@RequestBody UnivCertRequestDto dto) throws IOException {
//
//        Map<String, Object> response =
//                UnivCert.certify(API_KEY, dto.getUniversityEmail(), dto.getUniversityName(), dto.isCheck());
//
//        return ResponseEntity.ok(response);
//    }
//
//    //메일로 발송된 인증 코드 입력
//    @PostMapping("/university/certifycode")
//    public ResponseEntity<?> certifyUserCode(
//            @RequestBody UnivCertRequestDto dto) throws IOException {
//
//        Map<String, Object> response =
//                UnivCert.certifyCode(API_KEY, dto.getUniversityEmail(),
//                        dto.getUniversityName(), dto.getCode());
//
//        Boolean success = (Boolean) response.get("success");
//        if (success) { //인증 성공
//            String universityName = (String) response.get("univName");
//            String universityEmail = (String) response.get("certified_email");
//
//            UnivCertResponseDto responseDto = new UnivCertResponseDto(universityEmail, universityName, success);
//
//            return ResponseEntity.ok(responseDto);
//        } else { //인증 실패
//            Integer code = (Integer) response.get("code");
//            String message = (String) response.get("message");
//
//            UnivCertFail responseDto = new UnivCertFail(code, success, message);
//            return ResponseEntity.ok(responseDto);
//        }
//    }
}
