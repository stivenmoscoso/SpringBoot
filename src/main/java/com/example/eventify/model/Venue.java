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
@Table

public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false, length = 50)
    private String nombre;
    @Column (nullable = false, length = 50)
    private String direccion;
    @Column (nullable = false, length = 50)
    private Integer capacidad;
    @Column(length = 50)
    private String ciudad;

    public Venue(Long id, String nombre, String direccion, Integer capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidad = capacidad;
        this.ciudad = "Sin ciudad";
    }
}
