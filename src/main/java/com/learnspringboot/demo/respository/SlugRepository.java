package com.learnspringboot.demo.respository;

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


public interface SlugRepository extends JpaRepository<Slug, UUID> {
    @Query("SELECT s.title FROM Slug s")
    List<String> findAllTitle();

    Optional<Slug> findByTitle(String title);
    List<Slug> findByParent(Slug slug);
}
