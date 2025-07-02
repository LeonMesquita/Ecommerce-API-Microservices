package com.leonmesquita.ecommerce.cart_microservice.services;

import com.leonmesquita.ecommerce.cart_microservice.clients.ProductsClient;
import com.leonmesquita.ecommerce.cart_microservice.clients.UsersClient;
import com.leonmesquita.ecommerce.cart_microservice.dtos.CartDTO;
import com.leonmesquita.ecommerce.cart_microservice.dtos.CartItemDTO;
import com.leonmesquita.ecommerce.cart_microservice.dtos.feign.ProductResponseDTO;
import com.leonmesquita.ecommerce.cart_microservice.dtos.feign.UserResponseDTO;
import com.leonmesquita.ecommerce.cart_microservice.exceptions.GenericBadRequestException;
import com.leonmesquita.ecommerce.cart_microservice.exceptions.GenericForbiddenException;
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

import java.util.Objects;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UsersClient usersClient;

    @Autowired
    ProductsClient productsClient;


    public UserResponseDTO usersClientFindUser(String email) {
        try {
            return usersClient.getUserByEmail(email).getBody();
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            String message = e.contentUTF8();
            throw new ResponseStatusException(
                    status,
                    message != null && !message.isBlank() ? message : "Erro ao comunicar com microserviço remoto"
            );
        }
    }

    public ProductResponseDTO productsClientFindProduct(Long id) {
        try {
            return productsClient.findProductById(id).getBody();
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            String message = e.contentUTF8();
            throw new ResponseStatusException(
                    status,
                    message != null && !message.isBlank() ? message : "Erro ao comunicar com microserviço remoto"
            );
        }
    }


    public CartModel createCart(CartDTO dto) {
        CartModel cartModel = new CartModel();
        BeanUtils.copyProperties(dto, cartModel);
        return cartRepository.save(cartModel);
    }


    public CartModel addItemsToCart(String userEmail, CartItemDTO dto) {
            UserResponseDTO user = this.usersClientFindUser(userEmail);
            ProductResponseDTO product = this.productsClientFindProduct(dto.getProductId());
            if (dto.getAmount() > product.getStock()) {
                throw new GenericBadRequestException("Quantidade de itens não pode exceder o estoque");
            }
            CartModel cart = this.findByUserId(user.getId());
            CartItemModel cartItemModel = new CartItemModel();
            BeanUtils.copyProperties(dto, cartItemModel);
            cartItemModel.setCart(cart);
            cartItemRepository.save(cartItemModel);
            return cart;
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

    @Transactional
    public void removeItemFromCart(Long id, String userEmail) {
        CartItemModel cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("Item não encontrado")
        );
        UserResponseDTO user = this.usersClientFindUser(userEmail);

        if (!Objects.equals(user.getId(), cartItem.getCart().getUserId())) {
            throw new GenericForbiddenException("Usuário não possui autorização");
        }

        cartItemRepository.deleteById(id);
    }
}
