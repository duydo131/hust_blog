package com.learnspringboot.demo.controller;

import com.learnspringboot.demo.config.Contants;
import com.learnspringboot.demo.config.Role;
import com.learnspringboot.demo.dto.user.ChangePasswordInfoRequest;
import com.learnspringboot.demo.dto.user.UserInfoDTO;
import com.learnspringboot.demo.dto.mapper.UserMapper;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.security.UserPrincipal;
import com.learnspringboot.demo.service.db.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
        Sort sortable = Sort.by("id").ascending();
        if (sort.equals("DESC")) {
            sortable = Sort.by("id").descending();
        }
        Pageable pageable = PageRequest.of(page, size, sortable);
        return new ResponseEntity<>(new PageImpl<>(userService.allUser(pageable)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(
            HttpServletRequest request,
            @PathVariable("id") UUID id
        ) throws Exception {
        User user = ((UserPrincipal)request.getAttribute(Contants.PRINCIPAL)).getUser();
        return new ResponseEntity<>(userService.getUserDetail(id, user), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            HttpServletRequest request,
            @Validated @RequestBody ChangePasswordInfoRequest info
        ) throws Exception {
        User user = ((UserPrincipal)request.getAttribute(Contants.PRINCIPAL)).getUser();
        userService.changePassword(user, info);
        return new ResponseEntity<>(userMapper.userToUserInfoDTO(user), HttpStatus.OK);
    }
}
