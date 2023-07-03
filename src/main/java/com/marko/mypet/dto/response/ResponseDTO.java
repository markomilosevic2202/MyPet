package com.marko.mypet.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object payload;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> info = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<>();

    public void addInfo(String info) {
        if (this.info == null) {
            this.info = new ArrayList<>();
        }
        this.info.add(info);
    }

    public void addError(String error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }
}
