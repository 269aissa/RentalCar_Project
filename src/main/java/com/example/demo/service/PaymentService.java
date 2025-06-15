package com.example.demo.service;

import com.example.demo.entity.Payment;
import com.example.demo.entity.Reservation;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // Effectuer un paiement (lié à une réservation)
    public Payment effectuerPaiement(Long reservationId, double montant, String mode, String ref) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setMontant(montant);
        payment.setMode(mode);
        payment.setReferenceTransaction(ref);
        payment.setStatut(Payment.Statut.EFFECTUE);
        payment.setDatePaiement(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    // Lister tous les paiements
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Paiement d'une réservation donnée
    public Optional<Payment> getPaymentByReservation(Long reservationId) {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getReservation().getId().equals(reservationId))
                .findFirst();
    }
}
