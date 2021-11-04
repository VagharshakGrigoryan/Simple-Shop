package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.repasitory.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;

    @Override
    public List<Product> getProducts() {

        return new ArrayList<>(productRepository.findAll());
    }

    @Override
    public Page<Product> productpages(PageRequest pageRequest) {

        return productRepository.findAll(pageRequest);
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.getProductsByCategoryId(categoryId);
    }

    @Override
    public Long getProductsTotalSumByCategory(Long categoryId) {
        List<Product> products = getProductsByCategory(categoryId);
        int totalSum = products.stream().mapToInt(Product::getPrice).sum();
        return (long) totalSum;
    }

    @Override
    public List<Product> getProductsByPage(Pageable pageable) {
        return productRepository.findAll(pageable).getContent();
    }

    @Override
    public Optional<Product> findById(long id) {
        return Optional.empty();
    }

    public Product get(Long id) {
        return productRepository.findById(id).get();
    }
    public List<Product> listAll(String keyword) {
        if (keyword != null) {
            return productRepository.search(keyword);
        }
        return productRepository.findAll();
    }
}

