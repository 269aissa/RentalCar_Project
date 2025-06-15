package com.example.demo.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.User;

@Controller
public class ProfileController {
    @Autowired UserService userService;

    @GetMapping("/profil")
    public String profil(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("user", user);
        return "profil";
    }

    @PostMapping("/profil/modifier")
    public String updateProfil(
            @RequestParam String username,
            @RequestParam String password,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        userService.updateUser(user.getId(), username, password);
        redirectAttributes.addFlashAttribute("success", "Profil mis à jour !");
        return "redirect:/profil";
    }
}
