package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.config.Role;
import com.learnspringboot.demo.dto.MessageResponse;
import com.learnspringboot.demo.dto.auth.JwtResponse;
import com.learnspringboot.demo.dto.auth.SignupDTO;
import com.learnspringboot.demo.dto.auth.UserLoginDTO;
import com.learnspringboot.demo.dto.mapper.UserMapper;
import com.learnspringboot.demo.dto.user.UserSignupInfo;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.security.UserPrincipal;
import com.learnspringboot.demo.security.jwt.JwtUtil;
import com.learnspringboot.demo.service.infra.ICreateUserUseCase;
import com.learnspringboot.demo.service.infra.impl.NewCreateUserUseCase;
import com.learnspringboot.demo.service.infra.impl.NoActionCreateUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private JwtUtil jwtUtil;

    public JwtResponse authenticate(UserLoginDTO userLogin) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateJwtToken(authentication);

        UserPrincipal userDetail = (UserPrincipal)authentication.getPrincipal();

        return new JwtResponse(jwt, userDetail.getId(), userDetail.getUsername(), userDetail.getEmail());
    }

    @Transactional
    public UserSignupInfo registerUser(SignupDTO signup) throws Exception {
        User user = userMapper.SignupDTOToUser(signup);
        user.setActive(true);
        user.setRawPassword(signup.getPassword());
        user.setRole(roleService.findByName(Role.ROLE_USER.getName()).orElseThrow(() -> new Exception("Role user not define")));
        userService.save(user);
        ICreateUserUseCase createUserUseCase = getCreateUseCase(signup.getSlug());
        createUserUseCase.additionalCreateUser(user);
        return userMapper.userToUserSignupInfo(user);
    }

    private ICreateUserUseCase getCreateUseCase(String slug){
        switch (slug){
            case "tin-tuc":
                return new NewCreateUserUseCase(permissionService);
            default:
                return new NoActionCreateUseCase();
        }
    }
}
