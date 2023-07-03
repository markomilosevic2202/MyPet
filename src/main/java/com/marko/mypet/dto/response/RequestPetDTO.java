package com.marko.mypet.dto.response;


import jakarta.validation.constraints.NotBlank;
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
public class RequestPetDTO {

    @NotBlank(message = "Name should have between 2 and 50 characters")
    @NotNull(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name should have between 2 and 50 characters")
    private String name;
    private Integer age;
    private Float weight;
}
