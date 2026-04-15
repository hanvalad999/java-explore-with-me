package ru.practicum.main.categories.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.NewCategoryDto;
import ru.practicum.main.categories.model.Category;

@UtilityClass
public class CategoryMapper {
    public Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}