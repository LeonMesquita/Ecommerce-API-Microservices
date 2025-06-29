package com.leonmesquita.ecommerce.product_microservice.services;

import com.leonmesquita.ecommerce.product_microservice.dtos.ProductDTO;
import com.leonmesquita.ecommerce.product_microservice.exceptions.GenericNotFoundException;
import com.leonmesquita.ecommerce.product_microservice.models.ProductModel;
import com.leonmesquita.ecommerce.product_microservice.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

}
