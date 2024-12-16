package com.example.service_auth.service;

import com.example.service_auth.dto.request.AuthenticationRequest;
import com.example.service_auth.dto.request.IntrospectRequest;
import com.example.service_auth.dto.request.LogoutRequest;
import com.example.service_auth.dto.request.RefreshRequest;
import com.example.service_auth.dto.response.AuthenticationResponse;
import com.example.service_auth.dto.response.IntrospectResponse;
import com.example.service_auth.entity.InvalidatedToken;
import com.example.service_auth.entity.User;
import com.example.service_auth.exception.AppException;
import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.repository.InvalidatedTokenRepository;
import com.example.service_auth.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;

    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return  IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); // tao ra mot password encoder
        boolean authenticated =  passwordEncoder.matches(request.getPassword(), user.getPassword());  // kiem tra password
        var token = generateToken(user); // tao ra mot token
        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(authenticated)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID(); // lay ra jwt id
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime(); // lay ra thoi gian het han cua token

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build(); // tao ra mot token da bi invalidate

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject(); // lay ra username tu token

        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED)); // lay ra user tu username

        var token = generateToken(user); // tao ra mot token moi

        return AuthenticationResponse.builder().token(token).authenticated(true).build(); // tra ve token moi
    }
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true); // verify token

            String jit = signToken.getJWTClaimsSet().getJWTID(); // lay ra jwt id
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime(); // lay ra thoi gian het han cua token

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build(); // tao ra mot

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()// lay ra thoi gian het han cua token
                .getIssueTime() // lay ra thoi gian tao token
                .toInstant() // chuyen doi sang instant
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS) // cong them thoi gian refreshable
                .toEpochMilli()) // chuyen doi sang epoch milli
                : signedJWT.getJWTClaimsSet().getExpirationTime(); // lay ra thoi gian het han cua token

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // set username vao subject
                .issuer("service-auth") // set issuer
                .issueTime(new Date()) // set thoi gian tao token
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli())) // set thoi gian het han token
                .jwtID(UUID.randomUUID().toString()) // set jwt id
                .claim("scope", buildScope(user))  // set cac role cua user vao scope
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); // tao payload tu jwt claim set
        JWSObject jwsObject = new JWSObject(header, payload); // tao jws object tu header va payload
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes())); // sign jws object
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    // lay ra cac role cua user tu token de set vao scope
    private String buildScope (User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions()
                            .forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

}
