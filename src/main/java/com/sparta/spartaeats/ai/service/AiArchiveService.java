package com.sparta.spartaeats.ai.service;

import com.sparta.spartaeats.ai.domain.AiArchive;
import com.sparta.spartaeats.ai.dto.AiApiResponseDto;
import com.sparta.spartaeats.ai.dto.AiArchiveRequestDto;
import com.sparta.spartaeats.ai.dto.AiArchiveResponseDto;
import com.sparta.spartaeats.ai.dto.AiArchiveSearchCondition;
import com.sparta.spartaeats.ai.repository.AiArchiveRepository;
import com.sparta.spartaeats.common.exception.AiApiException;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.product.domain.Product;
import com.sparta.spartaeats.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiArchiveService {

    private final AiArchiveRepository aiArchiveRepository;

    private final ProductRepository productRepository;

    private final RestTemplate restTemplate;


    @Value("${spartaEats.ai.url}")
    private String apiUrl;

    @Value("${spartaEats.ai.api-key}")
    private String apiKey;


    public AiArchive askQuestion(AiArchiveRequestDto aiArchiveRequestDto) throws AiApiException {
        String url = apiUrl + apiKey;

        String question = aiArchiveRequestDto.getQuestion();

        UUID productId = aiArchiveRequestDto.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AiApiException(ApiResultError.ERROR_AI_API_NO_PRODUCT));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestJson = "{\"contents\":[{\"parts\":[{\"text\":";
        requestJson += "\"" + question + "\"";
        requestJson += "}]}]}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<AiApiResponseDto> aiResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AiApiResponseDto.class);

        AiApiResponseDto responseDto = aiResponse.getBody();
        log.debug(responseDto.toString());

        if (responseDto != null && !responseDto.getCandidates().isEmpty()) {
            AiApiResponseDto.Candidate candidate = responseDto.getCandidates().get(0);
            String responseText = candidate.getContent().getParts().get(0).getText();
            log.info("AI Response Text: " + responseText);
            log.info("productId {}", aiArchiveRequestDto.getProductId());

            if(responseText.length() > 400) {
                throw new AiApiException(ApiResultError.ERROR_AI_API_EXCEED_LENGTH);
            }

            AiArchive aiArchive = AiArchive.builder()
                    .product(product)
                    .requestContents(aiArchiveRequestDto.getQuestion())
                    .responseContents(responseText)
                    .delYn('N')
                    .build();
            aiArchive.setCreatedBy(aiArchiveRequestDto.getLoginUserIdx());
            aiArchiveRepository.save(aiArchive);
            return aiArchive;

        } else {
            throw new AiApiException(ApiResultError.ERROR_AI_API);
        }

    }

    public AiArchiveResponseDto getAiArchiveById(UUID aiId) {
        AiArchive aiArchive = aiArchiveRepository.findById(aiId).orElseThrow();

        AiArchiveResponseDto aiArchiveResponseDto = AiArchiveResponseDto.builder()
                .aiId(aiArchive.getId())
                .productId(aiArchive.getProduct().getId())
                .delYn(aiArchive.getDelYn())
                .createdAt(aiArchive.getCreatedAt())
                .createdBy(aiArchive.getCreatedBy())
                .build();

        return aiArchiveResponseDto;
    }


    public Page<AiArchiveResponseDto> getAiArchiveList(AiArchiveSearchCondition sc, Pageable pageable) {
        String productName = sc.getProductName();
        String userName = sc.getUserName();
        LocalDate startDate = sc.getStartDate();
        LocalDate endDate = sc.getEndDate();

        return aiArchiveRepository.searchWithJoinAndLike(
                productName, userName, startDate, endDate, pageable);
    }

    private AiArchiveResponseDto convertToResponseDto(AiArchive aiArchive) {
        return AiArchiveResponseDto.builder()
                .aiId(aiArchive.getId())
                .productId(aiArchive.getProduct() != null ? aiArchive.getProduct().getId() : null)
                .productName(aiArchive.getProduct() != null ? aiArchive.getProduct().getProductName() : null)
                .requestContents(aiArchive.getRequestContents())
                .responseContents(aiArchive.getResponseContents())
                .delYn(aiArchive.getDelYn())
                .createdAt(aiArchive.getCreatedAt())
                .createdBy(aiArchive.getCreatedBy())
                .build();
    }

    public AiArchive deleteAiArchiveById(UUID aiId, Long userIdx) {
        AiArchive aiArchive = aiArchiveRepository.findById(aiId).orElseThrow();

        aiArchive.setDelYn('Y');
        aiArchive.setModifiedBy(userIdx);
        aiArchive.setDeletedBy(userIdx);

        return aiArchiveRepository.save(aiArchive);
    }
}