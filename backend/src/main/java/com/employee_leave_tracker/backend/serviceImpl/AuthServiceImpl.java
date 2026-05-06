package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.dto.auth.LoginRequest;
import com.employee_leave_tracker.backend.dto.auth.LoginResponse;
import com.employee_leave_tracker.backend.dto.auth.PasswordChangeDto;
import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
import com.employee_leave_tracker.backend.exception.UnauthorizedException;
import com.employee_leave_tracker.backend.model.auth.UserAccount;
import com.employee_leave_tracker.backend.repository.auth.UserAccountRepository;
import com.employee_leave_tracker.backend.security.AppUserPrincipal;
import com.employee_leave_tracker.backend.security.JwtUtil;
import com.employee_leave_tracker.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;

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



    @Override
    @Transactional
    public void changePassword(PasswordChangeDto request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();

        if (principal == null) {
            throw new UnauthorizedException("User not authenticated");
        }

        UserAccount user = userAccountRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UnauthorizedException("User not found"));


        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new ArgumentNotValidException("Current password is incorrect");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPasswordHash())) {
            throw new ArgumentNotValidException("New password must be different");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userAccountRepository.save(user);
    }
}
