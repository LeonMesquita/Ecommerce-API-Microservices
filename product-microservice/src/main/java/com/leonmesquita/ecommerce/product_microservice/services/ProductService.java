package com.leonmesquita.ecommerce.product_microservice.services;

import com.leonmesquita.ecommerce.product_microservice.dtos.ProductDTO;
import com.leonmesquita.ecommerce.product_microservice.models.ProductModel;
import com.leonmesquita.ecommerce.product_microservice.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public ProductModel save(ProductDTO dto) {
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(dto, productModel);
        return productRepository.save(productModel);
    }
}
