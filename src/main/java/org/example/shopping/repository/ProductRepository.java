package org.example.shopping.repository;

import org.example.shopping.model.AddProductMdl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<AddProductMdl, Long> {
    // No code needed here! .save() is built-in.
}
