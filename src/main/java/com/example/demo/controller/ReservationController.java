package com.example.demo.controller;

import com.example.demo.entity.Reservation;
import com.example.demo.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired private ReservationService reservationService;

    @GetMapping
    public List<Reservation> allReservations() {
        return reservationService.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> reservationsByUser(@PathVariable Long userId) {
        return reservationService.findByUser(userId);
    }

    @PostMapping("/create")
    public Reservation createReservation(
            @RequestParam Long carId,
            @RequestParam int userId,
            @RequestParam String dateDebut,
            @RequestParam String dateFin
    ) {
        return reservationService.reserver(
                carId,
                (long) userId,
                LocalDate.parse(dateDebut),
                LocalDate.parse(dateFin)
        );
    }

    @PostMapping("/{id}/cancel")
    public void annulerReservation(@PathVariable Long id, @RequestParam Long userId) {
        reservationService.annuler(id, userId);
    }

    @PostMapping("/{id}/finish")
    public void terminerReservation(@PathVariable Long id) {
        reservationService.terminer(id);
    }
}
