package com.marko.mypet.controller;


import com.marko.mypet.dto.response.RequestSpecialtyDTO;
import com.marko.mypet.dto.response.RequestUserDTO;
import com.marko.mypet.entity.Specialty;
import com.marko.mypet.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/speciality")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @PostMapping()
    public ResponseEntity<?> createSpecialty(@Valid @RequestBody RequestSpecialtyDTO requestUserDTO, BindingResult bindingResult, @AuthenticationPrincipal Jwt jwt) {
        return specialtyService.createSpecialty(requestUserDTO, bindingResult, jwt);
    }

    @GetMapping()
    public ResponseEntity<?> getAllSpecialty(@AuthenticationPrincipal Jwt jwt) {
        return specialtyService.getAllSpecialty( jwt);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> putUser( @PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return specialtyService.deleteSpecialty(id, jwt);
    }
}
