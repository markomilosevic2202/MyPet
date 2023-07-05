package com.marko.mypet.controller;


import com.marko.mypet.dto.response.RequestPetDTO;
import com.marko.mypet.service.PetService;
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
@RequestMapping(path = "api/v1/pet")
public class PetController {
    private final PetService petService;

    @PostMapping()
    public ResponseEntity<?> createPet(@Valid @RequestBody RequestPetDTO petRequestDTO, BindingResult bindingResult, @AuthenticationPrincipal Jwt jwt) {
        return petService.create(petRequestDTO, bindingResult, jwt);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPet(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return petService.getPet(id, jwt);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePet(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return petService.deletePet(id, jwt);
    }

}
