package ru.practicum.main.categories.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.service.CategoryService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getById(@PathVariable Long categoryId) {
        return categoryService.getById(categoryId);
    }
}
