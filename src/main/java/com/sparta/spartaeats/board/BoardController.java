package com.sparta.spartaeats.board;

import com.sparta.spartaeats.common.aop.ApiLogging;
import com.sparta.spartaeats.common.controller.CustomApiController;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
/**
 * 참고용
 */
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Slf4j
// TODO : 권한
public class BoardController extends CustomApiController {

        private final BoardService boardService;

       // @Secured(UserRoleEnum.Authority.OWNER)
        @ApiLogging
        @GetMapping
        public ApiResult getBoardList(
                @RequestParam(defaultValue = "1") int pageNumber,
                @RequestParam(defaultValue = "10") int pageSize) {

            ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

            User loginUser = getLoginedUserObject();
            log.info("LoginUser info {}", loginUser);

            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
            Page<BoardVO> data =  boardService.getBoards(pageable);

            apiResult.set(ApiResultError.NO_ERROR).setList(data).setPageInfo(data);
            return apiResult;
        }

        //@Secured(UserRoleEnum.Authority.ADMIN)
        @ApiLogging
        @GetMapping("/get")
        public ApiResult getBoard(@RequestParam long boardId) {
            BoardVO board = boardService.getBoardById(boardId);

            ApiResult apiResult = new ApiResult();
            apiResult.set(ApiResultError.NO_ERROR).setResultData(board);

            return apiResult;
        }

        @ApiLogging
        @PostMapping
        public ApiResult createBoard(@RequestBody BoardVO board, Errors errors) {
            ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
            if (errors.hasErrors()) { // 파라미터 바인딩 오류시 리턴
                return bindError(errors, apiResult);
            }
            BoardVO boardVO = boardService.createBoard(board);

            // TODO : error 처리

            // 생성한 데이터를 다시 반환해야하면 setResultData() 사용
            // 데이터를 반환할 필요가 없으면 apiResult.set(ApiResultError.NO_ERROR);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(boardVO);

            return apiResult;
        }


}
