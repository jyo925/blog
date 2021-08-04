package com.cos.blog.service;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;



    @Transactional
    public void join(User user) {
        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);

        userRepository.save(user);
    }

    //수정 시
    //1. 영속성 컨텍스트에 user 오브젝트를 영속화 시키고
    //2. 영속화된 user 오브젝트를 수정하면 자동으로 update 쿼리가 나간다.(더티채킹)
    @Transactional
    public void update(User user) {
        User persistanceUser = userRepository.findById(user.getId())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("회원 찾기 실패");
                });
        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);
        persistanceUser.setPassword(encPassword);
        persistanceUser.setEmail(user.getEmail());

//        user.setPassword(encPassword); //굳이 세션에 패스워드를 담아줄 필요가 없음
        //이 메서드 종료 = 서비스 종료 = 트랜잭션 종료 = 커밋 자동 수행
    }

    //select할 때 트랜잭션 시작, 서비스 종료시에 트랜잭션 종료(정합성)
/*    @Transactional(readOnly = true)
    public User login(User user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }*/
}
