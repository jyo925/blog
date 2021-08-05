package com.cos.blog.service;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Log
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Transactional(readOnly = true)
    public User find(String username) {
        User user = userRepository.findByUsername(username).orElseGet(()->{
            return new User();
        });
        return user;
/*
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            return new User();
        }
        User user1 = (User) user.get();
        return user1;
*/
    }

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

        //Validate 체크
        //Oauth 사용자는 패스워드 수정 X
        if (persistanceUser.getOauth() == null || persistanceUser.getOauth().equals("")) {
            String rawPassword = user.getPassword();
            String encPassword = encoder.encode(rawPassword);
            persistanceUser.setPassword(encPassword);
        }

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
