package com.linkfix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DniResponse {
    private boolean success;
    private DniData data;

    @Data
    public static class DniData {
        @JsonProperty("nombres")
        private String nombre;
        @JsonProperty("apellido_paterno")
        private String apellidoPaterno;
        @JsonProperty("apellido_materno")
        private String apellidoMaterno;
    }

}
