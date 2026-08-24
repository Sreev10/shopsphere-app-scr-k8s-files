package com.shopsphere.product;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository repository;
    public ProductController(ProductRepository repository){this.repository=repository;}

    @GetMapping public List<Product> all(@RequestParam(required=false) String category){
        return category == null ? repository.findAll() : repository.findByCategoryIgnoreCase(category);
    }
    @GetMapping("/{id}") public Product one(@PathVariable Long id){
        return repository.findById(id).orElseThrow();
    }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Product create(@Valid @RequestBody Product product){
        return repository.save(product);
    }
}
