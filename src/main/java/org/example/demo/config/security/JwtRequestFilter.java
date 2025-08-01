package org.example.demo.config.security;

import org.example.demo.common.util.JwtUtil;
import org.example.demo.common.util.ResponseUtil;
import org.example.demo.common.util.UserContextUtil;
import org.example.demo.domain.emuns.ResponseEnum;
import org.example.demo.domain.vo.ResponseVO;
import org.example.demo.service.serviceImpl.UserDetailsServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson 用于 JSON 处理

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
            throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        final String authorizationHeader = request.getHeader("Authorization");
        if((requestURI.startsWith("/api/attendance")) ) {
            chain.doFilter(request, response);
            return;
        }
        // 允许 /api/user/login 和 /api/user/register 通过，不需要 JWT 认证
        if (   requestURI.endsWith("/user/login")
                || requestURI.endsWith("/user/register")
                || requestURI.endsWith("/user/resetLimit")
                || requestURI.endsWith("/experienceUser/register")
                || requestURI.endsWith("/experienceUser/login")
                || requestURI.endsWith("/experienceUser/getVerificationCode")
                || requestURI.endsWith("/experienceUser/deleteById")
                || requestURI.startsWith("/reference-data")

        ) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (authorizationHeader == null || authorizationHeader.isEmpty()) {
                log.warn("请求头中缺少 Authorization");
                sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ResponseUtil.error(ResponseEnum.UNAUTHORIZED, "未提供授权令牌，请登录后重试"));
                return;
            }

            Long userId = JwtUtil.extractUserId(authorizationHeader);
            UserContextUtil.setUserId(userId);
            if (userId == null) {
                log.warn("JWT 不包含有效的 userId");
                sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ResponseUtil.error(ResponseEnum.BAD_REQUEST, "无效的 JWT 令牌，缺少用户 ID"));
                return;
            }

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(String.valueOf(userId));
            if (userDetails == null) {
                log.warn("无法加载用户信息，userId: " + userId);
                sendJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ResponseUtil.error(ResponseEnum.INTERNAL_SERVER_ERROR, "无法加载用户信息，请稍后再试"));
                return;
            }

            if (JwtUtil.validateToken(authorizationHeader, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("JWT 令牌验证失败，userId: " + userId);
                sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ResponseUtil.error(ResponseEnum.BAD_REQUEST, "无效的 JWT 令牌，请重新登录"));
                return;
            }
        } catch (Exception e) {
            log.error("JWT 认证过程中发生错误", e);
            sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ResponseUtil.error(ResponseEnum.UNAUTHORIZED, "JWT 认证失败"));
            return;
        }

        // 继续执行过滤器链
        try {
            chain.doFilter(request, response);
        }finally {
            UserContextUtil.clear();
        }

    }

    /**
     * 发送 JSON 格式的错误响应
     */
    private void sendJsonResponse(HttpServletResponse response, int status, ResponseVO<?> responseVO) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(responseVO));
    }
}
