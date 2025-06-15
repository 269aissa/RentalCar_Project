package com.example.demo.service;

import com.example.demo.entity.Car;
import com.example.demo.entity.Category;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Lister toutes les voitures
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Trouver une voiture par ID
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    // Ajouter une nouvelle voiture
    public Car addCar(Car car) {
        // S'assurer que la catégorie existe avant l'ajout
        if (car.getCategory() != null && car.getCategory().getId() != null) {
            Category category = categoryRepository.findById(car.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
            car.setCategory(category);
        }
        car.setEtat(Car.Etat.DISPONIBLE); // Par défaut, la voiture est dispo à l'ajout
        return carRepository.save(car);
    }

    // Mettre à jour une voiture
    public Car updateCar(Long id, Car updatedCar) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));

        car.setMarque(updatedCar.getMarque());
        car.setModele(updatedCar.getModele());
        car.setPlaqueImmatriculation(updatedCar.getPlaqueImmatriculation());
        car.setAnnee(updatedCar.getAnnee());
        car.setPrixParJour(updatedCar.getPrixParJour());
        car.setEtat(updatedCar.getEtat());

        if (updatedCar.getCategory() != null && updatedCar.getCategory().getId() != null) {
            Category category = categoryRepository.findById(updatedCar.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
            car.setCategory(category);
        }
		

        return carRepository.save(car);
    }

    // Supprimer une voiture
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    // Lister les voitures par catégorie
    public List<Car> findCarsByCategory(Long categoryId) {
        return carRepository.findAll().stream()
                .filter(car -> car.getCategory() != null && car.getCategory().getId().equals(categoryId))
                .toList();
    }

    // Lister les voitures disponibles
    public List<Car> findAvailableCars() {
        return carRepository.findAll().stream()
                .filter(car -> car.getEtat() == Car.Etat.DISPONIBLE)
                .toList();
    }
}
