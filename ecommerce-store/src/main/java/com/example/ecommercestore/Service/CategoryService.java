package com.example.ecommercestore.Service;

import com.example.ecommercestore.Model.Category;
import com.example.ecommercestore.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        if (categoryRepository.count() == 0)
            return null;

        return categoryRepository.findAll();
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public Boolean updateCategory(Integer id, Category category) {
        Category oldCategory = categoryRepository.findById(id).get();
        if (oldCategory == null)
            return false;

        oldCategory.setName(category.getName());
        categoryRepository.save(oldCategory);
        return true;
    }

    public Boolean deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id).get();
        if (category == null)
            return false;

        categoryRepository.delete(category);
        return true;
    }
}
