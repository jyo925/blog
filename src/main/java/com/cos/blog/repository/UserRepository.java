package com.cos.blog.repository;

import com.cos.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//DAO
//빈으로 등록되는가? = 스프링 컨테이너에 객체를 가지고 있는가? 있어야 필요한 곳에서 내가 의존 주입을 사용할 수 있다.
//자동으로 빈으로 등록되므로 @Repository 생략 가능
//User테이블이 관리하는 레파지토리이고, User테이블의 P key가 Integer임을 명시
//기본적인 쿼리문을 JpaRepository가 다 들고 있음
public interface UserRepository extends JpaRepository<User, Integer> {
    

}
