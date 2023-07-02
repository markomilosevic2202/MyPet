package com.marko.mypet.service;

import com.marko.mypet.dto.response.RequestUserDTO;
import com.marko.mypet.dto.response.ResponseUserDTO;
import com.marko.mypet.entity.User;
import com.marko.mypet.repository.UserRepository;
import com.marko.mypet.tool.JwtTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<?> register(RequestUserDTO requestUserDTO, BindingResult bindingResult, Jwt jwt) {
        ResponseUserDTO responseUserDTO = new ResponseUserDTO();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                responseUserDTO.addError(objectError.getDefaultMessage());
            });
            return new ResponseEntity<>(responseUserDTO, HttpStatus.BAD_REQUEST);
        }
        try {
            // TODO: Check if user exist, enabled and not locked if true, throw error
            if (userRepository.existsByEmail(JwtTools.getEmailFromOAuthToken(jwt))) {
                Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
                responseUserDTO.setPayload(optionalUser.get());
                return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
            }
            User user = new User();
            user.setEnabled(true);

            user.setFirstName(requestUserDTO.getFirstName());
            user.setLastName(requestUserDTO.getLastName());

            user.setEmail(JwtTools.getEmailFromOAuthToken(jwt));
            user.setEnabled(false);

            user = userRepository.save(user);
            responseUserDTO.setPayload(user);
            return new ResponseEntity<>(responseUserDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            responseUserDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseUserDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
