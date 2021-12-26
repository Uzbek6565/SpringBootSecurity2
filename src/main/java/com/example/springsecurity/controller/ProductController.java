package com.example.springsecurity.controller;

import com.example.springsecurity.entity.Product;
import com.example.springsecurity.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;
// In order to use PreAuthorize annotation, first, use EnableGlobalMethodSecurity annotation on Configuration class and enable prePostEnabled
//    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'MANAGER', 'USER')")
    @PreAuthorize(value = "hasAuthority('READ_ALL_PRODUCT')")
    @GetMapping
    public HttpEntity<?> getProduct() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();     //by doing this you can know who is connected and it's authorities
        return ResponseEntity.ok(productRepository.findAll());
    }

//    @PreAuthorize(value = "hasRole('DIRECTOR')")
    @PreAuthorize(value = "hasAuthority('EDIT_PRODUCT')")
    @PostMapping
    public HttpEntity<?> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productRepository.save(product));
    }

//    @PreAuthorize(value = "hasRole('DIRECTOR')")
    @PreAuthorize(value = "hasAuthority('EDIT_PRODUCT')")
    @PutMapping("/{id}")
    public HttpEntity<?> editProduct(@PathVariable Integer id, @RequestBody Product product) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product1 = optionalProduct.get();
            product1.setName(product.getName());
            productRepository.save(product1);
            return ResponseEntity.ok(product1);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize(value = "hasAuthority('READ_ONE_PRODUCT')")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return ResponseEntity.status(optionalProduct.isPresent() ? 200 : 404).body(optionalProduct.orElse(null));
    }

//    @PreAuthorize(value = "hasRole('DIRECTOR')")
    @PreAuthorize(value = "hasAuthority('DELETE_PRODUCT')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteProduct(@PathVariable Integer id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
