package com.marko.mypet.service;


import com.marko.mypet.dto.response.RequestPetDTO;
import com.marko.mypet.dto.response.RequestSpecialtyDTO;
import com.marko.mypet.dto.response.RequestUserDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final UserRepository userRepository;
    private final VetRepository vetRepository;

    public ResponseEntity<?> createSpecialty(RequestSpecialtyDTO requestSpecialtyDTO, BindingResult bindingResult, Jwt jwt) {


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
            if (!optionalUser.get().isAdmin()) {
                responseDTO.addError("Creating specialties is allowed only by admin");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            Specialty specialty = new Specialty();

            if (requestSpecialtyDTO.getNameSpecialty() != null) {
                specialty.setName(requestSpecialtyDTO.getNameSpecialty());
            }

            specialty = specialtyRepository.save(specialty);
            responseDTO.setPayload(specialty);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<?> getAllSpecialty(Jwt jwt) {

        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
            if (optionalUser.isEmpty()) {
                responseDTO.addError("User not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            List<Specialty> specialties = specialtyRepository.findAll();
            if (specialties.isEmpty()) {
                responseDTO.setPayload(new ArrayList<Vet>());
                responseDTO.addError("Specialty types not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            responseDTO.setPayload(specialties);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteSpecialty(String id, Jwt jwt) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
            if (optionalUser.isEmpty()) {
                responseDTO.addError("User not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            List<Vet> vetsWithSpecialty = vetRepository.findBySpecialtyId(id);
            for (Vet vet : vetsWithSpecialty) {
                vet.setSpecialty(null);
                vetRepository.save(vet);
            }

            specialtyRepository.deleteById(id);
            specialtyRepository.flush();
            responseDTO.addInfo("Pet deleted");

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}






