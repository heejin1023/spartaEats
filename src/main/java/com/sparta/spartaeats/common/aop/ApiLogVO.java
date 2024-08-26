package com.sparta.spartaeats.common.aop;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "p_api_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiLogVO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID apiId;

    private String apiName;

    private String httpMethod;

    private String endpoint;

    private String requestBody;

    private String responseBody;

    private String responseCode;

    private Date createdAt;

    private Long clientId;
}
