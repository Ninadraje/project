package com.project.linkedIn.user_service.service;

import com.project.linkedIn.user_service.dto.LoginRequestDto;
import com.project.linkedIn.user_service.dto.SignupRequestDto;
import com.project.linkedIn.user_service.dto.UserDto;
import com.project.linkedIn.user_service.entity.User;
import com.project.linkedIn.user_service.exception.BadRequestException;
import com.project.linkedIn.user_service.exception.ResourceNotFoundException;
import com.project.linkedIn.user_service.repository.UserRepository;
import com.project.linkedIn.user_service.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signUp(SignupRequestDto signupRequestDto) {


        User user=modelMapper.map(signupRequestDto,User.class);
        user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));

        User userSaved=userRepository.save(user);
        return modelMapper.map(userSaved,UserDto.class);
    }

    public String login(LoginRequestDto loginRequestDto) {

        User user= userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(
                        ()-> new ResourceNotFoundException("User not found with email :"+loginRequestDto.getEmail())
                );

        boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(),user.getPassword());

        if(!isPasswordMatch){
            throw  new BadRequestException("Incorrect password");
        }

        return  jwtService.generateAccessToken(user);

    }
}
