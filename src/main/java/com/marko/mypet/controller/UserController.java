package com.marko.mypet.controller;

import com.marko.mypet.dto.response.RequestUserDTO;
import com.marko.mypet.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/ping")
    public ResponseEntity<?> ping(Principal principal, HttpServletRequest request){
        log.info(request.getHeader(""));
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> register(@Valid @RequestBody RequestUserDTO requestUserDTO, BindingResult bindingResult, @AuthenticationPrincipal Jwt jwt) {
        return userService.registerUser(requestUserDTO, bindingResult, jwt);
    }

    @GetMapping()
    public ResponseEntity<?> getUser(@AuthenticationPrincipal Jwt jwt) {
        return userService.getUser( jwt);
    }
    @GetMapping()
    public ResponseEntity<?> putUser(@Valid @RequestBody RequestUserDTO requestUserDTO, BindingResult bindingResult, @AuthenticationPrincipal Jwt jwt) {
        return userService.putUser(requestUserDTO, bindingResult, jwt);
    }

}
