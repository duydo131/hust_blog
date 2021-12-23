package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.dto.mapper.SlugMapper;
import com.learnspringboot.demo.dto.slug.SlugAdditionalRequestDTO;
import com.learnspringboot.demo.entity.Slug;
import com.learnspringboot.demo.exception.custom_exception.MyException;
import com.learnspringboot.demo.respository.SlugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SlugService {

    @Autowired
    private SlugRepository slugRepository;

    @Autowired
    private SlugMapper slugMapper;

    public void save(Slug slug) {slugRepository.save(slug);}

    public List<Slug> findByParent(Slug slug){
        return slugRepository.findByParent(slug);
    }


    public List<String> findAllSlugTitle(){
        return slugRepository.findAllTitle();
    }

    public Collection<Object> getParentSlug(String title) throws Exception {
        Slug slug = slugRepository.findByTitle(title).orElseThrow(
                () -> new MyException(String.format("slug title %s not define.", title))
        );
        return getAllParent(slug).stream().map(slugMapper::mapToSlugResponseDTO).collect(Collectors.toList());
    }

    public Collection<Object> save(SlugAdditionalRequestDTO slugAdditional) throws Exception {
        Slug slugParent = slugRepository.findById(slugAdditional.getParentID()).orElseThrow(
                () -> new MyException(String.format("Slug parent ID %s is not found", slugAdditional.getParentID().toString()))
        );

        Slug slug = new Slug(
                slugAdditional.getTitle(),
                slugAdditional.getName(),
                slugParent
        );
        slugRepository.save(slug);
        return getAllParent(slug).stream().map(slugMapper::mapToSlugResponseDTO).collect(Collectors.toList());
    }

    public Collection<Slug> getAllParent(Slug slug){
        List<Slug> slugParent = new ArrayList<>();
        while(slug != null){
            slugParent.add(slug);
            slug = slug.getParent();
        }
        return slugParent;
    }

    public List<Slug> getAllChild(Slug slug){
        List<Slug> slugChild = new ArrayList<>();
        Queue<Slug> queueSlug = new LinkedList<>();
        queueSlug.add(slug);

        while((slug = queueSlug.poll())!= null){
            slugChild.add(slug);
            queueSlug.addAll(findByParent(slug));
        }
        return slugChild;
    }

    public List<Slug> getAllChild(String slugStr) throws Exception {
        Slug slug = findByTitle(slugStr);
        return getAllChild(slug);
    }

    public Slug findByTitle(String title) throws Exception {
        return slugRepository.findByTitle(title).orElseThrow(() -> new MyException("Slug not define"));
    }
}
