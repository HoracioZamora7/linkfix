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


@Entity(name = "UbigeoDistritosEntity")
@Table(name = "UbigeoDistritos")

public class UbigeoDistritosEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "provinceId")
    private String provinceId;

    @Column(name = "departmentId")
    private String departmentId;
}
