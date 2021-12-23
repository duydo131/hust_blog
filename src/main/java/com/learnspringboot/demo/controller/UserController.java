package com.learnspringboot.demo.controller;

import com.learnspringboot.demo.dto.mapper.UserMapper;
import com.learnspringboot.demo.dto.user.ChangePasswordInfoRequestDTO;
import com.learnspringboot.demo.service.db.UserService;
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
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("")
    public ResponseEntity<Page<?>> listUser(
            HttpServletRequest request,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) throws Exception {

        Sort sortable = Sort.by("id").ascending();
        if (sort.equals("DESC")) {
            sortable = Sort.by("id").descending();
        }
        Pageable pageable = PageRequest.of(page, size, sortable);
        return new ResponseEntity<>(new PageImpl<>(userService.allUser(request, pageable)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(
            HttpServletRequest request,
            @PathVariable("id") UUID id
        ) throws Exception {

        return new ResponseEntity<>(userService.getUserDetail(request, id), HttpStatus.OK);
    }
}
