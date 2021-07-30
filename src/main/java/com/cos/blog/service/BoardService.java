package com.cos.blog.service;

import com.cos.blog.model.Board;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;


    @Transactional
    public void post(Board board, User user) { //title, content
        board.setCount(0);
        board.setUser(user);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<Board> getBoardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board getBoard(int id) {
        return boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        });
    }

    @Transactional
    public void delete(int id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void update(Board reqeustBoard) {
        Board board = boardRepository.findById(reqeustBoard.getId()).orElseThrow(() -> {
            return new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        }); //영속화
        board.setTitle(reqeustBoard.getTitle());
        board.setContent(reqeustBoard.getContent());
        //해당 함수 종료시 트랜잭션이 종료되면서 더티 채킹함(자동 업데이트)
    }
}
