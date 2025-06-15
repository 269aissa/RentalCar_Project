package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Car;
import com.example.demo.service.CarService;

@Controller
@RequestMapping("/cars")
public class CarMvcController {
    @Autowired CarService carService;

    @GetMapping
    public String showCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "cars";
    }

    @GetMapping("/{id}")
    public String carDetail(@PathVariable Long id, Model model) {
        Car car = carService.getCarById(id).orElseThrow();
        model.addAttribute("car", car);
        return "car_detail";
    }
    
    @PostMapping("/save")
    public String saveCar(
            @Validated @ModelAttribute Car car,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "car_form";
        }
        // On n’a plus besoin de gérer d’image ici
        carService.addCar(car);

        return "redirect:/cars";
    }


    @PostMapping("/update")
    public String updateCar(
            @Validated @ModelAttribute Car car,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "car_form";
        }
        carService.updateCar(car.getId(), car);
        return "redirect:/cars";
    }



    

}
