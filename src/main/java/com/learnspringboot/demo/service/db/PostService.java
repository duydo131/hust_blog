package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.entity.Post;
import com.learnspringboot.demo.entity.Slug;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.respository.PostRepository;
import com.learnspringboot.demo.respository.SlugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SlugRepository slugRepository;

    public Page<Post> findPostBySlug(Pageable pageable, String title) throws Exception {
        Slug s = slugRepository.findByTitle(title).orElseThrow(() -> new Exception("Slug not define"));
        return postRepository.findPostBySlug(s, pageable);
    }
}
