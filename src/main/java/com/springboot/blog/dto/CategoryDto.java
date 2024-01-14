package com.springboot.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotEmpty
    @Size(min = 2, message = "Category name should have at least 2 characters")
    private String categoryName;

    @NotEmpty
    @Size(min = 10, message = "Category description should have at least 10 characters")
    private String description;
}
