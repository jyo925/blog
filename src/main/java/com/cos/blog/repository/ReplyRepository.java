package com.cos.blog.repository;

import com.cos.blog.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    
    //네이티브 쿼리 사용
    //insert 수행된 결과의 행 개수를 리턴해줌
    @Modifying
    @Query(value = "INSERT INTO REPLY VALUES(?1, ?2, ?3, NOW())", nativeQuery = true)
    int mSave(int userId, int boardId, String content);
}
