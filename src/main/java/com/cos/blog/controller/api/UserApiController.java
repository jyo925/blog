package com.cos.blog.controller.api;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class UserApiController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/user")
    public ResponseDto<Integer> save(@RequestBody User user) {
        log.info("회원가입 호출");
        //db insert 수행
        user.setRole(RoleType.USER);
        userService.save(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); //자바 오브젝트를 Json데이터로 변환해서 전달(Jackson 라이브러리)
    }

}
