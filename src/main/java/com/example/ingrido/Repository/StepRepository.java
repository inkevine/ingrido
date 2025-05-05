package com.example.ingrido.Repository;

import com.example.ingrido.Model.Step;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StepRepository extends JpaRepository<Step, Long> {
    List<Step> findByRecipeIdOrderByStepNumberAsc(Long recipeId);
}
