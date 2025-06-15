package com.example.demo.repository;

import com.example.demo.entity.Reservation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	Optional<Reservation> findById(Long id);
}
