package com.leonmesquita.ecommerce.order_microservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leonmesquita.ecommerce.order_microservice.clients.CartsClient;
import com.leonmesquita.ecommerce.order_microservice.clients.ProductsClient;
import com.leonmesquita.ecommerce.order_microservice.dtos.OrderDTO;
import com.leonmesquita.ecommerce.order_microservice.dtos.feign.CartItemDTO;
import com.leonmesquita.ecommerce.order_microservice.dtos.feign.CartResponseDTO;
import com.leonmesquita.ecommerce.order_microservice.exceptions.GenericBadRequestException;
import com.leonmesquita.ecommerce.order_microservice.models.OrderItemModel;
import com.leonmesquita.ecommerce.order_microservice.models.OrderModel;
import com.leonmesquita.ecommerce.order_microservice.models.enums.OrderStatusEnum;
import com.leonmesquita.ecommerce.order_microservice.rabbitmq.ClearCartPublisher;
import com.leonmesquita.ecommerce.order_microservice.rabbitmq.UpdateStockPublisher;
import com.leonmesquita.ecommerce.order_microservice.repositories.OrderItemRepository;
import com.leonmesquita.ecommerce.order_microservice.repositories.OrderRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartsClient cartsClient;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    UpdateStockPublisher updateStockPublisher;

    @Autowired
    ProductsClient productsClient;

    @Autowired
    ClearCartPublisher clearCartPublisher;

    @Transactional
    public OrderModel save(OrderDTO dto) {
        CartResponseDTO cart = this.cartsClientFindCart(dto.getCartId());
        if (cart.getItems().isEmpty()) {
            throw new GenericBadRequestException("O carrinho está vazio");
        }
        this.productsClientCheckStock(cart.getItems());
        OrderModel orderModel = new OrderModel();
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        List<OrderItemModel> orderItems = new ArrayList<>();
        for (CartItemDTO item : cart.getItems() ) {
            OrderItemModel orderItemModel = new OrderItemModel();
            BeanUtils.copyProperties(item, orderItemModel, "id");
            orderItemModel.setOrder(orderModel);
            orderItemRepository.save(orderItemModel);
            totalPrice = totalPrice.add(item.getUnitPrice());
            orderItems.add(orderItemModel);
        }


        orderModel.setUserId(cart.getUserId());
        orderModel.setTotal(totalPrice);
        orderModel.setItems(orderItems);
        orderModel.setStatus(OrderStatusEnum.CREATED);

        try {
            updateStockPublisher.updateStock(orderItems);
            clearCartPublisher.clearCart(cart.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return orderRepository.save(orderModel);
    }

    public List<OrderModel> findAllUserOrders(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public CartResponseDTO cartsClientFindCart(Long id) {
        try {
            return cartsClient.getCartById(id).getBody();
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            String message = e.contentUTF8();
            throw new ResponseStatusException(
                    status,
                    message != null && !message.isBlank() ? message : "Erro ao comunicar com microserviço remoto"
            );
        }
    }

    public void productsClientCheckStock(List<CartItemDTO> items) {
        try {
            productsClient.checkProductStock(items);
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            String message = e.contentUTF8();
            throw new ResponseStatusException(
                    status,
                    message != null && !message.isBlank() ? message : "Erro ao comunicar com microserviço remoto"
            );
        }
    }


}
