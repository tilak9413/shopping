package org.example.shopping.controller;

import lombok.RequiredArgsConstructor;
import org.example.shopping.dto.ApiResponse;
import org.example.shopping.model.AddProductMdl;
import org.example.shopping.model.Sale;
import org.example.shopping.model.SaleItem;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.repository.SaleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sale")
@RequiredArgsConstructor
public class SaleController {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<ApiResponse> createSale(@RequestBody Sale sale) {
        try {
            // Set date
            sale.setSaleDate(LocalDateTime.now().toString());
            
            double totalAmount = 0.0;

            // Process each item to check stock and calculate totals
            for (SaleItem item : sale.getItems()) {
                // Find product
                Optional<AddProductMdl> productOpt = productRepository.findById(item.getProductId());
                if (productOpt.isEmpty()) {
                    throw new RuntimeException("Product not found: ID " + item.getProductId());
                }

                AddProductMdl product = productOpt.get();
                
                // Parse quantities (Assuming they are stored as Strings in Product model based on previous file views, but let's parse safely)
                int currentStock = Integer.parseInt(product.getQuantity());
                if (currentStock < item.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
                }

                // Deduct stock
                product.setQuantity(String.valueOf(currentStock - item.getQuantity()));
                productRepository.save(product);

                // Calculate subtotal
                double subTotal = item.getQuantity() * item.getUnitPrice();
                item.setSubTotal(subTotal);
                totalAmount += subTotal;
            }

            sale.setTotalAmount(totalAmount);
            saleRepository.save(sale);

            return ResponseEntity.ok(new ApiResponse(1, "Sale completed successfully!"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(0, "Sale failed: " + e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getAllSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        try {
            Pageable paging = PageRequest.of(page, size, Sort.by("saleDate").descending());
            Page<Sale> pageResult;

            if (search == null || search.trim().isEmpty()) {
                pageResult = saleRepository.findAll(paging);
            } else {
                pageResult = saleRepository.findByCustomerNameContainingIgnoreCaseOrCustomerMobileContainingOrderBySaleDateDesc(search, search, paging);
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sales", pageResult.getContent());
            responseData.put("currentPage", pageResult.getNumber());
            responseData.put("totalItems", pageResult.getTotalElements());
            responseData.put("totalPages", pageResult.getTotalPages());

            ApiResponse response = new ApiResponse(1, "Sales history fetched successfully");
            response.setData(responseData);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
             e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(0, "Failed to fetch sales: " + e.getMessage()));
        }
    }
}
