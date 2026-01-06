package org.example.shopping.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sale_items")
@Data
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String productName;
    private Double unitPrice;
    private Integer quantity;
    private Double subTotal;
}
