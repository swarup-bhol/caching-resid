package com.techtuds.cachingredis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.techtuds.cachingredis.entity.Product;
import com.techtuds.cachingredis.repository.ProductRepository;


@RestController
public class ProductController {
	
	@Autowired
	ProductRepository repository;
	
	
	
	@PostMapping("/product")
//	@CachePut(cacheNames = "product", key = "#id")
	public Product editProduct(@RequestBody Product product) throws Exception {
		return repository.save(product);
	}
	
	
	@GetMapping("/product/{id}")  
	@Cacheable(value = "product", key = "#id")
	public Product getProductById(@PathVariable long id) throws Exception {
		  return repository.findById(id).orElseThrow(() -> new Exception("Product Not found"));
		  
	}
	
	@PutMapping("/product/{id}")
	@CachePut(cacheNames = "product", key = "#id")
	public Product editProduct(@PathVariable long id, @RequestBody Product product) throws Exception {
		repository.findById(id).orElseThrow(() -> new Exception("Product Not found"));
		return repository.save(product);
	}
	
	
	@DeleteMapping("/product/{id}")
	@CacheEvict(cacheNames = "product", key = "#id", beforeInvocation = true)
	public String removeProductById(@PathVariable long id) throws Exception {
		Product product = repository.findById(id).orElseThrow(() -> new Exception("Product Not found"));
		repository.delete(product);
		return  "Deleted Successfully";
	}

}
