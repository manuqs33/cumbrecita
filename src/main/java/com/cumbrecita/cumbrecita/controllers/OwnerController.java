/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.entities.ClientTicket;
import com.cumbrecita.cumbrecita.entities.Lodging;
import com.cumbrecita.cumbrecita.entities.Owner;
import com.cumbrecita.cumbrecita.entities.OwnerTicket;
import com.cumbrecita.cumbrecita.entities.Reservation;
import com.cumbrecita.cumbrecita.repositories.LodgingRepository;
import com.cumbrecita.cumbrecita.repositories.OwnerTicketRepository;
import com.cumbrecita.cumbrecita.repositories.ReservationRepository;
import com.cumbrecita.cumbrecita.services.ErrorService;
import com.cumbrecita.cumbrecita.services.OwnerTicketService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author sara
 */
@Controller
public class OwnerController {

    @Autowired
    public OwnerTicketRepository otr;
    @Autowired
    public ReservationRepository rR;
    @Autowired
    public OwnerTicketService otS;
    @Autowired
    public LodgingRepository lR;

//    @GetMapping("/client/ticket/{id}")//para ver un ticket en especifico
//    public String viewTicket(@PathVariable("id") String id, ModelMap model, HttpSession session) {
//
//        Owner admin = (Owner) session.getAttribute("sessionOwner");
//        if (admin == null) {
//            return "redirect:/";
//        }
//
//        OwnerTicket ot = otr.getById(id);
//
//        if (ot != null) {
//            model.put("ticket", ot);
//        }
//
//        return "ticket.html";
//    }
//
//    @GetMapping("/owner/new-ticket")
//    public String newTicket(ModelMap model, HttpSession session) {
//        Owner owner = (Owner) session.getAttribute("sessionOwner");
//        if (owner == null) {
//            return "redirect:/";
//        }
//        List<Lodging> lods = lR.searchByOwner(owner.getId());
//
//        model.addAttribute("lods", lods);
//
//        return "newTicket.html";
//    }
//    
//    @GetMapping("/owner/mytickets")
//    public String myTickets(ModelMap model, HttpSession session) {
//        
//        Owner owner = (Owner) session.getAttribute("sessionOwner");
//        if (owner == null) {
//            return "redirect:/";
//        }
//        
//        List<OwnerTicket> mytickets = otr.showMyTickets(owner.getId());
//        
//        model.put("tickets", mytickets);
//        
//        return "mytickets.html";
//    }
//
//    @PostMapping("/owner/create-ticket")
//    public String createTicket(HttpSession session, ModelMap model, String subjet, String text, MultipartFile file, String idLodging) {
//
//        try {
//            otS.newTicket(lR.getById(idLodging), subjet, text, file);
//        } catch (ErrorService ex) {
//            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return "ticket.html";
//    }
}
