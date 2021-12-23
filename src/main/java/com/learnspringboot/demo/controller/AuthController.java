package com.learnspringboot.demo.controller;

import com.learnspringboot.demo.dto.auth.SignupDTO;
import com.learnspringboot.demo.dto.auth.UserLoginDTO;
import com.learnspringboot.demo.dto.user.ChangePasswordInfoRequestDTO;
import com.learnspringboot.demo.service.db.AuthService;
import com.learnspringboot.demo.service.db.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Validated @RequestBody UserLoginDTO userLogin) {
        return ResponseEntity.ok(authService.authenticate(userLogin));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignupDTO signup) throws Exception {
        return new ResponseEntity<>(authService.registerUser(signup), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            HttpServletRequest request,
            @Validated @RequestBody ChangePasswordInfoRequestDTO info
    ) throws Exception {

        return new ResponseEntity<>(userService.changePassword(request, info), HttpStatus.OK);
    }
}