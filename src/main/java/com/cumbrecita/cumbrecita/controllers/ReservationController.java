package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.entities.Reservation;
import com.cumbrecita.cumbrecita.repositories.ReservationRepository;
import com.cumbrecita.cumbrecita.services.ReservationService;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReservationController {

    @Autowired
    private ReservationRepository rR;
    @Autowired
    private ReservationService rS;

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
        return "owner-reservation.html";
    }

//    @PreAuthorize("hasAnyRole('CLIENT')")
    @GetMapping("/reservation/list")
    public String showReserv(ModelMap model, HttpSession session) {
        Client client = (Client) session.getAttribute("sessionClient");
        if (client == null) {
            return "redirect:/login";
        }
        
        if (client != null) {
            String mail = client.getMail();
            model.put("namelog", mail);
        }
        List<Reservation> reserv = rR.searchClient(client.getId());//trae la lista de reservas que haya realizado el usuario
        model.addAttribute("reserv", reserv);
        return "client-reservation.html";
    }
    
    @GetMapping("/reservation/pay")
    public String pay(@RequestParam String reserveId, ModelMap model){
        
        Reservation reserve = rR.searchById(reserveId);
        
        try {
            Preference preference = rS.pay(reserve);
            model.put("preference", preference.getId());
        } catch (MPException ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "pay.html";
    }
    
    @GetMapping("/reservation/delete")
    public String deleteReserv(@RequestParam String id){
        rS.deleteReservationById(id);
        return "redirect:/reservation/list";
    }
}
