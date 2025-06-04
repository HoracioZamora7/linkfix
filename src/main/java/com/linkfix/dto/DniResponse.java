package com.linkfix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DniResponse {

    private boolean success;
    private DniData data;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DniData {
        private String nombre;
        private String apellidos;
        private String dni;
    }
}
