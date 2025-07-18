package com.skillcert.backend.service;

import com.skillcert.backend.dto.*;
import com.skillcert.backend.entity.*;
import com.skillcert.backend.exception.ResourceNotFoundException;
import com.skillcert.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EvaluationService evaluationService;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findByOrderByOrderIndexAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToResponse(category);
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }

        Category prerequisite = null;
        if (request.getPrerequisiteCategoryId() != null) {
            prerequisite = categoryRepository.findById(request.getPrerequisiteCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prerequisite category not found"));
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .orderIndex(request.getOrder())  // Cambiado de order a orderIndex
                .color(request.getColor())
                .icon(request.getIcon())
                .prerequisiteCategory(prerequisite)
                .status(CategoryStatus.ACTIVE)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getName().equals(request.getName()) && 
            categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }

        Category prerequisite = null;
        if (request.getPrerequisiteCategoryId() != null) {
            prerequisite = categoryRepository.findById(request.getPrerequisiteCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prerequisite category not found"));
            
            // Prevenir dependencias circulares
            if (prerequisite.getId().equals(id)) {
                throw new IllegalArgumentException("Category cannot be prerequisite of itself");
            }
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setOrderIndex(request.getOrder());  // Cambiado de setOrder a setOrderIndex
        category.setColor(request.getColor());
        category.setIcon(request.getIcon());
        category.setPrerequisiteCategory(prerequisite);

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponse(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // Verificar si la categoría tiene categorías dependientes
        List<Category> dependentCategories = categoryRepository.findDependentCategories(id);
        if (!dependentCategories.isEmpty()) {
            throw new IllegalStateException("Cannot delete category with dependent categories");
        }

        // Verificar si la categoría tiene evaluaciones asociadas
        if (category.getEvaluations() != null && !category.getEvaluations().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated evaluations");
        }

        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public CategoryWithEvaluationsResponse getCategoryEvaluations(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        List<EvaluationResponse> evaluations = evaluationService.getByCategoryId(id);

        return CategoryWithEvaluationsResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .evaluations(evaluations)
                .build();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findByStatusOrderByOrderIndexAsc(CategoryStatus.ACTIVE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .order(category.getOrderIndex())  // Mapeado de orderIndex a order
                .color(category.getColor())
                .icon(category.getIcon())
                .prerequisiteCategoryId(category.getPrerequisiteCategory() != null ? 
                    category.getPrerequisiteCategory().getId() : null)
                .prerequisiteCategoryName(category.getPrerequisiteCategory() != null ? 
                    category.getPrerequisiteCategory().getName() : null)
                .status(category.getStatus().name())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}