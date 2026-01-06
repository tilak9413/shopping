    package org.example.shopping.model;

    import jakarta.persistence.*;
    import lombok.Data;

    @Entity
    @Table(name = "products")
    @Data
    public class AddProductMdl {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String productName;

        @Column(nullable = false)
        private String unitPrice;

        @Column(nullable = false)
        private String quantity;

        @Column(nullable = false)
        private String locationNumber;

        @Column(nullable = false)
        private String color;

        @Column(nullable = false)
        private String size;

        @Column(nullable = false)
        private String dateTime;

        @Column(nullable = false, name = "description")
        private String description;
    }
