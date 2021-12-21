package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.dto.mapper.UserMapper;
import com.learnspringboot.demo.dto.user.ChangePasswordInfoRequest;
import com.learnspringboot.demo.dto.user.UserInfoDTO;
import com.learnspringboot.demo.entity.Role;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    public void changePassword(User user, ChangePasswordInfoRequest info) throws Exception {
        if(!passwordEncoder.matches(info.getOldPassword(), user.getPassword()))
            throw new Exception("Password is incorrect.");

        if(!info.checkRepeatPassword())
            throw new Exception("Password and repeat password is not valid.");

        user.setRawPassword(null);
        user.setPassword(info.getNewPassword1());
        save(user);
    }

    public Object getUserDetail(UUID id, User userRequest) throws Exception {
        User user = findById(id).orElseThrow(
            () -> new Exception(String.format("User id %s not found", id.toString()))
        );
        if(userRequest.getRole().getName().equals(com.learnspringboot.demo.config.Role.ROLE_ADMIN.getName()))
            return userMapper.userToUserSignupInfo(user);
        else {
            if(!userRequest.getId().equals(id))
                throw new Exception(
                        String.format("You not seen details user id %s", id.toString())
                );
            return userMapper.userToUserInfoDTO(user);
        }
    }

    public List<UserInfoDTO> allUser(Pageable pageable){
        Page<User> users = findUser(pageable);
        return users.stream().map(userMapper::userToUserInfoDTO).collect(Collectors.toList());
    }
}
