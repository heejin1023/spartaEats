package com.sparta.spartaeats.ai.service;

import com.sparta.spartaeats.ai.domain.AiArchive;
import com.sparta.spartaeats.ai.dto.AiApiResponseDto;
import com.sparta.spartaeats.ai.dto.AiArchiveRequestDto;
import com.sparta.spartaeats.ai.repository.AiArchiveRepository;
import com.sparta.spartaeats.product.domain.Product;
import com.sparta.spartaeats.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiArchiveService {

    private final AiArchiveRepository aiArchiveRepository;

    private final ProductRepository productRepository;

    private final RestTemplate restTemplate;


    @Value("{spartaEats.ai.url}")
    private String apiUrl;

    @Value("{spartaEats.ai.api-key}")
    private String apiKey;


    public AiArchive askQuestion(AiArchiveRequestDto aiArchiveRequestDto) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyDDYv9R1IEOvNIdgkhWITGpDSD6lbdLq_c";

        System.out.println(url);
        String question = aiArchiveRequestDto.getQuestion();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestJson = "{\"contents\":[{\"parts\":[{\"text\":";
        requestJson += "\"" + question + "\"";
        requestJson += "}]}]}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<AiApiResponseDto> aiResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AiApiResponseDto.class);

        ResponseEntity<AiApiResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AiApiResponseDto.class);
        AiApiResponseDto responseDto = responseEntity.getBody();

        if (responseDto != null && !responseDto.getCandidates().isEmpty()) {
            AiApiResponseDto.Candidate candidate = responseDto.getCandidates().get(0);
            String responseText = candidate.getContent().getParts().get(0).getText();
            log.info("AI Response Text: " + responseText);
            log.info("productId {}", aiArchiveRequestDto.getProductId());
            Product product = productRepository.findById(aiArchiveRequestDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));

            AiArchive aiArchive = AiArchive.builder()
                    .product(product)
                    .requestContents(aiArchiveRequestDto.getQuestion())
                    .responseContents(responseText)
                    .build();
            aiArchiveRepository.save(aiArchive);

            log.debug("{}", aiArchive);
        } else {
            System.out.println("No response candidates found.");
        }

        return null;
    }


}
