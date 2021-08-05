package com.cos.blog.controller.api;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.service.BoardService;
import com.cos.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Log
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;


    @PostMapping("/api/board")
    public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) {
        boardService.post(board, principal.getUser());
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id) {
        boardService.delete(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board) {
        board.setId(id);
        boardService.update(board);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    //컨트롤러에서 데이터를 받을 때 dto를 만들어서 받는게 좋다.
    @PostMapping("/api/board/{boardId}/reply")
    public ResponseDto<Integer> replySave(@PathVariable int boardId,
                                          @RequestBody Reply reply,
                                          @AuthenticationPrincipal PrincipalDetail principal) {

        boardService.postReply(principal.getUser(), boardId, reply);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> replyDelete(@PathVariable int replyId) {
        boardService.deleteReply(replyId);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}
