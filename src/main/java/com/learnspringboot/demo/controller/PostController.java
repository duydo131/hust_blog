package com.learnspringboot.demo.controller;

import com.learnspringboot.demo.dto.mapper.PostMapper;
import com.learnspringboot.demo.dto.post.PostDTO;
import com.learnspringboot.demo.dto.user.UserInfoDTO;
import com.learnspringboot.demo.entity.Post;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.service.db.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @GetMapping("/{slug}")
    public ResponseEntity<Page<?>> listPost(@PathVariable("slug") String slug,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) throws Exception {

        Sort sortable = Sort.by("id").ascending();

        if (sort.equals("DESC")) {
            sortable = Sort.by("id").descending();
        }
        Pageable pageable = PageRequest.of(page, size, sortable);

        Page<Post> posts = postService.findPostBySlug(pageable, slug);

        List<PostDTO> postResponse = posts.stream().map(postMapper::mapToPostDTO).collect(Collectors.toList());
        return new ResponseEntity<>(new PageImpl<>(postResponse), HttpStatus.OK);
    }
}
