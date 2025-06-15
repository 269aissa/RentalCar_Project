package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Car;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.User;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Créer une réservation (avec vérification de disponibilité)
    public Reservation reserver(Long carId, Long long1, LocalDate debut, LocalDate fin) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Voiture introuvable"));
        User user = userRepository.findById((long) long1)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Vérifie que la voiture est dispo pour la période
        if (!isCarDisponible(car, debut, fin)) {
            throw new RuntimeException("La voiture n'est pas disponible pour ces dates");
        }

        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setUser(user);
        reservation.setDateDebut(debut);
        reservation.setDateFin(fin);
        reservation.setStatut(Reservation.Statut.EN_COURS);
        long jours = debut.until(fin).getDays() + 1;
        reservation.setMontantTotal(jours * car.getPrixParJour());

        // Marquer la voiture comme louée
        car.setEtat(Car.Etat.LOUEE);
        carRepository.save(car);

        return reservationRepository.save(reservation);
    }

    // 2. Annuler une réservation
    public void annuler(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new RuntimeException("Action non autorisée");
        }
        if (reservation.getStatut() != Reservation.Statut.EN_COURS) {
            throw new RuntimeException("Impossible d'annuler une réservation déjà terminée ou annulée");
        }
        reservation.setStatut(Reservation.Statut.ANNULEE);
        reservation.getCar().setEtat(Car.Etat.DISPONIBLE);
        carRepository.save(reservation.getCar());
        reservationRepository.save(reservation);
    }

    // 3. Terminer une réservation (après retour du véhicule)
    public void terminer(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));
        reservation.setStatut(Reservation.Statut.TERMINEE);
        reservation.getCar().setEtat(Car.Etat.DISPONIBLE);
        carRepository.save(reservation.getCar());
        reservationRepository.save(reservation);
    }

    // 4. Lister les réservations d’un user
    public List<Reservation> findByUser(Long userId) {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getUser().getId().equals(userId))
                .toList();
    }

    // 5. Lister toutes les réservations (admin)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    // 6. Vérifier la dispo d’une voiture sur une période
    public boolean isCarDisponible(Car car, LocalDate debut, LocalDate fin) {
        List<Reservation> reservations = reservationRepository.findAll().stream()
                .filter(r -> r.getCar().getId().equals(car.getId()))
                .filter(r -> r.getStatut() == Reservation.Statut.EN_COURS)
                .toList();

        for (Reservation r : reservations) {
            // Si les dates se chevauchent
            if (!(fin.isBefore(r.getDateDebut()) || debut.isAfter(r.getDateFin()))) {
                return false;
            }
        }
        return car.getEtat() == Car.Etat.DISPONIBLE;
    }

	public Optional<Reservation> findById(Long id) {
		return reservationRepository.findById(id);
	}


}
