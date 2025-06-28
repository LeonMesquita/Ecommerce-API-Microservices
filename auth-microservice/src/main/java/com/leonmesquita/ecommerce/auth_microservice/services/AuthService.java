package com.leonmesquita.ecommerce.auth_microservice.services;

import com.leonmesquita.ecommerce.auth_microservice.dtos.LoginDTO;
import com.leonmesquita.ecommerce.auth_microservice.dtos.RequestRefreshDTO;
import com.leonmesquita.ecommerce.auth_microservice.dtos.TokenResponseDTO;
import com.leonmesquita.ecommerce.auth_microservice.dtos.UserDTO;
import com.leonmesquita.ecommerce.auth_microservice.exceptions.AuthorizationException;
import com.leonmesquita.ecommerce.auth_microservice.exceptions.GenericBadRequestException;
import com.leonmesquita.ecommerce.auth_microservice.exceptions.GenericConflictException;
import com.leonmesquita.ecommerce.auth_microservice.models.UserModel;
import com.leonmesquita.ecommerce.auth_microservice.models.enums.ProfileEnum;
import com.leonmesquita.ecommerce.auth_microservice.repositories.UserRepository;
import com.leonmesquita.ecommerce.auth_microservice.security.JWTUtil;
import com.leonmesquita.ecommerce.auth_microservice.security.UserSpringSecurity;
import io.micrometer.common.lang.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;
    public UserModel register(UserDTO dto) {
        this.existsByEmail(dto.getEmail(), null);
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(dto, userModel);
        userModel.setPassword(this.bCryptPasswordEncoder.encode(dto.getPassword()));
        userModel.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        return userRepository.save(userModel);
    }

    public void existsByEmail(String email,  @Nullable Long id) {
        Optional<UserModel> user = userRepository.findByEmail(email);
        if (user.isPresent() && !Objects.equals(user.get().getId(), id)) {
            throw new GenericConflictException("Já existe um usuário com o email " + email);
        }
    }


    public TokenResponseDTO login(LoginDTO dto) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication authentication = authenticationManager.authenticate(authToken);
        UserSpringSecurity user = (UserSpringSecurity) authentication.getPrincipal();
        String accessToken = jwtUtil.generateToken(user.getUsername(), jwtUtil.getTokenExpirationHour());
        String refreshToken = jwtUtil.generateToken(user.getUsername(), jwtUtil.getRefreshTokenExpirationHour());

        return new TokenResponseDTO(accessToken, refreshToken);
    }

    public TokenResponseDTO refreshToken(RequestRefreshDTO dto) {
        String refreshToken = dto.refreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new GenericBadRequestException("Refresh token ausente");
        }
        String email = jwtUtil.isValidToken(refreshToken);
        if (email.isEmpty()) {
            throw new AuthorizationException("Refresh token inválido");
        }
        String newAccessToken = jwtUtil.generateToken(email, jwtUtil.getTokenExpirationHour());
        String newRefreshToken = jwtUtil.generateToken(email, jwtUtil.getRefreshTokenExpirationHour());

        return new TokenResponseDTO(newAccessToken, newRefreshToken);
    }
}
