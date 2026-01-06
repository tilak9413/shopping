package org.example.shopping.repository;

import org.example.shopping.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    org.springframework.data.domain.Page<Sale> findByCustomerNameContainingIgnoreCaseOrCustomerMobileContainingOrderBySaleDateDesc(String name, String mobile, org.springframework.data.domain.Pageable pageable);
}
