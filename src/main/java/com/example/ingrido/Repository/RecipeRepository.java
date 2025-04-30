package com.example.ingrido.Repository;


import com.example.ingrido.Model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByTitleContainingIgnoreCase(String keyword);
    List<Recipe> findByCategory(String category);
    List<Recipe> findByFavoriteTrue();
    List<Recipe> findTop3ByOrderByIdAsc();

}
