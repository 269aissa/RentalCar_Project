package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updated) {
        Category cat = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
        cat.setNom(updated.getNom());
        cat.setDescription(updated.getDescription());
        return categoryRepository.save(cat);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
