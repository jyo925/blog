package com.cos.blog.controller.api;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
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
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;


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
        //1. Authentication 객체를 만들어서 강제로 넣는 방법 ---> 사용X
        //라는데 나는 principal값을 수정했더니 잘 동작함
        //그러나 회원 정보가 1000개라면? 각 필드를 수기로 수정하는 것은 어려움 -> 아래 Manager를 이용한 방법으로 적용하기

//        principal.getUser().setEmail(user.getEmail());
//        principal.getUser().setPassword(user.getPassword());  //이렇게 넣어주면 암호화 되지 않은 값으로 들어감 -> 서비스 단에서 user.setPassword(encPassword) 수행해줘야함

/*        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        httpSession.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);*/

        //2. Manager에 접근해서 강제 로그인하여 Authentication을 만들면 자동으로 시큐리티 세션에 넣어주는 방법
        //기존에 있던 Authentication은 사라지는 건가?
        //UsernamePasswordAuthenticationToken의 파라미터 값 유형이 다양한데 차이는?
        //세션 등록
        //이 방식을 사용하려면 username도 bodydata에 담아서 받아야함
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    //전통적인 방식의 로그인 사용X
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
