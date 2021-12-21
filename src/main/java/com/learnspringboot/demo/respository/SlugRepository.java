package com.learnspringboot.demo.respository;

import com.learnspringboot.demo.entity.Post;
import com.learnspringboot.demo.entity.Slug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface SlugRepository extends JpaRepository<Slug, UUID> {

    Optional<Slug> findByTitle(String title);
}
