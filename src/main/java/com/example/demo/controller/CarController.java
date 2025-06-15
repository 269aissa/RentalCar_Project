package com.example.demo.controller;

import com.example.demo.entity.Car;
import com.example.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired private CarService carService;

    @GetMapping
    public List<Car> allCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public Optional<Car> getCar(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @PostMapping
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @RequestBody Car car) {
        return carService.updateCar(id, car);
    }
    
    @PostMapping("/update/{id}")
    public String updateCar(@PathVariable Long id, @ModelAttribute Car car, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        carService.updateCar(id, car);
        return "redirect:/cars";
    }


    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<Car> carsByCategory(@PathVariable Long categoryId) {
        return carService.findCarsByCategory(categoryId);
    }

    @GetMapping("/available")
    public List<Car> availableCars() {
        return carService.findAvailableCars();
    }
}
