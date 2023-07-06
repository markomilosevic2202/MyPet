package com.marko.mypet.controller;

import com.marko.mypet.dto.response.RequestUserDTO;
import com.marko.mypet.service.VetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/vet")
public class VetController {
    private final VetService vetService;

    @PostMapping()
    public ResponseEntity<?> register(@Valid @RequestBody RequestUserDTO requestUserDTO, BindingResult bindingResult, @AuthenticationPrincipal Jwt jwt) {
        return vetService.createVet(requestUserDTO, bindingResult, jwt);
    }
}