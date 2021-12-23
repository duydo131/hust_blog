package com.learnspringboot.demo.respository;

import com.learnspringboot.demo.entity.Permission;
import com.learnspringboot.demo.entity.Post;
import com.learnspringboot.demo.entity.Slug;
import com.learnspringboot.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface PostRepository extends JpaRepository<Post, UUID> {

    Page<Post> findPostBySlug(Slug slug, Pageable pageable);
    Page<Post> findPostBySlugAndPublished(Slug slug, Boolean published, Pageable pageable);
}
