package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.config.RoleEnum;
import com.learnspringboot.demo.dto.auth.JwtResponse;
import com.learnspringboot.demo.dto.auth.SignupDTO;
import com.learnspringboot.demo.dto.auth.UserLoginDTO;
import com.learnspringboot.demo.dto.mapper.UserMapper;
import com.learnspringboot.demo.dto.user.UserSignupInfoDTO;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.exception.custom_exception.MyException;
import com.learnspringboot.demo.security.UserPrincipal;
import com.learnspringboot.demo.security.jwt.JwtUtil;
import com.learnspringboot.demo.service.infra.ICreateUserUseCase;
import com.learnspringboot.demo.service.infra.impl.NewCreateUserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    private SlugService slugService;

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
    public UserSignupInfoDTO registerUser(SignupDTO signup) throws Exception {
        User user = userMapper.SignupDTOToUser(signup);
        user.setActive(true);
        user.setRawPassword(signup.getPassword());
        user.setRole(roleService.findByName(RoleEnum.ROLE_USER.getName()).orElseThrow(
                () -> new MyException("Role user not define"))
        );
        userService.save(user);
        ICreateUserUseCase createUserUseCase = getCreateUseCase(signup.getSlug());
        createUserUseCase.additionalCreateUser(user, signup.getSlug());
        return userMapper.userToUserSignupInfo(user);
    }

    private ICreateUserUseCase getCreateUseCase(String slug) throws Exception {
        List<String> slugs = slugService.findAllSlugTitle();
        if (slugs.contains(slug)) return new NewCreateUserUseCase(permissionService, slugService);
        throw new MyException(String.format("slug %s not define.", slug));
    }
}
