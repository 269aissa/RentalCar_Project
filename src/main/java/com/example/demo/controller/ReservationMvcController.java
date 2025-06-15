package com.example.demo.controller;


import java.security.Principal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Car;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository; // <-- À ajouter
import com.example.demo.service.CarService;
import com.example.demo.service.ReservationService;
import com.example.demo.service.StripeService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/reservations")
public class ReservationMvcController {
    @Autowired
    ReservationService reservationService;
    @Autowired
    CarService carService;
    @Autowired
    UserRepository userRepository; // <-- À ajouter
    @Autowired StripeService stripeService;

    
    @Autowired
    private StripeService stripeService1;

    @PostMapping("/payer/{id}")
    public String payer(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));
        try {
            String url = stripeService.createCheckoutSession(reservation.getMontantTotal(), reservation.getId().toString());
            return "redirect:" + url;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur Stripe : " + e.getMessage());
            return "redirect:/reservations/mes";
        }
    }

    @GetMapping("/facture/{id}")
    public void facture(@PathVariable Long id, @RequestParam String session_id, HttpServletResponse response) {
		Reservation reservation = reservationService.findById(id)
				.orElseThrow(() -> new RuntimeException("Réservation introuvable"));
		
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=facture.pdf");
   }




    
    @GetMapping("/success")
    public String paiementSucces(@RequestParam("session_id") String sessionId, RedirectAttributes redirectAttributes) {
        // Ici tu mets à jour le statut de la réservation concernée si besoin
        redirectAttributes.addFlashAttribute("success", "Paiement effectué avec succès !");
        return "redirect:/reservations/mes";
    }

    @GetMapping("/cancel")
    public String paiementAnnule(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Paiement annulé.");
        return "redirect:/reservations/mes";
    }



    // Page de réservation (formulaire)
    @GetMapping("/nouvelle")
    public String showReservationForm(@RequestParam Long carId, Model model) {
        Car car = carService.getCarById(carId).orElseThrow();
        model.addAttribute("car", car);
        return "reservation_form";
    }

    @GetMapping("/mes")
    public String mesReservations(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                      .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            model.addAttribute("reservations", reservationService.findAll());
        } else {
            model.addAttribute("reservations", reservationService.findByUser(user.getId()));
        }        
        return "mes_reservations";
    }

    @PostMapping("/annuler/{id}")
    public String annuler(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                      .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        reservationService.annuler(id, user.getId());
        return "redirect:/reservations/mes";
    }

  

    @PostMapping("/terminer/{id}")
    public String terminer(@PathVariable Long id) {
        reservationService.terminer(id);
        return "redirect:/reservations/mes";
    }
    
    @PostMapping("/creer")
    public String creerReservation(
        @RequestParam Long carId,
        @RequestParam String dateDebut,
        @RequestParam String dateFin,
        Principal principal,
        RedirectAttributes redirectAttributes
    ) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                      .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        try {
            reservationService.reserver(
                carId,
                user.getId(),
                LocalDate.parse(dateDebut),
                LocalDate.parse(dateFin)
            );
            redirectAttributes.addFlashAttribute("success", "Réservation réussie !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservations/nouvelle?carId=" + carId;
        }
        return "redirect:/reservations/mes";
    }

}
