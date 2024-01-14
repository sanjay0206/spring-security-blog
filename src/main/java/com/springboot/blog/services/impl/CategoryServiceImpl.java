package com.springboot.blog.services.impl;

import com.springboot.blog.dto.CategoryDto;
import com.springboot.blog.entities.Category;
import com.springboot.blog.exceptions.BlogAPIException;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.repositories.CategoryRepository;
import com.springboot.blog.repositories.PostRepository;
import com.springboot.blog.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Optional<Category> existingCategory = categoryRepository.findByCategoryName(categoryDto.getCategoryName());

        // Check if category name already exists
        if (existingCategory.isPresent()) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Category with name " + categoryDto.getCategoryName() + " already exists");
        }

        Category savedCategory = categoryRepository.save(category);

        return mapToDTO(savedCategory);
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        return mapToDTO(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map((category) -> mapToDTO(category))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        category.setCategoryName(categoryDto.getCategoryName());
        category.setDescription(categoryDto.getDescription());
        category.setId(categoryId);

      /*  List<Post> posts = new ArrayList<>();
        for (PostDto postDto : categoryDto.getPosts()) {
           Post post = modelMapper.map(postDto, Post.class);
           posts.add(post);
        }
        category.setPosts(posts);*/
        Category updatedCategory = categoryRepository.save(category);

        return mapToDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        categoryRepository.deleteById(categoryId);
    }

    // convert Entity into DTO
    private CategoryDto mapToDTO(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    // convert DTO to entity
    private Category mapToEntity(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

}
