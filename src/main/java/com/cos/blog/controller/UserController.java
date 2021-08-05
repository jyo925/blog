package com.cos.blog.controller;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

// 인증X 사용자들이 출입할 수 있는 경로를 /auth/**와 /, static 내 파일만 허용
@Controller
@Log4j2
@RequiredArgsConstructor
public class UserController {

    @Value("${cos.key}")
    private String cosKey;

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @GetMapping("/auth/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code) {
        //토큰 요청 하기
        //POST 방식으로 key = value 데이터를 요청(카카오톡 자원 서버로), 라이브러리 사용
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "1a1c602e60bc0b66c3e1137a353c0669");
        params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        params.add("code", code);

        //하나의 엔티티에 담기(헤더 + 바디)
        HttpEntity<MultiValueMap<String, String>> kakaoTokenReqeust = new HttpEntity<>(params, headers);

        //Http 요청하고 응답 받기
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenReqeust,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        log.info("카카오 엑세스 토큰: " + oAuthToken.getAccess_token());

        //사용자 정보 요청
        RestTemplate rt2 = new RestTemplate();

        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers2.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        HttpEntity<MultiValueMap<String, String>> kakaoProfileReqeust = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileReqeust,
                String.class
        );

//        log.info("사용자 정보: " + response2);

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

/*        log.info("카카오 프로필 정보: " + kakaoProfile.getId() + ", " + kakaoProfile.getKakao_account().getEmail());
        //User 오브젝트: username, password, email
        log.info("blog username: " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
        log.info("blog email: " + kakaoProfile.getKakao_account().getEmail());
        log.info("blog password: " + cosKey);*/

        //이 정보를 토대로 강제 회원가입 수행(기 가입자인지 체크도 포함)


        User kakaoUser = User.builder()
                .username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
                .password(cosKey)
                .email(kakaoProfile.getKakao_account().getEmail())
                .oauth("kakao")
                .build();

        // 가입자 혹은 비가입자 체크 해서 처리
        User originUser = userService.find(kakaoUser.getUsername());

        if(originUser.getUsername() == null) {
            log.info("기존 회원이 아니기에 자동 회원가입을 진행합니다");
            userService.join(kakaoUser);
        }

        // 자동 로그인 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }

}
