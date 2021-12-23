package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.config.Contants;
import com.learnspringboot.demo.config.RoleEnum;
import com.learnspringboot.demo.dto.mapper.UserMapper;
import com.learnspringboot.demo.dto.user.ChangePasswordInfoRequestDTO;
import com.learnspringboot.demo.dto.user.UserInfoDTO;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.exception.custom_exception.MyException;
import com.learnspringboot.demo.exception.custom_exception.NotAUserException;
import com.learnspringboot.demo.respository.UserRepository;
import com.learnspringboot.demo.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public User update(User user) {
        User oldUser = findByUsername(user.getUsername()).orElse(null);
        if(oldUser == null) return null;
        user.setPassword(oldUser.getPassword());
        return userRepository.save(user);
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    public Page<User> findUser(Pageable pageable){
        return userRepository.findUser(pageable);
    }

    public UserInfoDTO changePassword(
            HttpServletRequest request,
            ChangePasswordInfoRequestDTO info) throws Exception {

        User user = ((UserPrincipal)request.getAttribute(Contants.PRINCIPAL)).getUser();
        if(user == null) throw new Exception("Server Error");

        if(!passwordEncoder.matches(info.getOldPassword(), user.getPassword()))
            throw new Exception("Password incorrect.");

        if(!info.checkRepeatPassword())
            throw new Exception("Password and repeat password is invalid.");

        user.setRawPassword(null);
        user.setPassword(info.getNewPassword1());
        save(user);

        return userMapper.userToUserInfoDTO(user);
    }

    public Object getUserDetail(HttpServletRequest request, UUID id) throws Exception {
        User userRequest = ((UserPrincipal)request.getAttribute(Contants.PRINCIPAL)).getUser();
        if(userRequest == null) throw new NotAUserException();

        User user = findById(id).orElseThrow(
            () -> new MyException(String.format("User id %s not found", id.toString()))
        );
        if(userRequest.getRole().getName().equals(RoleEnum.ROLE_ADMIN.getName()))
            return userMapper.userToUserSignupInfo(user);
        else {
            if(!userRequest.getId().equals(id))
                throw new Exception(
                        String.format("You not seen details user id %s", id.toString())
                );
            return userMapper.userToUserInfoDTO(user);
        }
    }

    public List<UserInfoDTO> allUser(HttpServletRequest request, Pageable pageable) throws Exception {
        User userRequest = ((UserPrincipal)request.getAttribute(Contants.PRINCIPAL)).getUser();
        if(userRequest == null) return new ArrayList<>();

        if(userRequest.getRole().getName().equals(RoleEnum.ROLE_ADMIN.getName())){
            Page<User> users = findUser(pageable);
            return users.stream().map(userMapper::userToUserInfoDTO).collect(Collectors.toList());
        }
        return new ArrayList<>(Arrays.asList(userMapper.userToUserInfoDTO(userRequest)));
    }
}
