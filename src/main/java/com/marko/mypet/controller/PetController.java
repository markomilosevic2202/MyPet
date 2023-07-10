package com.marko.mypet.controller;


import com.marko.mypet.dto.response.RequestAddVetPet;
import com.marko.mypet.dto.response.RequestPetDTO;
import com.marko.mypet.dto.response.RequestPetUpdateDTO;
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
    @PutMapping()
    public ResponseEntity<?> updatePet(@RequestBody RequestPetUpdateDTO requestPetUpdateDTO, @AuthenticationPrincipal Jwt jwt) {
        return petService.updatePet(requestPetUpdateDTO, jwt);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePet(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return petService.deletePet(id, jwt);
    }

    @GetMapping()
    public ResponseEntity<?> getPets(@AuthenticationPrincipal Jwt jwt) {
        return petService.getPets(jwt);
    }
    @PostMapping("/vet")
    public ResponseEntity<?> addVet(@Valid @RequestBody RequestAddVetPet requestAddVetPet, BindingResult bindingResult, @AuthenticationPrincipal Jwt jwt) {
        return petService.addVet(requestAddVetPet, bindingResult, jwt);
    }

    @DeleteMapping("/vet")
    public ResponseEntity<?> deleteVet(@Valid @RequestBody RequestAddVetPet requestAddVetPet, @AuthenticationPrincipal Jwt jwt) {
        return petService.deleteVet(requestAddVetPet, jwt);
    }

}
