package com.example.ingrido.Model;

import jakarta.persistence.*;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String quantity; // "2 cups", "3 tbsp", etc.

    @ManyToOne
    private Recipe recipe;
}
