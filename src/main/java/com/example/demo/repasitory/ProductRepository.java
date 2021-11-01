package com.example.demo.repasitory;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {

    List<Product> getProductsByCategoryId(Long id);

    Optional<Product> findById(long id);

    @Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.description, ' ', p.url, ' ', p.price) LIKE %?1%")
    public List<Product> search(String keyword);
}
