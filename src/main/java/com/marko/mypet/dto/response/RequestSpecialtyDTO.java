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
public class RequestSpecialtyDTO {

    @NotBlank(message = "Username should have between 2 and 50 characters")
    @NotNull(message = "Username is required")
    @Size(min = 2, max = 50, message = "Username should have between 2 and 50 characters")
    private String nameSpecialty;
}
