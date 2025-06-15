package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Reservation reservation; // Paiement lié à une réservation

    private double montant;
    private LocalDateTime datePaiement;

    @Enumerated(EnumType.STRING)
    private Statut statut; // EN_ATTENTE, EFFECTUE, ECHEC

    public enum Statut {
        EN_ATTENTE, EFFECTUE, ECHEC
    }

    private String mode; // ex : "Carte bancaire", "Paypal"
    private String referenceTransaction; // référence de paiement externe
}
