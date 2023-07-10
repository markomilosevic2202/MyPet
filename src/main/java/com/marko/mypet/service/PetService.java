package com.marko.mypet.service;

import com.marko.mypet.dto.response.*;
import com.marko.mypet.entity.Breed;
import com.marko.mypet.entity.Pet;
import com.marko.mypet.entity.User;
import com.marko.mypet.entity.Vet;
import com.marko.mypet.repository.BreedRepository;
import com.marko.mypet.repository.PetRepository;
import com.marko.mypet.repository.UserRepository;
import com.marko.mypet.repository.VetRepository;
import com.marko.mypet.tool.JwtTools;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



@RequiredArgsConstructor
@Service
public class PetService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final VetRepository vetRepository;
    private final BreedRepository breedRepository;
    private EntityManager entityManager;

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
            Optional<Breed> optionalBreed = breedRepository.findById(requestPetDTO.getIdBreed());
            if (optionalBreed.isEmpty()) {
                responseDTO.addError("Breed not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            Pet pet = createPetFromPetDTO(requestPetDTO, new Pet());
            pet.setLost(false);
            pet.setUser(optionalUser.get());
            pet.setBreed(optionalBreed.get());
            pet = petRepository.save(pet);
            responseDTO.setPayload(pet);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getPet(String id, Jwt jwt) {
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

            Pet pet = optionalPet.get();
            RequestPetDTO petDTO = mapPetToDTO(pet);
            responseDTO.setPayload(petDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getPets(Jwt jwt) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
            if (optionalUser.isEmpty()) {
                responseDTO.addError("User not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            List<Pet> pets = petRepository.findAll();
            if (pets.isEmpty()) {
                responseDTO.setPayload(new ArrayList<Vet>());
                responseDTO.addError("Pet not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            List<RequestPetDTO> petDTOList = mapPetListToDTOList(petRepository.findAll());
            responseDTO.setPayload(petDTOList);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updatePet(RequestPetUpdateDTO requestPetUpdateDTO, Jwt jwt) {

        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
            if (optionalUser.isEmpty()) {
                responseDTO.addError("User not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            Pet pet = optionalUser.get().getPetById(requestPetUpdateDTO.getId());
            if (pet == null) {
                responseDTO.addError("Pet not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            Pet updatedPet = updatePetFromPetDTO(requestPetUpdateDTO, pet);
            if (requestPetUpdateDTO.getIdBreed() != null) {
                Optional<Breed> optionalBreed = breedRepository.findById(requestPetUpdateDTO.getIdBreed());
                if (optionalBreed.isEmpty() && !Objects.equals(requestPetUpdateDTO.getIdBreed(), "")) {
                    responseDTO.addError("Breed doesn't exists!");
                    return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                }

                updatedPet.setBreed(optionalBreed.get());

            }

            responseDTO.setPayload(petRepository.save(updatedPet));
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<?> deletePet(String id, Jwt jwt) {
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

            if (!optionalUser.get().getId().equals(optionalPet.get().getUser().getId())) {
                responseDTO.addError("You are not the owner of this pet. You cannot delete it");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            optionalPet.get().setUser(null);
            petRepository.deleteById(id);
            // petRepository.flush();
            responseDTO.addInfo("Pet deleted");
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addVet(RequestAddVetPet requestAddVetPet, BindingResult bindingResult, Jwt jwt) {
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
            Optional<Pet> optionalPet = petRepository.findById(requestAddVetPet.getIdPet());
            if (optionalPet.isEmpty()) {
                responseDTO.addError("Pet not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            if (!optionalPet.get().getUser().getId().equals(optionalUser.get().getId())) {
                responseDTO.addError("You are not the owner of this pet");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            Optional<Vet> optionalVet = vetRepository.findById(requestAddVetPet.getIdVet());
            if (optionalVet.isEmpty()) {
                responseDTO.addError("Vet not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            Pet pet = optionalPet.get();
            if (pet.getVets().contains(optionalVet.get())) {
                responseDTO.addError("Vet already exists for this pet");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            pet.addVet(optionalVet.get());
            pet = petRepository.save(pet);
            RequestPetDTO petDTO = mapPetToDTO(pet);
            responseDTO.setPayload(petDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            responseDTO.addError(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteVet(RequestAddVetPet requestAddVetPet, Jwt jwt) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {


            Optional<User> optionalUser = userRepository.findUserByEmail(JwtTools.getEmailFromOAuthToken(jwt));
            if (optionalUser.isEmpty()) {
                responseDTO.addError("User not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            Optional<Pet> optionalPet = petRepository.findById(requestAddVetPet.getIdPet());
            if (optionalPet.isEmpty()) {
                responseDTO.addError("Pet not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            Optional<Vet> optionalVet = vetRepository.findById(requestAddVetPet.getIdVet());
            if (optionalVet.isEmpty()) {
                responseDTO.addError("Vet not found");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            if (!optionalUser.get().getId().equals(optionalPet.get().getUser().getId())) {
                responseDTO.addError("You are not the owner of this pet. You cannot delete it");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            RequestPetDTO requestPetDTO = mapPetToDTO(optionalPet.get());
            if (requestPetDTO.getVets().contains(optionalVet.get())) {
                responseDTO.addError("This vet is not on this pet's list");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            petRepository.deleteVet(requestAddVetPet.getIdPet(), requestAddVetPet.getIdVet());
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

    private Pet updatePetFromPetDTO(RequestPetUpdateDTO petDTO, Pet pet) {
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

    private RequestPetDTO mapPetToDTO(Pet pet) {
        RequestPetDTO requestPetDTO = new RequestPetDTO();
        requestPetDTO.setId(pet.getId());
        requestPetDTO.setName(pet.getName());
        requestPetDTO.setWeight(pet.getWeight());
        requestPetDTO.setAge(pet.getAge());
        List<RequestVetDTO> vetDTOList = new ArrayList<>();
        for (Vet vet : pet.getVets()) {
            RequestVetDTO vetDTO = new RequestVetDTO();
            vetDTO.setId(vet.getId());
            vetDTO.setFirstName(vet.getFirstName());
            vetDTO.setLastName(vet.getLastName());
            vetDTO.setIdSpecialty(vet.getSpecialty().getId());
            vetDTO.setSpecialty(vet.getSpecialty());
            vetDTOList.add(vetDTO);
        }

        requestPetDTO.setVets(vetDTOList);

        return requestPetDTO;
    }

    private List<RequestPetDTO> mapPetListToDTOList(List<Pet> petList) {
        List<RequestPetDTO> petDTOList = new ArrayList<>();
        for (Pet pet : petList) {
            RequestPetDTO petDTO = mapPetToDTO(pet);
            petDTOList.add(petDTO);
        }
        return petDTOList;
    }


}