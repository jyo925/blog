package com.cos.blog.test;


import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//회원가입 더미데이터로 테스트
@RestController
@AllArgsConstructor
public class DummyControllerTest {

    private UserRepository userRepository;

    //http://localhost:8080/blog/dummy/join 요청
    //http body의 username, password, email 데이터를 가지고 요청하면 파라미터에서 받음
    @PostMapping("/dummy/join")
//    public String join(String username, String password, String email) {
    public String join(User user) {
        System.out.println("user = " + user);
        user.setRole(RoleType.USER);
        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }
}
