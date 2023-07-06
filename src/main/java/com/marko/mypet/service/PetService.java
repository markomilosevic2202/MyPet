package com.marko.mypet.service;

import com.marko.mypet.dto.response.RequestPetDTO;
import com.marko.mypet.dto.response.ResponseDTO;
import com.marko.mypet.entity.Pet;
import com.marko.mypet.entity.User;
import com.marko.mypet.repository.PetRepository;
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
public class PetService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public ResponseEntity<?> create(RequestPetDTO requestPetDTO, BindingResult bindingResult, Jwt jwt) {

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

            Pet pet = createPetFromPetDTO(requestPetDTO, new Pet());
//            Optional<Breed> optionalBreed = breedRepository.findById(petDTO.getBreedId());
//            if (optionalBreed.isEmpty()) {
//                responseDTO.addError("Breed wrong");
//                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
//            }
//            pet.setBreed(optionalBreed.get());
//            pet.setType(optionalBreed.get().getPetType().getName());
//
//            if (petDTO.getAdditionalBreedId() != null && !petDTO.getAdditionalBreedId().isEmpty()) {
//                Optional<Breed> optionalAdditionalBreed = breedRepository.findById(petDTO.getAdditionalBreedId());
//                if (optionalAdditionalBreed.isEmpty()) {
//                    responseDTO.addError("Additional breed wrong");
//                    return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
//                }
//                pet.setAdditionalBreed(optionalAdditionalBreed.get());
//            }
            pet.setUser(optionalUser.get());
            pet.setLost(false);
//            if (petDTO.getTagId() != null) {
//                Optional<Tag> optionalTag = tagRepository.findByQrIdOrNfcId(petDTO.getTagId());
//                if (optionalTag.isEmpty()) {
//                    responseDTO.addError("Tag don't exists in our database...");
//                    return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
//                }
//                if (optionalTag.get().getUserId() != null || optionalTag.get().getPetId() != null) {
//                    responseDTO.addError("This tag is already in use by someone else");
//                    return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
//                }
//                pet.setTag(optionalTag.get());
              pet = petRepository.save(pet);
//               optionalTag.get().setUserId(optionalUser.get().getId());
//                optionalTag.get().setNewUserId(optionalUser.get().getId());
//                optionalTag.get().setPetId(pet.getId());
//                tagRepository.saveAndFlush(optionalTag.get());
//            }else {
//                pet = petRepository.save(pet);
//            }
            responseDTO.setPayload(pet);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getPet(String id,Jwt jwt) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
            if (optionalUser.isEmpty()) {
                responseDTO.addError("User not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            Optional<Pet> optionalPet = petRepository.findById(id);
            if (optionalPet.isEmpty()) {
                responseDTO.addError("Pet not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    public ResponseEntity<?> deletePet(String id,Jwt jwt) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
            if (optionalUser.isEmpty()) {
                responseDTO.addError("User not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            Optional<Pet> optionalPet = petRepository.findById(id);
            Pet pet = optionalPet.get();
            if (optionalPet.isEmpty()) {
                responseDTO.addError("Pet not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            if (!optionalUser.get().getId().equals(optionalPet.get().getUser().getId())) {
                responseDTO.addError("You are not the owner of this pet. You cannot delete it");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            optionalPet.get().setUser(null);
            petRepository.deleteById(id);
            petRepository.flush();
            responseDTO.addInfo("Pet deleted");
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

        private Pet createPetFromPetDTO(RequestPetDTO petDTO, Pet pet) {
        if (petDTO.getName() != null && !petDTO.getName().equals(pet.getName())) {
            pet.setName(petDTO.getName());
        }
        if (petDTO.getAge() != null && !petDTO.getAge().equals(pet.getAge())) {
            pet.setAge(petDTO.getAge());
        }

        if (petDTO.getWeight() != null && !petDTO.getWeight().equals(pet.getWeight())) {
            pet.setWeight(petDTO.getWeight());
        }


        return pet;
    }
}
