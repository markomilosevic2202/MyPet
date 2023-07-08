package com.marko.mypet.dto.response;


import com.marko.mypet.entity.Specialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
public class RequestVetDTO {
    private String id;
    @NotBlank(message = "firstName should have between 2 and 50 characters")
    @NotNull(message = "firstName is required")
    @Size(min = 2, max = 50, message = "firstName should have between 2 and 50 characters")
    private String firstName;
    @NotBlank(message = "lastName should have between 2 and 50 characters")
    @NotNull(message = "lastName is required")
    @Size(min = 2, max = 50, message = "lastName should have between 2 and 50 characters")
    private String lastName;
    @NotNull(message = "idSpecialty is required")
    private String idSpecialty;
    private List<RequestPetDTO> pets;



}
