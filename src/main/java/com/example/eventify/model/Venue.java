package com.example.eventify.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "venue")

public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false, length = 50)
    private String nombre;
    @Column (nullable = false, length = 50)
    private String direccion;
    @Column (nullable = false)
    private Integer capacidad;
    @Column(nullable = false, length = 50)
    private String ciudad;


    }
