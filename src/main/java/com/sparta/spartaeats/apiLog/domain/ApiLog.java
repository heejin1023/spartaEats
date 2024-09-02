package com.sparta.spartaeats.apiLog.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_api_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiLog {

    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID apiId;

    @Column(length = 200)
    private String apiName;

    @Column(length = 50)
    private String httpMethod;

    @Column(length = 200)
    private String endpoint;

    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column(columnDefinition = "TEXT")
    private String responseBody;

    @Column(length = 10)
    private String responseCode;

    private LocalDateTime createdAt;

    private Long clientId;

    private static final int MAX_LENGTH = 3000; // 최대 길이 설정

    public void setResponseBody(String responseBody) {
        if (responseBody != null && responseBody.length() > MAX_LENGTH) {
            this.responseBody = responseBody.substring(0, MAX_LENGTH) + "...";
        } else {
            this.responseBody = responseBody;
        }
    }
}
