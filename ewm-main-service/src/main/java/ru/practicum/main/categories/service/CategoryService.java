package ru.practicum.main.categories.service;

import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto getById(Long id);

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto createCategory(NewCategoryDto categoryDto);

    void deleteCategoryById(Long id);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
}