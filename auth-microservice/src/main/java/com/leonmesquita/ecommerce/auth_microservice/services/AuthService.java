package com.leonmesquita.ecommerce.auth_microservice.services;

import com.leonmesquita.ecommerce.auth_microservice.dtos.LoginDTO;
import com.leonmesquita.ecommerce.auth_microservice.dtos.RequestRefreshDTO;
import com.leonmesquita.ecommerce.auth_microservice.dtos.TokenResponseDTO;
import com.leonmesquita.ecommerce.auth_microservice.dtos.UserDTO;
import com.leonmesquita.ecommerce.auth_microservice.dtos.rabbitmq.DataCartRequestDTO;
import com.leonmesquita.ecommerce.auth_microservice.exceptions.AuthorizationException;
import com.leonmesquita.ecommerce.auth_microservice.exceptions.GenericBadRequestException;
import com.leonmesquita.ecommerce.auth_microservice.exceptions.GenericConflictException;
import com.leonmesquita.ecommerce.auth_microservice.exceptions.GenericNotFoundException;
import com.leonmesquita.ecommerce.auth_microservice.models.UserModel;
import com.leonmesquita.ecommerce.auth_microservice.models.enums.ProfileEnum;
import com.leonmesquita.ecommerce.auth_microservice.rabbitmq.CreateCartPublisher;
import com.leonmesquita.ecommerce.auth_microservice.repositories.UserRepository;
import com.leonmesquita.ecommerce.auth_microservice.security.JWTUtil;
import com.leonmesquita.ecommerce.auth_microservice.security.UserSpringSecurity;
import io.micrometer.common.lang.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ResponseStatusException;


import java.util.Collection;
import java.util.List;
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


    @Autowired
    CreateCartPublisher createCartPublisher;

    public UserModel register(UserDTO dto) {
        this.existsByEmail(dto.getEmail(), null);
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(dto, userModel);
        userModel.setPassword(this.bCryptPasswordEncoder.encode(dto.getPassword()));
        userModel.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        UserModel createdUser = userRepository.save(userModel);
        try {
            DataCartRequestDTO data = new DataCartRequestDTO();
            data.setUserId(createdUser.getId());
            createCartPublisher.createCart(data);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao criar carrinho");
        }
        return createdUser;
    }

    public void existsByEmail(String email,  @Nullable Long id) {
        Optional<UserModel> user = userRepository.findByEmail(email);
        if (user.isPresent() && !Objects.equals(user.get().getId(), id)) {
            throw new GenericConflictException("Já existe um usuário com o email " + email);
        }
    }


    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new GenericNotFoundException("Usuário com o email " + email + " não encontrado")
        );
    }



    public TokenResponseDTO login(LoginDTO dto) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication authentication = authenticationManager.authenticate(authToken);
        UserSpringSecurity user = (UserSpringSecurity) authentication.getPrincipal();
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String accessToken = jwtUtil.generateToken(user.getUsername(), roles, jwtUtil.getTokenExpirationHour());
        String refreshToken = jwtUtil.generateToken(user.getUsername(), roles, jwtUtil.getRefreshTokenExpirationHour());

        return new TokenResponseDTO(accessToken, refreshToken);
    }

//    public TokenResponseDTO refreshToken(RequestRefreshDTO dto) {
//        String refreshToken = dto.refreshToken();
//        if (refreshToken == null || refreshToken.isEmpty()) {
//            throw new GenericBadRequestException("Refresh token ausente");
//        }
//
//        String email = jwtUtil.isValidToken(refreshToken);
//        if (email.isEmpty()) {
//            throw new AuthorizationException("Refresh token inválido");
//        }
//
//        // Buscar o usuário para recuperar as roles
//        UserModel user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new AuthorizationException("Usuário não encontrado"));
//
//        Collection<? extends GrantedAuthority> authorities = user.getProfiles().stream()
//                .map(p -> new SimpleGrantedAuthority(p.getDescription()))
//                .collect(Collectors.toSet());
//
//        String newAccessToken = jwtUtil.generateToken(email, authorities, jwtUtil.getTokenExpirationHour());
//        String newRefreshToken = jwtUtil.generateToken(email, authorities, jwtUtil.getRefreshTokenExpirationHour());
//
//        return new TokenResponseDTO(newAccessToken, newRefreshToken);
//    }
}
