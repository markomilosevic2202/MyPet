package com.marko.mypet.controller;


import com.marko.mypet.dto.response.RequestVetDTO;
import com.marko.mypet.service.VetService;
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
@RequestMapping(path = "api/v1/vet")
public class VetController {
    private final VetService vetService;

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody RequestVetDTO requestVetDTO, BindingResult bindingResult, @AuthenticationPrincipal Jwt jwt) {
        return vetService.createVet(requestVetDTO, bindingResult, jwt);
    }
    @GetMapping
    public ResponseEntity<?> getAllVet(@AuthenticationPrincipal Jwt jwt) {
        return vetService.getAllVet( jwt);
    }

    @GetMapping("{specialty}")
    public ResponseEntity<?> getVet(@PathVariable String specialty, @AuthenticationPrincipal Jwt jwt) {
        return vetService.getVetSpecialty(specialty, jwt);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> create(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return vetService.deleteVet(id, jwt);
    }
}
