package com.example.ingrido.Service;

import com.example.ingrido.Model.Recipe;
import com.example.ingrido.Repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> getSomeRecipes() {
        return recipeRepository.findTop3ByOrderByIdAsc();
    }


    public List<Recipe> searchRecipes(String keyword) {
        return recipeRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Recipe> filterByCategory(String category) {
        return recipeRepository.findByCategory(category);
    }

    public void toggleFavorite(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe ID: " + id));
        recipe.setFavorite(!recipe.isFavorite());
        recipeRepository.save(recipe);
    }

    public List<Recipe> getFavoriteRecipes() {
        return recipeRepository.findByFavoriteTrue();
    }

}
