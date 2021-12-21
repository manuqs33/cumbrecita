package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.entities.Reservation;
import com.cumbrecita.cumbrecita.repositories.ReservationRepository;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReservationController {

    @Autowired
    private ReservationRepository rR;

    @PreAuthorize("hasAnyRole('OWNER')")
    @GetMapping("/owner/reservation")
    public String reservs(ModelMap model, HttpSession session) {
        Client owner = (Client) session.getAttribute("sessionClient");
        List<Reservation> reservs = rR.findAll();
        for (Reservation reserv : reservs) {
            if (!reserv.getL().getO().getId().equals(owner.getId())) {
                reservs.remove(reserv);
            }
        }
        model.addAttribute("reservs", reservs);
        return "owner-reservs.html";
    }

    @PreAuthorize("hasAnyRole('CLIENT')")
    @PostMapping("/client/reservation")
    public String showReserv(ModelMap model, HttpSession session) {
        Client client = (Client) session.getAttribute("sessionClient");
        List<Reservation> reserv = rR.searchClient(session.getId());//trae la lista de reservas que haya realizado el usuario
        model.addAttribute("reserv", reserv);
        return "client-reserv.html";
    }
}
