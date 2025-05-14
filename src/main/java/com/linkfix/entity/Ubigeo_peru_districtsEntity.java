package com.linkfix.entity;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import jakarta.persistence.*;
import java.io.Serializable;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


@Entity(name = "Ubigeo_peru_districts")
@Table(name = "ubigeo_peru_districts")

public class Ubigeo_peru_districtsEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "province_id")
    private String province_id;

    @Column(name = "department_id")
    private String department_id;
}
