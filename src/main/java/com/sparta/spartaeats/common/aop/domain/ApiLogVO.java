package com.sparta.spartaeats.common.aop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_api_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiLogVO {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID apiId;

    @Column(columnDefinition = "TEXT")
    private String apiName;

    @Column(columnDefinition = "TEXT")
    private String httpMethod;

    @Column(columnDefinition = "TEXT")
    private String endpoint;

    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column(columnDefinition = "TEXT")
    private String responseBody;

    @Column(columnDefinition = "TEXT")
    private String responseCode;

    private LocalDateTime createdAt;

    private Long clientId;
}
