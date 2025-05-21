# 🧪 spring-plus

Spring Boot 학습용 개인 프로젝트입니다.  
Spring Security, JPA, Test 등을 직접 구성하며 백엔드 기술 스택을 익히고 있습니다.

## 🔍 프로젝트 목적

- 스프링 프레임워크의 핵심 개념 학습
- 보안(Spring Security), @Transactional의 이해, N+1문제 이해, QueryDSL이해, 테스트(MockMvc), 예외 처리 등 백엔드 전반적인 기능 익히기

## ⚙️ 사용 기술

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security**
- **Spring Data JPA**
- **MySQL**
- **Gradle**
- **JUnit 5 / MockMvc**
- **Lombok**
- **application.properties 설정 기반 구성**

## 🧱 주요 기능 (진행 중)

- [x] 회원가입 및 로그인
- [x] JWT 인증 및 인가 처리
- [x] 예외 처리 공통 모듈 구성
- [x] 사용자 프로필 조회
- [ ] TODO 기능 추가 예정
- [ ] 통합 테스트 환경 개선 중

## 📁 프로젝트 구조

```
spring-plus/
├── domain/
│   ├── user/
│   └── todo/
├── global/
│   ├── config/
│   ├── exception/
│   └── security/
└── test/
```

## 📌 커밋 메시지 컨벤션

- `feat`: 기능 추가
- `fix`: 버그 수정
- `test`: 테스트 코드 관련
- `refactor`: 리팩토링
- `docs`: 문서 수정

## 👤 작성자

- [@Tcimel](https://github.com/Tcimel)

