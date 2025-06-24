package com.linkfix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
<<<<<<< HEAD
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
=======
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

>>>>>>> fa3e532499547fc40d7bd7671b426a36d6cbf0d3
}
