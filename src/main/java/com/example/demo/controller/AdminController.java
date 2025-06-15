package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Car;
import com.example.demo.entity.Category;
import com.example.demo.service.CarService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ReservationService;
import com.example.demo.service.UserService;

import lombok.Data;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private CarService carService;
    @Autowired private CategoryService categoryService;
    @Autowired private UserService userService;
    @Autowired private ReservationService reservationService;
    @Autowired private PaymentService paymentService;

    // 1. Dashboard admin
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", new StatsDto(0, 0, 0, 0
        ));
        return "admin_dashboard";
    }

    // 2. Gestion des voitures
    @GetMapping("/cars")
    public String adminCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "admin_cars";
    }
    
    


    @GetMapping("/cars/add")
    public String addCarForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "car_form";
    }

    @PostMapping("/cars/add")
    public String addCar(@ModelAttribute Car car, @RequestParam("image") MultipartFile file) {
      
        
        carService.addCar(car);	
        return "redirect:/cars";
    }


    @GetMapping("/cars/edit/{id}")
    public String editCarForm(@PathVariable Long id, Model model) {
        Car car = carService.getCarById(id).orElseThrow();
        model.addAttribute("car", car);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "car_form";
    }

    @PostMapping("/cars/edit/{id}")
    public String editCar(@PathVariable Long id, @ModelAttribute Car car, RedirectAttributes redirectAttributes) {
        carService.updateCar(id, car);
        redirectAttributes.addFlashAttribute("success", "Voiture modifiée avec succès !");
        return "redirect:/admin/cars";
    }

    @PostMapping("/cars/delete/{id}")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        carService.deleteCar(id);
        redirectAttributes.addFlashAttribute("success", "Voiture supprimée !");
        return "redirect:/admin/cars";
    }

    // 3. Gestion des catégories
    @GetMapping("/categories")
    public String adminCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin_categories";
    }

    @GetMapping("/categories/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-form";
    }

    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryService.addCategory(category);
        redirectAttributes.addFlashAttribute("success", "Catégorie ajoutée !");
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id).orElseThrow();
        model.addAttribute("category", category);
        return "category-form";
    }

    @PostMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryService.updateCategory(id, category);
        redirectAttributes.addFlashAttribute("success", "Catégorie modifiée !");
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("success", "Catégorie supprimée !");
        return "redirect:/admin/categories";
    }

    // 4. Gestion des utilisateurs (listing simple)
    @GetMapping("/users")
    public String adminUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin_users";
    }

    // 5. Gestion des réservations (listing simple)
    @GetMapping("/reservations")
    public String adminReservations(Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        return "admin_reservations";
    }

    // 6. Gestion des paiements (listing simple)
    @GetMapping("/payments")
    public String adminPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "payments";
    }

    // DTO pour le dashboard (nombre d'entités)
    @Data
    public static class StatsDto {
        private final int nbUsers;
        private final int nbCars;
        private final int nbReservations;
        private final int nbPayments;
    }
}
