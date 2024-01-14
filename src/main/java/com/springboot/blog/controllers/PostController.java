package com.springboot.blog.controllers;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostDtoV2;
import com.springboot.blog.responses.PostResponse;
import com.springboot.blog.services.PostService;
import com.springboot.blog.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("hasAuthority('SCOPE_post:create')")
    // create blog post rest api
    @PostMapping("/api/v1/posts/{categoryId}/category")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto,
                                              @PathVariable(name = "categoryId") Long categoryId ) {
        return new ResponseEntity<>(postService.createPost(postDto, categoryId), HttpStatus.CREATED);
    }


    // get all posts rest api
    @PreAuthorize("hasAuthority('SCOPE_post:read')")
    @GetMapping("/api/v1/posts")
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    // get post by id
    @PreAuthorize("hasAuthority('SCOPE_post:read')")
    @GetMapping(value = "/api/posts/{id}", params = "version=1")
    public ResponseEntity<PostDto> getPostByIdV1Param(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // get post by id
    @PreAuthorize("hasAuthority('SCOPE_post:read')")
    @GetMapping(value = "/api/posts/{id}", headers = "X-API-VERSION=1")
    public ResponseEntity<PostDto> getPostByIdV1Header(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // get post by id
    @PreAuthorize("hasAuthority('SCOPE_post:read')")
    @GetMapping(value = "/api/posts/{id}", produces = "application/vnd.myblog.v1+json")
    public ResponseEntity<PostDto> getPostByIdV1ContentNegotiation(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // get post by id
    @PreAuthorize("hasAuthority('SCOPE_post:read')")
    @GetMapping("/api/v2/posts/{id}")
    public ResponseEntity<PostDtoV2> getPostByIdURI(@PathVariable(name = "id") long id) {
        PostDto postDto = postService.getPostById(id);
        PostDtoV2 postDtoV2 = new PostDtoV2();
        postDtoV2.setId(postDto.getId());
        postDtoV2.setTitle(postDto.getTitle());
        postDtoV2.setDescription(postDto.getDescription());
        postDtoV2.setContent(postDto.getContent());
        postDtoV2.setCategory(postDto.getCategory());
        List<String> tags = new ArrayList<>();
        tags.add("Food");
        tags.add("Pizza");
        tags.add("Italian Cuisine");
        postDtoV2.setTags(tags);
        return ResponseEntity.ok(postDtoV2);
    }

    // update post by id rest api
    @PreAuthorize("hasAuthority('SCOPE_post:update')")
    @PutMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id) {

        PostDto postResponse = postService.updatePost(postDto, id);

        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    // delete post rest api
    @PreAuthorize("hasAuthority('SCOPE_post:delete')")
    @DeleteMapping("/api/v1/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id) {

        postService.deletePostById(id);

        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }
}
