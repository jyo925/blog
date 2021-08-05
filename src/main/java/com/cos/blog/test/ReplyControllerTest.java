package com.cos.blog.test;

import com.cos.blog.model.Board;
import com.cos.blog.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplyControllerTest {

    @Autowired
    private BoardRepository boardRepository;


    //무한 참조 테스트
    //return 시 jackson 라이브러리가 오브젝트를 json으로 변환하는 과정에서 모델의 Getter를 호출하게 되는데
    //이때, Reply와 Board 둘 사이에서 무한 참조가 일어남
    @GetMapping("/test/board/{id}")
    public Board getBoard(@PathVariable int id) {
        return boardRepository.findById(id).get();
    }
}
