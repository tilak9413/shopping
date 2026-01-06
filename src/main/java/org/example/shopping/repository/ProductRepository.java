package org.example.shopping.repository;

import org.example.shopping.model.AddProductMdl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<AddProductMdl, Long> {
    org.springframework.data.domain.Page<AddProductMdl> findByProductNameContainingIgnoreCase(String productName, org.springframework.data.domain.Pageable pageable);
}
