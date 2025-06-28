package com.leonmesquita.ecommerce.auth_microservice.services;

import com.leonmesquita.ecommerce.auth_microservice.exceptions.GenericNotFoundException;
import com.leonmesquita.ecommerce.auth_microservice.models.UserModel;
import com.leonmesquita.ecommerce.auth_microservice.repositories.UserRepository;
import com.leonmesquita.ecommerce.auth_microservice.security.UserSpringSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws GenericNotFoundException {
        UserModel user = this.userRepository.findByEmail(username).orElseThrow(
                () -> new GenericNotFoundException("Usuário não encontrado: " + username)
        );

        return new UserSpringSecurity(user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getProfiles());
    }
}