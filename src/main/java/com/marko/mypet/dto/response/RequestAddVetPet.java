package com.marko.mypet.dto.response;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
public class RequestAddVetPet {

    @NotNull(message = "idPet is required")
    private String idPet;
    @NotNull(message = "idVet is required")
    private String idVet;
}
