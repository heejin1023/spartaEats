package com.sparta.spartaeats.ai.controller;

import com.sparta.spartaeats.ai.domain.AiArchive;
import com.sparta.spartaeats.ai.dto.AiArchiveRequestDto;
import com.sparta.spartaeats.ai.dto.AiArchiveResponseDto;
import com.sparta.spartaeats.ai.dto.AiArchiveSearchCondition;
import com.sparta.spartaeats.ai.service.AiArchiveService;
import com.sparta.spartaeats.common.aop.ApiLogging;
import com.sparta.spartaeats.common.controller.CustomApiController;
import com.sparta.spartaeats.common.exception.AiApiException;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiArchiveController extends CustomApiController {

    private final AiArchiveService aiArchiveService;

    /**
     * 상품설명 자동생성
     * ai 질문 & 답변 저장
     * @param aiArchiveRequestDto
     * @param errors
     * @return
     */
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    @PostMapping
    public ApiResult createAiDescription(
                @RequestBody @Validated AiArchiveRequestDto aiArchiveRequestDto,
                             Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) { // 파라미터 바인딩 오류시 리턴
            return bindError(errors, apiResult);
        }

        User loginUser = getLoginedUserObject();
        aiArchiveRequestDto.setLoginUserIdx(loginUser.getId());

        try {
            AiArchive aiArchive = aiArchiveService.askQuestion(aiArchiveRequestDto);
            if (aiArchive != null) {
                apiResult.set(ApiResultError.NO_ERROR).setResultData(aiArchive);
            }
        } catch(AiApiException e) {
            return apiResult.set(e.getCode());
        }

        return apiResult;

    }

    /**
     * AI 요청기록 상세 조회
     * @param aiId
     * @return
     */
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    @GetMapping("/history/{aiId}")
    public ApiResult getAiArchive(@PathVariable UUID aiId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        if(aiId == null) {
            apiResult.set(ApiResultError.ERROR_INVALID_ARGUMENT);
            return apiResult;
        }
        AiArchiveResponseDto aiArchive = aiArchiveService.getAiArchiveById(aiId);

        return apiResult.set(ApiResultError.NO_ERROR).setResultData(aiArchive) ;
    }

    /**
     * 전체 조회
     * @param sc
     * @return
     */
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    @GetMapping("/history")
    public ApiResult getAiArchiveList(AiArchiveSearchCondition sc) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        sc.validateAndSetDefaults();
        Pageable pageable = sc.generatePageable();

        Page<AiArchiveResponseDto> aiArchivePage = aiArchiveService.getAiArchiveList(sc, pageable);

        log.info(" RESULT {}", aiArchivePage);
        apiResult.set(ApiResultError.NO_ERROR).setList(aiArchivePage).setPageInfo(aiArchivePage);
        return apiResult;
    }

    /**
     * 삭제처리
     * @param aiId
     * @return
     */
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    @PatchMapping("{aiId}")
    public ApiResult deleteAiArchive(@PathVariable UUID aiId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if(aiId == null) {
            apiResult.set(ApiResultError.ERROR_INVALID_ARGUMENT);
        }

        User user = getLoginedUserObject();
        Long userIdx = user.getId();

        try {
            AiArchive aiArchive = aiArchiveService.deleteAiArchiveById(aiId, userIdx);

            if(aiArchive != null) {
                return apiResult.set(ApiResultError.NO_ERROR, "삭제되었습니다.");
            }
        } catch(Exception e) {
            return apiResult.setResultMessage("삭제에 실패했습니다.");
        }
        return apiResult;

    }

}
