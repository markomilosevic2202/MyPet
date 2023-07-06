package com.marko.mypet.service;

import com.marko.mypet.dto.response.RequestPetDTO;
import com.marko.mypet.dto.response.RequestVetDTO;
import com.marko.mypet.dto.response.ResponseDTO;
import com.marko.mypet.entity.Pet;
import com.marko.mypet.entity.Specialty;
import com.marko.mypet.entity.User;
import com.marko.mypet.entity.Vet;
import com.marko.mypet.repository.SpecialtyRepository;
import com.marko.mypet.repository.UserRepository;
import com.marko.mypet.repository.VetRepository;
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
public class VetService {


    private final SpecialtyRepository specialtyRepository;
    private final UserRepository userRepository;
    private final VetRepository vetRepository;

    public ResponseEntity<?> createVet(RequestVetDTO requestVetDTO, BindingResult bindingResult, Jwt jwt) {
        ResponseDTO responseDTO = new ResponseDTO();

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                responseDTO.addError(objectError.getDefaultMessage());
            });
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
            if (optionalUser.isEmpty()) {
                responseDTO.addError("User not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            Optional<Specialty> optionalSpecialty = specialtyRepository.findById(requestVetDTO.getIdSpecialty());
            if (optionalSpecialty.isEmpty()) {
                responseDTO.addError("Specialty not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            Vet vet = createVetFromVetDTO(requestVetDTO, new Vet());
            responseDTO.setPayload(vet);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private Vet createVetFromVetDTO(RequestVetDTO requestVetDTO, Vet vet) {
        if (requestVetDTO.getFirstName() != null && !requestVetDTO.getFirstName().equals(vet.getFirstName())) {
            vet.setFirstName(requestVetDTO.getFirstName());
        }
        if (requestVetDTO.getLastName() != null && !requestVetDTO.getLastName().equals(vet.getLastName())) {
            vet.setLastName(requestVetDTO.getLastName());
        }

        if (requestVetDTO.getIdSpecialty() != null && !requestVetDTO.getIdSpecialty().equals(vet.getSpecialty().getName())) {
            vet.setLastName(requestVetDTO.getLastName());
        }


        return vet;
    }
}