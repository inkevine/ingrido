package com.example.ingrido.Controller;

import com.example.ingrido.Model.Recipe;
import com.example.ingrido.Service.ChatGPTService;
import com.example.ingrido.Service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class RecipeController {
    @Autowired
    private RecipeService recipeService;


    @GetMapping("/myrecipes")
    public String myRecipes(Model model) {
        return "myrecipes";
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "add-recipe";
    }

    @PostMapping("/add")
    public String addRecipe(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("category") String category,
                            @RequestParam("ingredients") String ingredients,
                            @RequestParam("steps") String steps,
                            @RequestParam("cookingTime") int cookingTime,
                            @RequestParam(value = "imageUrl", required = false) MultipartFile imageFile) {

        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setIngredients(ingredients);
        recipe.setCategory(category);
        recipe.setDescription(description);
        recipe.setCookingTime(cookingTime);


        // ✨ Split steps
        List<String> stepList = List.of(steps.split("\\r?\\n"));
        recipe.setSteps(stepList);

        // ✨ Save image
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadsDir = "uploads/";
                String realPathToUploads = new java.io.File("").getAbsolutePath() + "/" + uploadsDir;
                java.io.File uploadDir = new java.io.File(realPathToUploads);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String originalFilename = imageFile.getOriginalFilename();
                String newFilename = System.currentTimeMillis() + "_" + originalFilename;
                Path filePath = Paths.get(realPathToUploads + newFilename);
                Files.write(filePath, imageFile.getBytes());

                recipe.setImageUrl("/uploads/" + newFilename);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        recipeService.saveRecipe(recipe);
        return "redirect:/index";
    }

    @GetMapping("/favorite/{id}")
    public String favoriteRecipe(@PathVariable Long id) {
        recipeService.toggleFavorite(id);
        return "redirect:/index";
    }

    @GetMapping("/favorites")
    public String viewFavorites(Model model) {
        model.addAttribute("recipes", recipeService.getFavoriteRecipes());
        return "favorite";
    }

    @GetMapping("/recipe/{id}")
    public String viewRecipe(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe Id:" + id));

        // Add the average rating to the recipe object
        double averageRating = recipeService.getAverageRating(recipe);
        recipe.setAverageRating(averageRating);

        model.addAttribute("recipe", recipe);
        return "view-recipe";
    }

    @PostMapping("/recipe/{id}/rate")
    public String rateRecipe(@PathVariable Long id,
                             @RequestParam int rating,
                             @RequestParam String review) {
        try {
            // Validate rating
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }

            recipeService.addRating(id, rating, review);
            return "redirect:/recipe/" + id;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error adding rating: " + e.getMessage());

            return "redirect:/recipe/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return "redirect:/index";
    }

    @Autowired
    private ChatGPTService chatGPTService;

    @GetMapping("/assistant")
    public String assistantPage() {
        return "assistant";
    }

    @PostMapping("/assistant")
    public String askAssistant(@RequestParam("question") String question, Model model) {
        String answer = chatGPTService.askGroq(question);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "assistant";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe Id:" + id));
        model.addAttribute("recipe", recipe);
        return "edit-recipe";
    }

    @PostMapping("/update/{id}")
    public String updateRecipe(@PathVariable Long id, @ModelAttribute Recipe updatedRecipe) {
        Recipe existingRecipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe Id:" + id));

        existingRecipe.setTitle(updatedRecipe.getTitle());
        existingRecipe.setDescription(updatedRecipe.getDescription());
        existingRecipe.setCategory(updatedRecipe.getCategory());
        existingRecipe.setIngredients(updatedRecipe.getIngredients());
        existingRecipe.setSteps(updatedRecipe.getSteps());
        existingRecipe.setCookingTime(updatedRecipe.getCookingTime());

        // Notice: we are not touching existingRecipe.setImageUrl(...)
        // so the original image stays as is

        recipeService.saveRecipe(existingRecipe);
        return "redirect:/recipe/" + id;
    }





}
