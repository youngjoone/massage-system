# Massage System Backend

## 🧾 프로젝트 개요

마사지 예약 시스템의 백엔드 애플리케이션입니다.  
사용자 관리, 마사지 서비스, 예약, 공지사항 등 프론트엔드와 통신하는 RESTful API를 제공합니다.  
JWT와 HttpOnly 쿠키를 활용한 보안 인증 및 세션 관리를 구현하였습니다.

---

## ⚙️ 기술 스택

- **언어:** Java 17  
- **프레임워크:** Spring Boot 3.x  
- **웹:** Spring Web (RESTful API)  
- **데이터베이스:** H2 (개발/테스트용 인메모리 또는 파일 기반)  
- **ORM:** Spring Data JPA (Hibernate)  
- **보안:** Spring Security (JWT, BCryptPasswordEncoder)  
- **JWT 라이브러리:** JJWT (io.jsonwebtoken)  
- **유틸리티:** Lombok  
- **빌드 도구:** Maven  

---

## 🔐 인증 및 토큰 흐름

### 1. 로그인 요청
- 프론트엔드(React)에서 `/api/auth/login`으로 사용자명과 비밀번호를 POST 전송  
- `axios`는 `withCredentials: true` 옵션을 사용하여 HttpOnly 쿠키 수신을 준비

### 2. 백엔드 인증 및 토큰 발급
- `UserService`가 사용자 정보 검증  
- 성공 시 `JwtTokenProvider`가 **Access Token**을 발급 (만료 시간 짧음)  
- `RefreshTokenService`가 **Refresh Token**을 발급하고 DB에 저장 (만료 시간 김)  
- 두 토큰은 HttpOnly 쿠키(`accessToken`, `refreshToken`)로 응답 헤더에 포함됨  

### 3. 보호된 API 요청 처리
- 브라우저는 저장된 쿠키를 자동으로 포함해 요청  
- 백엔드의 `JwtAuthenticationFilter`가 쿠키 내 Access Token을 검증  
- 유효 시 Spring Security Context에 인증 정보 설정  

### 4. Access Token 만료 시
- 백엔드는 401 Unauthorized 반환  
- 프론트의 `axios` 인터셉터가 `/api/auth/refresh-token`으로 Access Token 재요청  
- 백엔드는 Refresh Token 검증 후 새 Access Token 발급 → 쿠키에 저장  
- 프론트는 이전 요청을 자동 재시도  

### 5. 로그아웃 (예정)
- 프론트에서 `/api/auth/logout` 요청  
- 백엔드는 Refresh Token 삭제, 쿠키 무효화로 세션 종료 처리  
