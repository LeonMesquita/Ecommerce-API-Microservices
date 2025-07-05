package com.leonmesquita.ecommerce.product_microservice.services;

import com.leonmesquita.ecommerce.product_microservice.dtos.ProductDTO;
import com.leonmesquita.ecommerce.product_microservice.dtos.rabbitmq.Order;
import com.leonmesquita.ecommerce.product_microservice.dtos.rabbitmq.OrderItem;
import com.leonmesquita.ecommerce.product_microservice.dtos.rabbitmq.OrderStatusEnum;
import com.leonmesquita.ecommerce.product_microservice.exceptions.GenericBadRequestException;
import com.leonmesquita.ecommerce.product_microservice.exceptions.GenericNotFoundException;
import com.leonmesquita.ecommerce.product_microservice.models.ProductModel;
import com.leonmesquita.ecommerce.product_microservice.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public ProductModel save(ProductDTO dto) {
//        if (dto.getStock() < 1) {
//            throw new GenericBadRequestException("");
//        }
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(dto, productModel);
        return productRepository.save(productModel);
    }

    public Page<ProductModel> findAll(Pageable pageable) {
        return productRepository.listAllActiveProducts(pageable);
    }

    public ProductModel findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("Produto com o id " + id + " não encontrado.")
        );
    }

    public ProductModel update(Long id, ProductDTO dto) {
        ProductModel product = this.findById(id);
        BeanUtils.copyProperties(dto, product, "id");
        return productRepository.save(product);
    }

    public void updateStock(Order order) {
        for (OrderItem orderItem : order.getItems()) {
            ProductModel product = this.findById(orderItem.getProductId());
            if (order.getStatus() == OrderStatusEnum.CANCELED) {
                product.setStock(product.getStock() + orderItem.getAmount());
            } else {
                if (orderItem.getAmount() > product.getStock()) {
                    throw new GenericBadRequestException("Quantidade comprada maior que o estoque disponível");
                }
                product.setStock(product.getStock() - orderItem.getAmount());
            }
            productRepository.save(product);
        }
    }


    public void checkStockAvailable(List<OrderItem> dto) {
        for (OrderItem orderItem : dto) {
            ProductModel product = this.findById(orderItem.getProductId());
            if (orderItem.getAmount() > product.getStock()) {
                throw new GenericBadRequestException("Quantidade comprada maior que o estoque disponível");
            }
        }
    }


    public void delete(Long id) {
        ProductModel product = this.findById(id);
        product.setActive(false);
        productRepository.save(product);
    }

}
