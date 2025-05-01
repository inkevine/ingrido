package com.example.ingrido.Service;

import com.example.ingrido.Model.Recipe;
import com.example.ingrido.Repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public void addRating(Long id, int rating, String review) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe ID: " + id));

        // Initialize lists if null
        if (recipe.getRatings() == null) {
            recipe.setRatings(new ArrayList<>());
        }
        if (recipe.getReviews() == null) {
            recipe.setReviews(new ArrayList<>());
        }

        // Add the new rating and review
        recipe.getRatings().add(rating);
        recipe.getReviews().add(review);

        // Calculate and set average rating
        double avg = recipe.getRatings().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        recipe.setAverageRating(avg);

        // Save the updated recipe
        recipeRepository.save(recipe);
    }

    public double getAverageRating(Recipe recipe) {
        if (recipe.getRatings() == null || recipe.getRatings().isEmpty()) {
            return 0.0;
        }
        return recipe.getRatings().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }


}
