package com.learnspringboot.demo.controller;

import com.learnspringboot.demo.dto.slug.SlugAdditionalRequestDTO;
import com.learnspringboot.demo.service.db.SlugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/slugs")
public class SlugController {

    @Autowired
    private SlugService slugService;

    @GetMapping("")
    public ResponseEntity<Collection<?>> getSlugs(
            @RequestParam(name = "slug", required = true) String slug
        ) throws Exception {
        return new ResponseEntity<>(slugService.getParentSlug(slug), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Collection<?>> additionalSlugs(
            @Validated  @RequestBody SlugAdditionalRequestDTO slugAdditional) throws Exception {
        return new ResponseEntity<>(slugService.save(slugAdditional), HttpStatus.OK);
    }
}
