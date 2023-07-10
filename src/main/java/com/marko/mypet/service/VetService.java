package com.marko.mypet.service;

import com.marko.mypet.dto.response.RequestPetDTO;
import com.marko.mypet.dto.response.RequestVetDTO;
import com.marko.mypet.dto.response.ResponseDTO;
import com.marko.mypet.entity.Pet;
import com.marko.mypet.entity.Specialty;
import com.marko.mypet.entity.User;
import com.marko.mypet.entity.Vet;
import com.marko.mypet.repository.PetRepository;
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
public class VetService {


    private final SpecialtyRepository specialtyRepository;
    private final UserRepository userRepository;
    private final VetRepository vetRepository;
    private final PetRepository petRepository;

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
            Vet vet = createVetFromVetDTO(requestVetDTO, new Vet());

            Optional<Specialty> optionalSpecialty = specialtyRepository.findById(requestVetDTO.getIdSpecialty());
            if (optionalSpecialty.isEmpty()) {
                responseDTO.addError("Specialty not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            vet.setSpecialty(optionalSpecialty.get());
            vet = vetRepository.save(vet);

            responseDTO.setPayload(vet);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    public ResponseEntity<?> getAllVet(Jwt jwt) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
            if (optionalUser.isEmpty()) {
                responseDTO.addError("User not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            List<Vet> vets = vetRepository.findAll();
            if (vets.isEmpty()) {
                responseDTO.setPayload(new ArrayList<Vet>());
                responseDTO.addError("Pet types not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            responseDTO.setPayload(vets);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getVetSpecialty(String specialty, Jwt jwt) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
            if (optionalUser.isEmpty()) {
                responseDTO.addError("User not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            List<Vet> vets = vetRepository.findBySpecialtyName(specialty);
            if (vets.isEmpty()) {
                responseDTO.setPayload(new ArrayList<Vet>());
                responseDTO.addError("Pet types not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            responseDTO.setPayload(vets);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> deleteVet(String id, Jwt jwt) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
            if (optionalUser.isEmpty()) {
                responseDTO.addError("User not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            Optional<Vet> optionalVet = vetRepository.findById(id);
            if (optionalVet.isEmpty()) {
                responseDTO.addError("Vet not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            if (!optionalUser.get().isAdmin()) {
                responseDTO.addError("Delete vet is allowed only by admin");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }


            vetRepository.deleteById(id);
            vetRepository.flush();
            responseDTO.addInfo("Vet deleted");
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

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


        return vet;
    }



}