package org.woolf.EComService.services;

import org.woolf.EComService.dtos.CreateProductDto;
import org.woolf.EComService.exceptions.InvalidDataException;
import org.woolf.EComService.exceptions.NotFoundException;
import org.woolf.EComService.models.product.Category;
import org.woolf.EComService.models.product.Product;
import org.woolf.EComService.repositories.CategoryRepository;
import org.woolf.EComService.repositories.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product createProduct(CreateProductDto createProductDto) throws InvalidDataException {

        if(createProductDto.getCategory() == null|| createProductDto.getTitle() == null || createProductDto.getDescription() == null
        || createProductDto.getRating()<0 || createProductDto.getPrice()<0 || createProductDto.getStockQuantity()<1)
        {
            throw new InvalidDataException("Invalid Request Body");
        }

        Optional<Category> optionalCategory = categoryRepository.findByName(createProductDto.getCategory());
        Category addCategory;
        if (optionalCategory.isPresent()) {
            addCategory = optionalCategory.get();
        }
        else {
            addCategory = new Category();
            addCategory.setName(createProductDto.getCategory());
            categoryRepository.save(addCategory);
        }
        Product product = new Product();
        product.setCategory(addCategory);
        product.setTitle(createProductDto.getTitle());
        product.setDescription(createProductDto.getDescription());
        product.setPrice(createProductDto.getPrice());
        product.setStockQuantity(createProductDto.getStockQuantity());
        product.setRating(createProductDto.getRating());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Cache individual product by ID
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) throws NotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if(optionalProduct.isEmpty())
        {
            throw new NotFoundException("Product not found with id: "+id);
        }

        if(optionalProduct.get().isDeleted())
        {
            throw new NotFoundException("Product with id: "+id+ " does not exist");
        }

        return optionalProduct.get();
    }

    /**
     * Updates existing product and updates cache automatically
     */
    @Caching(
            put = @CachePut(value = "products", key = "#id"),
            evict = @CacheEvict(value = "products", key = "#id", beforeInvocation = true)
    )
    public Product updateProduct(Long id, Map<String, Object> updates) throws NotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if(optionalProduct.isEmpty())
        {
            throw new NotFoundException("Product not found with id: "+id);
        }

        Product product = optionalProduct.get();
        if(product.isDeleted())
        {
            throw new NotFoundException("Product with id: "+id+ " does not exist");
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    product.setTitle((String) value);
                    break;
                case "description":
                    product.setDescription((String) value);
                    break;
                case "price":
                    product.setPrice((Double) value);
                    break;
                case "rating":
                    product.setRating((Double) value);
                    break;
                case "stockQuantity":
                    product.setStockQuantity((Integer) value);
                    break;
                case "category":
                    String mapCategory = ((String) value);
                    if(!product.getCategory().getName().equalsIgnoreCase(mapCategory))
                    {
                        Optional<Category> optionalCategory = categoryRepository.findByName(mapCategory);
                        if(optionalCategory.isPresent()){
                            product.setCategory(optionalCategory.get());
                        }
                        else {
                            Category addCategory = new Category();
                            addCategory.setName(mapCategory);
                            categoryRepository.save(addCategory);
                            product.setCategory(addCategory);
                        }
                    }
                    break;
            }
        });
        return productRepository.save(product);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) throws NotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty())
        {
            throw new NotFoundException("Product not found with id: "+id);
        }

        Product product = optionalProduct.get();
        if(product.isDeleted())
        {
            throw new NotFoundException("Product with id: "+id+ " does not exist");
        }
        product.setDeleted(true);
    }

    // Clear all product caches
    @CacheEvict(value = "products", allEntries = true)
    public void refreshProductCache() {
        // Method just for cache eviction
    }

}
