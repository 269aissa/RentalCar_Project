package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
//imports...

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	
	 private String marque;
	 private String modele;
	 private String plaqueImmatriculation;
	 private int annee;
	 private double prixParJour;
	
	 @Enumerated(EnumType.STRING)
	 private Etat etat;
	
	 @ManyToOne
	 private Category category; // Relation avec catégorie
	
	 public enum Etat {
	     DISPONIBLE, LOUEE, MAINTENANCE
	 }
	 
	 		
}
