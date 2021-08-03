package com.cos.blog.controller.api;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Log
public class UserApiController {

    @Autowired
    private UserService userService;


    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody User user) {
        log.info("회원가입 호출");
        userService.join(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); //자바 오브젝트를 Json데이터로 변환해서 전달(Jackson 라이브러리)
    }

    @PutMapping("/user")
    public ResponseDto<Integer> update(@RequestBody User user,
                                       @AuthenticationPrincipal PrincipalDetail principal,
                                       HttpSession httpSession) {
        userService.update(user);
        //DB 값은 변경했지만 세션값이 변경되지 않음 -> 세션 값 직접 변경
        //Authentication 객체를 만들어서 강제로 넣기 ---> X
        //Manager에 접근해서 강제 로그인하여 Authentication을 만들면 자동으로 시큐리티 세션에 넣어주는 방법으로 변경
/*        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        httpSession.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);*/



        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    //전통적인 방식의 로그인
/*    @PostMapping("/api/user/login")
    public ResponseDto<Integer> login(@RequestBody User user, HttpSession session) {
        log.info("로그인 호출");
        //principal 접근 주체
        User principal = userService.login(user);
        //세션에 담기
        if (principal != null) {
            session.setAttribute("principal", principal);
        }
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }*/
}
