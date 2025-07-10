
package com.example.massagesystem.auth;

import com.example.massagesystem.token.RefreshToken;
import com.example.massagesystem.token.RefreshTokenService;
import com.example.massagesystem.user.User;
import com.example.massagesystem.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        return userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword())
                .map(user -> {
                    String jwt = tokenProvider.generateToken(user.getUsername());
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

                    // 액세스 토큰 쿠키 생성
                    ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", jwt)
                            .httpOnly(true)
                            .secure(true)
                            .path("/")
                            .maxAge(tokenProvider.getJwtExpirationInMs() / 1000) // 초 단위
                            .sameSite("None")
                            .build();

                    // 리프레시 토큰 쿠키 생성
                    ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                            .httpOnly(true)
                            .secure(true)
                            .path("/api/auth/refresh-token") // 리프레시 토큰은 특정 경로로만 전송
                            .maxAge(refreshTokenService.getRefreshTokenDurationMs() / 1000) // 초 단위
                            .sameSite("None")
                            .build();

                    response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
                    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

                    return ResponseEntity.ok(new LoginResponse(user.getUsername(), "로그인 성공"));
                })
                .orElse(ResponseEntity.status(401).body(new LoginResponse(null, "Invalid credentials")));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String requestRefreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    requestRefreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (requestRefreshToken != null) {
            return refreshTokenService.findByToken(requestRefreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        String newAccessToken = tokenProvider.generateToken(user.getUsername());

                        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(tokenProvider.getJwtExpirationInMs() / 1000)
                                .sameSite("None")
                                .build();

                        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
                        return ResponseEntity.ok(new LoginResponse(user.getUsername(), "새 액세스 토큰 발급"));
                    })
                    .orElse(ResponseEntity.status(403).body(new LoginResponse(null, "Refresh Token is not in database!")));
        }

        return ResponseEntity.status(403).body(new LoginResponse(null, "Refresh Token is missing!"));
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            // UserDetails 객체에서 사용자 이름을 가져옵니다.
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            return ResponseEntity.ok(new LoginResponse(username, "현재 로그인된 사용자"));
        }
        return ResponseEntity.status(401).body(new LoginResponse(null, "인증되지 않은 사용자"));
    }
}
