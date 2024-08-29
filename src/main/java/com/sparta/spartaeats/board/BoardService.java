package com.sparta.spartaeats.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<BoardVO> getBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public BoardVO createBoard(BoardVO boardVO) {
        return boardRepository.save(boardVO);
    }

    public BoardVO getBoardById(long id) {
        Optional<BoardVO> board = boardRepository.findById(id);
        return board.orElse(null);
    }

}
