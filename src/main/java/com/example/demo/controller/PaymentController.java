package com.example.demo.controller;

import com.example.demo.entity.Payment;
import com.example.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired private PaymentService paymentService;

    @GetMapping
    public List<Payment> allPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/reservation/{reservationId}")
    public Optional<Payment> paymentByReservation(@PathVariable Long reservationId) {
        return paymentService.getPaymentByReservation(reservationId);
    }

    @PostMapping("/pay")
    public Payment pay(
            @RequestParam Long reservationId,
            @RequestParam double montant,
            @RequestParam String mode,
            @RequestParam(required = false) String reference
    ) {
        return paymentService.effectuerPaiement(reservationId, montant, mode, reference);
    }
}
