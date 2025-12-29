package org.example.shopping.controller;

import lombok.RequiredArgsConstructor;
import org.example.shopping.model.AddProductMdl;
import org.example.shopping.repository.ProductRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class AddProduct {

    private final ProductRepository productRepository;
    @PostMapping("/add")
    public String addProduct(@RequestBody AddProductMdl product) {
        productRepository.save(product);
        return "Product added successfully!";
    }

}
