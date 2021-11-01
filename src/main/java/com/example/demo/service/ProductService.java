package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {


    List<Product> getProducts();

    Product getProduct(Long id);

    void saveProduct(Product product);

    List<Product> getProductsByCategory(Long categoryId);

    Long getProductsTotalSumByCategory(Long categoryId);

    List<Product> getProductsByPage(Pageable pageable);

    Optional<Product> findById(long id);

    Page<Product> productpages(PageRequest pageRequest);

}
