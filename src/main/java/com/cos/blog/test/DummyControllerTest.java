package com.cos.blog.test;


import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;


//회원가입 더미데이터로 테스트
@RestController
@AllArgsConstructor
public class DummyControllerTest {

    private UserRepository userRepository;

    //update email, pw
    //form 태그 말고 json 데이터로 받기
    //스프링이 json 데이터를 받아서 메시지 컨버터를 이용해 (jackson라이브러리) 자바 오브젝트로 변환해줌
    //user 객체에 속성이 null 값이 있으면 save를 통해 update 정상 작동 X
    //save는 id를 전달하지 않으면 insert를 수행, id를 전달하면 해당 id가 있는 경우 update를 하고 없으면 insert를 한다.
    @Transactional
    @PutMapping("/dummy/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User requestUser) {

        System.out.println("id = " + id);
        System.out.println("requestUser password= " + requestUser.getPassword());
        System.out.println("requestUser email= " + requestUser.getEmail());
        //자바는 파라미터로 함수를 넣을 수 없음
        //람다식 이용
        User user = userRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("해당 유저가 없습니다. 수정에 실패하였습니다.");
        });

        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());
//        userRepository.save(user);
        
        //save를 사용하지 않고 @Transactional을 이용하면 update됨 -> 더티채킹

        return null;
    }

    @GetMapping("/dummy/users")
    public List<User> list() {
        return userRepository.findAll();
    }

    //한 페이지당 2건의 데이터를 리턴
    //jpa가 페이징 기능을 제공함
    //2건씩, id기준으로 최신순으로
    @GetMapping("/dummy/user")
    public List<User> pageList(
            @PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        Page<User> pagingUser = userRepository.findAll(pageable);
        List<User> users = pagingUser.getContent();
        return users;
    }

    @GetMapping("/dummy/user/{id}")
    public User detail(@PathVariable int id) {

        //DB에서 값을 못찾는 경우 null이 되므로 Optional로 감싸서 리턴해줌
        //만약에 값이 없는 경우 orElseGet부분이 실행됨 -> 빈 객체 User를 리턴
        /*
        User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
            @Override
            public User get() {
                return new User(); //빈 객체를 리턴
            }
        });
        */

        //추후 Spring Aop를 이용해서 Exception을 가로채서 에러페이지를 보여주기
        /*User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 유저는 없습니다. id: " + id);
            }
        });
        */

        //람다식을 이용하면 Supplier 타입을 리턴해야하는지 몰라도 된다.
        User user = userRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 유저는 없습니다.  id: " + id);
        });

        //user 객체는 자바 오브젝트
        //웹 브라우저가 이해할 수 있는 데이터 json으로 변환 필요(Gson 라이브러리 이용 등)
        //스프링 부트는 MessageConverter가 응답 시에 자동으로 작동하는데,
        //이게 Jackson 라이브러리를 호출해서 자바 오브젝트를 json으로 변환해서 브라우저에 전달
        return user;
    }

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
