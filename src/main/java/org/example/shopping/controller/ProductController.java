package org.example.shopping.controller;

import lombok.RequiredArgsConstructor;
import org.example.shopping.dto.ApiResponse;
import org.example.shopping.model.AddProductMdl;
import org.example.shopping.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductMdl product) {
        try {
            productRepository.save(product);
            return ResponseEntity.ok(new ApiResponse(1, "Product added successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(0, "Failed to add product: " + e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        try {
            Pageable paging = PageRequest.of(page, size, Sort.by("id").descending());
            Page<AddProductMdl> pageResult;

            if (search == null || search.trim().isEmpty()) {
                pageResult = productRepository.findAll(paging);
            } else {
                pageResult = productRepository.findByProductNameContainingIgnoreCase(search, paging);
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("products", pageResult.getContent());
            responseData.put("currentPage", pageResult.getNumber());
            responseData.put("totalItems", pageResult.getTotalElements());
            responseData.put("totalPages", pageResult.getTotalPages());

            ApiResponse response = new ApiResponse(1, "Products fetched successfully");
            response.setData(responseData); // Need to make sure ApiResponse has setData or public field
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(0, "Failed to fetch products: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody AddProductMdl productDetails) {
        try {
            Optional<AddProductMdl> productData = productRepository.findById(id);
            if (productData.isPresent()) {
                AddProductMdl _product = productData.get();
                _product.setProductName(productDetails.getProductName());
                _product.setUnitPrice(productDetails.getUnitPrice());
                _product.setQuantity(productDetails.getQuantity());
                _product.setLocationNumber(productDetails.getLocationNumber());
                _product.setColor(productDetails.getColor());
                _product.setSize(productDetails.getSize());
                _product.setDescription(productDetails.getDescription());
                // Don't update dateTime typically, or maybe updated_at? keeping simple for now
                
                productRepository.save(_product);
                return ResponseEntity.ok(new ApiResponse(1, "Product updated successfully!"));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(0, "Product not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(0, "Failed to update product: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(1, "Product deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(0, "Failed to delete product: " + e.getMessage()));
        }
    }
}
