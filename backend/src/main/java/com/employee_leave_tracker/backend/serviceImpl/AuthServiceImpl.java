package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.dto.auth.LoginRequest;
import com.employee_leave_tracker.backend.dto.auth.LoginResponse;
import com.employee_leave_tracker.backend.exception.UnauthorizedException;
import com.employee_leave_tracker.backend.security.AppUserPrincipal;
import com.employee_leave_tracker.backend.security.JwtUtil;
import com.employee_leave_tracker.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        AppUserPrincipal principal = (AppUserPrincipal) auth.getPrincipal();

        if (principal == null) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(principal);

        return new LoginResponse(token);
    }
}
