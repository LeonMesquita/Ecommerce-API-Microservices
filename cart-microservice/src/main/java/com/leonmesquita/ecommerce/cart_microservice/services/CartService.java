package com.leonmesquita.ecommerce.cart_microservice.services;

import com.leonmesquita.ecommerce.cart_microservice.clients.ProductsClient;
import com.leonmesquita.ecommerce.cart_microservice.clients.UsersClient;
import com.leonmesquita.ecommerce.cart_microservice.dtos.CartDTO;
import com.leonmesquita.ecommerce.cart_microservice.dtos.CartItemDTO;
import com.leonmesquita.ecommerce.cart_microservice.dtos.feign.CartItemResponseDTO;
import com.leonmesquita.ecommerce.cart_microservice.dtos.feign.CartResponseDTO;
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

import java.util.ArrayList;
import java.util.List;
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


    public UserResponseDTO usersClientFindUser(Long id) {
        try {
            return usersClient.getUserById(id).getBody();
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


    @Transactional
    public CartModel addItemsToCart(Long id, CartItemDTO dto) {
            UserResponseDTO user = this.usersClientFindUser(id);
            ProductResponseDTO product = this.productsClientFindProduct(dto.getProductId());
            if (dto.getAmount() > product.getStock()) {
                throw new GenericBadRequestException("Quantidade de itens não pode exceder o estoque");
            }
            CartModel cart = this.findByUserId(user.getId());
            CartItemModel cartItemModel = new CartItemModel();
            BeanUtils.copyProperties(dto, cartItemModel);
            cartItemModel.setUnitPrice(product.getPrice());
            cartItemModel.setCart(cart);
            cart.getItems().add(cartItemModel);
            cartItemRepository.save(cartItemModel);
            return cartRepository.save(cart);
    }
    @Transactional
    public CartModel findByUser(Long id) {
        UserResponseDTO user = this.usersClientFindUser(id);

        return cartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new GenericNotFoundException("Carrinho não encontrado")
        );
    }

    @Transactional
    public CartModel findById(Long id) {
        CartModel cart = cartRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("Carrinho não encontrado")
        );
        UserResponseDTO user = this.usersClientFindUser(cart.getUserId());
        return cart;
    }

    @Transactional
    public CartModel findByUserId(Long id) {
        return cartRepository.findByUserId(id).orElseThrow(
                () -> new GenericNotFoundException("Carrinho não encontrado")
        );
    }

    @Transactional
    public void removeItemFromCart(Long id, Long userId) {
        CartItemModel cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("Item não encontrado")
        );
        UserResponseDTO user = this.usersClientFindUser(userId);

        if (!Objects.equals(user.getId(), cartItem.getCart().getUserId())) {
            throw new GenericForbiddenException("Usuário não possui autorização");
        }

        cartItemRepository.deleteById(id);
    }

    @Transactional
    public void clearCart(Long cartId) {
        CartModel cart = this.findById(cartId);
        for (CartItemModel item : cart.getItems()) {
            cartItemRepository.deleteById(item.getId());
        }
    }

    public CartResponseDTO toResponseDTO(CartModel cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setCreationDate(cart.getCreationDate());
        dto.setActive(cart.isActive());

        List<CartItemResponseDTO> itemDTOs = cart.getItems() != null
                ? cart.getItems().stream().map(item -> {
            CartItemResponseDTO itemDTO = new CartItemResponseDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductId(item.getProductId());
            itemDTO.setAmount(item.getAmount());
            itemDTO.setUnitPrice(item.getUnitPrice());
            return itemDTO;
        }).toList()
                : new ArrayList<>();

        dto.setItems(itemDTOs);
        return dto;
    }
}
