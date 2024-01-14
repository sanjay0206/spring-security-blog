package com.springboot.blog.services;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.responses.PostResponse;

public interface PostService {
    PostDto createPost(PostDto postDto, Long categoryId);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePost(PostDto postDto, long id);

    void deletePostById(long id);
}
