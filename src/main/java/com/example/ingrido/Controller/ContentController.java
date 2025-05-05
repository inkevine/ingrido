package com.example.ingrido.Controller;

import com.example.ingrido.Model.MyAppUser;
import com.example.ingrido.Model.Recipe;
import com.example.ingrido.Service.MyAppUserService;
import com.example.ingrido.Service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContentController {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private MyAppUserService myAppUserService;

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/req/login")
    public String login(){
        return "login";
    }

    @GetMapping("/req/signup")
    public String signup(){
        return "signup";
    }
    @GetMapping("/index")
    public String home(Model model,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "category", required = false) String category) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("recipes", recipeService.searchRecipes(keyword));
        } else if (category != null && !category.isEmpty()) {
            model.addAttribute("recipes", recipeService.filterByCategory(category));
        } else {
            model.addAttribute("recipes", recipeService.getAllRecipes());
        }
        return "index";
    }

        @GetMapping("/profile")
    public String myProfile(Model model) {
        return "profile";
    }


}