package com.linkfix.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "departamento")
public class DepartamentoEntity implements Serializable 
{
    private static final long serialVersionUID = 1L;
    @Id
    @Column
    private String id;

    @Column(length = 45)
    private String name;
}
