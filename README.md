# 🧘‍♀️ Massage System Backend

마사지 예약 시스템의 백엔드 애플리케이션입니다.  
사용자 인증, 마사지 서비스 정보, 예약 관리, 공지사항 등 프론트엔드와 통신하는 모든 RESTful API를 제공합니다.

Spring Security 기반 인증/인가 기능과 JWT + HttpOnly 쿠키를 이용한 세션 관리를 구현하여 보안을 강화했습니다.

---

## 🚀 기술 스택

| 분류       | 사용 기술                                  |
|------------|---------------------------------------------|
| 언어       | Java 17                                     |
| 프레임워크 | Spring Boot 3.x                             |
| 웹         | Spring Web (REST API)                       |
| 데이터베이스 | H2 (개발/테스트용 인메모리 or 파일 기반) |
| ORM        | Spring Data JPA (Hibernate)                 |
| 보안       | Spring Security, JWT, BCryptPasswordEncoder |
| 빌드 도구  | Maven                                       |
| 기타       | Lombok, JJWT (io.jsonwebtoken)              |

---

## 🔐 인증 및 세션 흐름

### 1. 로그인

- 프론트엔드에서 `/api/auth/login` 으로 사용자명과 비밀번호를 `POST` 요청
- `axios`는 `withCredentials: true` 옵션을 설정하여 HttpOnly 쿠키 수신 준비

### 2. 인증 및 토큰 발급

- `UserService`에서 사용자 검증
- 인증 성공 시:
  - `JwtTokenProvider`가 **Access Token** 발급 (짧은 만료 시간)
  - `RefreshTokenService`가 **Refresh Token** 발급 → DB에 저장 (긴 만료 시간)
- 두 토큰은 HttpOnly 쿠키(`accessToken`, `refreshToken`)로 클라이언트에 전달

### 3. 보호된 API 요청 처리

- 브라우저는 저장된 HttpOnly 쿠키를 자동으로 포함해 API 요청
- `JwtAuthenticationFilter`가 Access Token 검증 → 유효하면 인증된 사용자로 처리

### 4. Access Token 만료 시 재발급

- Access Token이 만료된 요청 → `401 Unauthorized` 응답
- `axios` 인터셉터가 `/api/auth/refresh-token` 으로 Refresh Token 이용해 새 Access Token 요청
- 백엔드는 Refresh Token 유효성 검사 후 새 Access Token 발급 → 쿠키로 전송
- 실패했던 원본 요청은 자동 재시도됨

### 5. 로그아웃

- 프론트엔드가 `/api/auth/logout` 호출
- 백엔드는:
  - Refresh Token DB에서 제거
  - Access/Refresh 쿠키 만료 설정으로 세션 종료

---

## 🧪 개발용 데이터베이스 (H2)

- 접속 URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/massagedb`
- 사용자명/비밀번호: `sa` / (빈값)

