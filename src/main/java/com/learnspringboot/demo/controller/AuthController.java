package com.learnspringboot.demo.controller;

import com.learnspringboot.demo.dto.auth.JwtResponse;
import com.learnspringboot.demo.dto.MessageResponse;
import com.learnspringboot.demo.dto.auth.SignupDTO;
import com.learnspringboot.demo.dto.auth.UserLoginDTO;
import com.learnspringboot.demo.dto.mapper.UserMapper;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.security.jwt.JwtUtil;
import com.learnspringboot.demo.security.UserPrincipal;
import com.learnspringboot.demo.service.db.RoleService;
import com.learnspringboot.demo.service.db.UserService;

import com.learnspringboot.demo.service.infra.ICreateUserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ICreateUserUseCase createUserUseCase;

    @GetMapping("")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Validated @RequestBody UserLoginDTO userLogin) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateJwtToken(authentication);

        UserPrincipal userDetail = (UserPrincipal)authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetail.getId(), userDetail.getUsername(), userDetail.getEmail()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignupDTO signup) throws Exception {
        User user = userMapper.SignupDTOToUser(signup);
        user.setActive(true);
        user.setRole(roleService.findByName("ROLE_USER").orElseThrow(() -> new Exception("Role user not define")));
        userService.save(user);
        createUserUseCase.additionalCreateUser(user);
        return ResponseEntity.ok(new MessageResponse("User Register successfully!"));
    }
}