package com.marko.mypet.dto.response;

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
public class RequestPetUpdateDTO {

    private String id;
    private String name;
    private Integer age;
    private Float weight;
    private String idBreed;

}
