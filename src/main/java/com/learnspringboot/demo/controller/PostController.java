package com.learnspringboot.demo.controller;

import com.learnspringboot.demo.dto.post.PostRequestDTO;
import com.learnspringboot.demo.dto.post.PostUpdateRequestDTO;
import com.learnspringboot.demo.service.db.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/all/{slug}")
    public ResponseEntity<?> listPost(
            HttpServletRequest request,
            @PathVariable("slug") String slug,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) throws Exception {

        Sort sortable = Sort.by("id").ascending();
        if (sort.equals("DESC")) sortable = Sort.by("id").descending();

        Pageable pageable = PageRequest.of(page, size, sortable);
        return new ResponseEntity<>(postService.findPostBySlug(request, pageable, slug), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable("id") UUID id) throws Exception {
        return new ResponseEntity<>(postService.findPostById(id), HttpStatus.OK);
    }

    @PostMapping("/{slug}")
    public ResponseEntity<?> additionalPost(
            HttpServletRequest request,
            @PathVariable("slug") String slug,
            @Validated @RequestBody PostRequestDTO postRequestDTO) throws Exception {
        return new ResponseEntity<>(postService.save(request, postRequestDTO, slug), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePost(
            HttpServletRequest request,
            @PathVariable UUID id,
            @RequestBody PostUpdateRequestDTO postUpdateRequestDTO) throws Exception {
        return new ResponseEntity<>(postService.updatePost(request, id, postUpdateRequestDTO), HttpStatus.OK);
    }

    @GetMapping("/all/my")
    public ResponseEntity<?> myPost(
            HttpServletRequest request,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) throws Exception {

        Sort sortable = Sort.by("id").ascending();
        if (sort.equals("DESC")) sortable = Sort.by("id").descending();

        Pageable pageable = PageRequest.of(page, size, sortable);
        return new ResponseEntity<>(postService.getMyPost(request, pageable), HttpStatus.OK);
    }
}
