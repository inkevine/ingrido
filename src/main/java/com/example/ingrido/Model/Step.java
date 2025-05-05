package com.example.ingrido.Model;

import jakarta.persistence.*;

@Entity
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stepNumber;
    private String description;

    @ManyToOne
    private Recipe recipe;
}
