package com.leonmesquita.ecommerce.cart_microservice.services;

import com.leonmesquita.ecommerce.cart_microservice.clients.UsersClient;
import com.leonmesquita.ecommerce.cart_microservice.dtos.CartDTO;
import com.leonmesquita.ecommerce.cart_microservice.dtos.CartItemDTO;
import com.leonmesquita.ecommerce.cart_microservice.dtos.feign.UserResponseDTO;
import com.leonmesquita.ecommerce.cart_microservice.exceptions.GenericNotFoundException;
import com.leonmesquita.ecommerce.cart_microservice.models.CartItemModel;
import com.leonmesquita.ecommerce.cart_microservice.models.CartModel;
import com.leonmesquita.ecommerce.cart_microservice.repositories.CartItemRepository;
import com.leonmesquita.ecommerce.cart_microservice.repositories.CartRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UsersClient usersClient;


    public CartModel createCart(CartDTO dto) {
        CartModel cartModel = new CartModel();
        BeanUtils.copyProperties(dto, cartModel);
        return cartRepository.save(cartModel);
    }


    public CartModel addItemsToCart(String userEmail, CartItemDTO dto) {
        try {
            ResponseEntity<UserResponseDTO> user = usersClient.getUserByEmail(userEmail);
            CartModel cart = this.findByUserId(user.getBody().getId());
            CartItemModel cartItemModel = new CartItemModel();
            BeanUtils.copyProperties(dto, cartItemModel);
            cartItemModel.setCart(cart);
            cartItemRepository.save(cartItemModel);
            return cart;
        } catch (FeignException.FeignClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao adicionar item no carrinho!");
        }
    }
    @Transactional
    public CartModel findByUser(String userEmail) {
        try {
            ResponseEntity<UserResponseDTO> user = usersClient.getUserByEmail(userEmail);
            return cartRepository.findByUserId(user.getBody().getId()).orElseThrow(
                    () -> new GenericNotFoundException("Carrinho não encontrado")
            );

        } catch (FeignException.FeignClientException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!");
        }
    }

    @Transactional
    public CartModel findByUserId(Long id) {
        return cartRepository.findByUserId(id).orElseThrow(
                () -> new GenericNotFoundException("Carrinho não encontrado")
        );
    }

}
