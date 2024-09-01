package com.sparta.spartaeats.ai.service;

import com.sparta.spartaeats.ai.domain.AiArchive;
import com.sparta.spartaeats.ai.dto.AiApiResponseDto;
import com.sparta.spartaeats.ai.dto.AiArchiveRequestDto;
import com.sparta.spartaeats.ai.dto.AiArchiveResponseDto;
import com.sparta.spartaeats.ai.dto.AiArchiveSearchCondition;
import com.sparta.spartaeats.ai.repository.AiArchiveRepository;
import com.sparta.spartaeats.product.domain.Product;
import com.sparta.spartaeats.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


    public AiArchive askQuestion(AiArchiveRequestDto aiArchiveRequestDto) {
        String url = apiUrl + apiKey;

        System.out.println(url);
        String question = aiArchiveRequestDto.getQuestion();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestJson = "{\"contents\":[{\"parts\":[{\"text\":";
        requestJson += "\"" + question + "\"";
        requestJson += "}]}]}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<AiApiResponseDto> aiResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AiApiResponseDto.class);

        AiApiResponseDto responseDto = aiResponse.getBody();

        if (responseDto != null && !responseDto.getCandidates().isEmpty()) {
            AiApiResponseDto.Candidate candidate = responseDto.getCandidates().get(0);
            String responseText = candidate.getContent().getParts().get(0).getText();
            log.info("AI Response Text: " + responseText);
            log.info("productId {}", aiArchiveRequestDto.getProductId());

            // exception 만들어서 컨트롤러로 throw
            Product product = productRepository.findById(aiArchiveRequestDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));

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
            System.out.println("No response candidates found.");
        }

        return null;
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
        Page<AiArchive> aiArchivePage = aiArchiveRepository.searchWithJoinAndLike(
                productName, pageable);

        List<AiArchiveResponseDto> aiArchiveDTOList = aiArchivePage.getContent().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(aiArchiveDTOList, pageable, aiArchivePage.getTotalElements());
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