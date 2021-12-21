package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.dto.slug.SlugAdditionalRequest;
import com.learnspringboot.demo.entity.Slug;
import com.learnspringboot.demo.respository.SlugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SlugService {

    @Autowired
    private SlugRepository slugRepository;

    public Collection<Slug> getParentSlug(String title) throws Exception {
        Slug slug = slugRepository.findByTitle(title).orElseThrow(
                () -> new Exception(String.format("slug title %s not define.", title))
        );
        return getAllParent(slug);
    }

    public Collection<Slug> save(SlugAdditionalRequest slugAdditional) throws Exception {
        Slug slugParent = slugRepository.findById(slugAdditional.getParentID()).orElseThrow(
                () -> new Exception(String.format("Slug parent ID %s is not found", slugAdditional.getParentID().toString()))
        );

        Slug slug = new Slug(
                slugAdditional.getTitle(),
                slugAdditional.getName(),
                slugParent
        );
        slugRepository.save(slug);
        return getAllParent(slug);
    }

    private Collection<Slug> getAllParent(Slug slug){
        List<Slug> slugParent = new ArrayList<>();
        while(slug != null){
            slugParent.add(slug);
            slug = slug.getParent();
        }
        return slugParent;
    }
}
