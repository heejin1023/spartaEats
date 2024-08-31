package com.sparta.spartaeats.ai.controller;

import com.sparta.spartaeats.ai.dto.AiArchiveRequestDto;
import com.sparta.spartaeats.ai.service.AiArchiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiArchiveController {

    private final AiArchiveService aiArchiveService;


    @PostMapping
    public String apiTest(@RequestBody AiArchiveRequestDto aiArchiveRequestDto) {

        aiArchiveService.askQuestion(aiArchiveRequestDto);

        return "S";


    }


}
