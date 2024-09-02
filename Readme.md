## :mortar_board: 목차
[1. 프로젝트 목적](#1-프로젝트-목적)

[2. 기술 스택](#2-기술-스택) 

[3. 팀원 및 역할](#3-팀원-및-역할)

[4. 서비스 구성 및 실행 방법](#4-서비스-구성-및-실행-방법)

[5. ERD](#5-ERD)

[6. API DOCS](#6-API-DOCS)


## 1. 프로젝트 목적

- 00의 민족과 같은 주문 관리 플랫폼
- 자동화된 서비스로 불필요한 인력 소모를 줄이는 것이 목적
- 배달 및 포장 주문 관리
- AI Open APi를 이용해 상품 설명을 추천 받아 쉽게 작성할 수 있도록 지원
---

## 2. 기술 스택
:computer:
### Platform
![IntelliJ](https://img.shields.io/badge/IntelliJ-0078D6.svg?style=for-the-badge&logo=intellijidea&logoColor=#000000)
![AWS](https://img.shields.io/badge/AWS-0078D6.svg?style=for-the-badge&logo=amazonwebservices&logoColor=#000000)

### RDBMS
![PostgreSQL](https://img.shields.io/badge/postgresql-0078D6?style=for-the-badge&logo=postgresql&logoColor=white)

### Application Development / Skills
![Java](https://img.shields.io/badge/Java-0078D6?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-0078D6?style=for-the-badge&logo=spring&logoColor=#6DB33F)
![Boot](https://img.shields.io/badge/springboot-0078D6?style=for-the-badge&logo=springboot&logoColor=#)
![Security](https://img.shields.io/badge/springsecurity-0078D6?style=for-the-badge&logo=springsecurity&logoColor=#)
![JPA](https://img.shields.io/badge/JPA-0078D6?style=for-the-badge&logo=aseprite&logoColor=#)
![GitHub](https://img.shields.io/badge/Git-0078D6?style=for-the-badge&logo=git&logoColor=#)

### Detail
- Spring Boot : 3.3.3
- JAVA : 17
- Hibernate 6

---
## 3. 팀원 및 역할

- 유희진
  - 
- 곽솔래
  - 
- 임예이
  - 
- 박기도
  - 

---

## 4. 서비스 구성 및 실행 방법
```
\---src
+---main
|   +---java
|   |   \---com
|   |       \---sparta
|   |           \---spartaeats
|   |               +---address
|   |               |   +---controller
|   |               |   +---domain
|   |               |   +---dto
|   |               |   +---repository
|   |               |   \---service
|   |               +---ai
|   |               |   +---controller
|   |               |   +---domain
|   |               |   +---dto
|   |               |   +---repository
|   |               |   \---service
|   |               |
|   |               +---apiLog
|   |               |   +---controller
|   |               |   +---domain
|   |               |   +---dto
|   |               |   +---repository
|   |               |   \---service
|   |               |
|   |               +---auditing
|   |               +---board
|   |               +---common
|   |               |   +---aop
|   |               |   +---config
|   |               |   +---controller
|   |               |   +---dto
|   |               |   +---exception
|   |               |   +---interceptor
|   |               |   +---jwt
|   |               |   +---model
|   |               |   +---security
|   |               |   +---type
|   |               |   \---util
|   |               |
|   |               +---location
|   |               |   +---controller
|   |               |   +---domain
|   |               |   +---dto
|   |               |   +---repository
|   |               |   \---service
|   |               |
|   |               +---order
|   |               |   +---controller
|   |               |   +---domain
|   |               |   +---dto
|   |               |   +---repository
|   |               |   \---service
|   |               |
|   |               +---payments
|   |               |   +---controller
|   |               |   +---domain
|   |               |   +---dto
|   |               |   +---repository
|   |               |   \---service
|   |               |
|   |               +---product
|   |               |   +---controller
|   |               |   +---domain
|   |               |   |   \---validationGroup
|   |               |   +---dto
|   |               |   +---repository
|   |               |   \---service
|   |               |
|   |               +---responseDto
|   |               +---store
|   |               |   +---controller
|   |               |   +---domain
|   |               |   |   \---validationGroup
|   |               |   +---dto
|   |               |   +---repository
|   |               |   \---service
|   |               |
|   |               +---storeCategory
|   |               |   +---controller
|   |               |   +---domain
|   |               |   |   \---validationGroup
|   |               |   +---dto
|   |               |   +---repository
|   |               |   \---service
|   |               |
|   |               +---token
|   |               |   +---domain
|   |               |   +---repository
|   |               |   \---service
|   |               |
|   |               \---user
|   |                   +---controller
|   |                   +---domain
|   |                   |   +---dto
|   |                   |   \---validationGroup
|   |                   +---repository
|   |                   \---service
|   |
|   \---resources
|           application.yml
|
SpartaEatsApplicationTests.java
```

## 5. ERD

![AI 검증 비즈니스 프로젝트 (3)](https://github.com/user-attachments/assets/64f730c1-2b8a-4a36-b6da-c0d9350e7e13)

## 6. API DOCS
[API 명세서](https://www.notion.so/teamsparta/API-22106ad12bf74320b517e301c03d997a)





