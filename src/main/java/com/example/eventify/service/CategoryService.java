package com.example.eventify.service;

import com.example.eventify.model.Category;
import com.example.eventify.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Set<Category> findAllById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        Set<Category> categories = new HashSet<>();
        categoryRepository.findAllById(ids).forEach(categories::add);
        return categories;
    }
}
