package com.spe.iiitbcms.service;

import com.spe.iiitbcms.dto.RegisterRequest;
import com.spe.iiitbcms.exceptions.CMSException;
import com.spe.iiitbcms.model.EmailValidator;
import com.spe.iiitbcms.model.NotificationEmail;
import com.spe.iiitbcms.model.User;
import com.spe.iiitbcms.model.VerificationToken;
import com.spe.iiitbcms.repository.UserRepository;
import com.spe.iiitbcms.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final EmailValidator emailValidator;


    public void signup(RegisterRequest registerRequest) {


        boolean isEmailValid = emailValidator.isEmailPresent(registerRequest.getEmail());

        if(isEmailValid)
            throw new IllegalStateException("Email already exists");

        User user = new User();
        user.setRollNo(registerRequest.getRollNo());
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setLocalDateTime(LocalDateTime.now());
        user.setEnabled(true);
        user.setRole(registerRequest.getRole());

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to IIITB CMS, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByRollNo(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String rollNo = verificationToken.getUser().getRollNo();
        User user = userRepository.findByRollNo(rollNo).orElseThrow(() -> new CMSException("User not found with name - " + rollNo));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new CMSException("Invalid Token")));
    }

//    public AuthenticationResponse login(LoginRequest loginRequest) {
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getRollNo(),
//                loginRequest.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        String token = jwtProvider.generateToken(authenticate);
//        return AuthenticationResponse.builder()
//                .authenticationToken(token)
//                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
//                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
//                .rollNo(loginRequest.getRollNo())
//                .build();
//    }
//
//    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
//        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
//        String token = jwtProvider.generateTokenWithRollNo(refreshTokenRequest.getRollNo());
//        return AuthenticationResponse.builder()
//                .authenticationToken(token)
//                .refreshToken(refreshTokenRequest.getRefreshToken())
//                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
//                .rollNo(refreshTokenRequest.getRollNo())
//                .build();
//    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}