package com.learnspringboot.demo.respository;

import com.learnspringboot.demo.entity.Slug;
import org.jetbrains.annotations.NotNull;
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

    @Query("select s from Slug s")
    @NotNull
    List<Slug> findAll();
}
