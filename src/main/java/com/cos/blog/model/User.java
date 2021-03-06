package com.cos.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // 빌더 패턴
//ORM -> Java(다른언어) Object -> 테이블로 매핑해주는 기술
@Entity // User 클래스가 MySQL에 테이블이 생성이 된다.
//@DynamicInsert // insert시에 null인 필드를 제외시켜준다.(좋은 방법X)
public class User {

    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    private int id; // 시퀀스, auto_increment

    @Column(nullable = false, length = 100, unique = true)
    private String username; // 아이디

    @Column(nullable = false, length = 100) // 해쉬로 암호화하면 길어짐
    private String password;

    @Column(nullable = false, length = 50)
    private String email; // myEmail, my_email

    // @ColumnDefault("'user'")
    // DB는 RoleType이라는 게 없으므로 @E 붙이기
    @Enumerated(EnumType.STRING)
    private RoleType role; // Enum을 쓰는게 좋다. // ADMIN, USER

    private String oauth; // kakao, google

    // 내가 직접 시간을 넣으려면 Timestamp.valueOf(LocalDateTime.now())
    @CreationTimestamp //시간 자동 입력
    private Timestamp createDate;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", oauth='" + oauth + '\'' +
                ", createDate=" + createDate +
                '}';
    }

}