package org.example.shopping.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "sales")
@Data
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerMobile;
    private String customerAddress;

    private Double totalAmount;
    private String saleDate; // Storing as String for simplicity as per other models, or LocalDateTime

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id") // Unidirectional join for simplicity
    private List<SaleItem> items;
}
