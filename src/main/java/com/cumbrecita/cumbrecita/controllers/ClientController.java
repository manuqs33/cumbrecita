/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.entities.ClientTicket;
import com.cumbrecita.cumbrecita.entities.Lodging;
import com.cumbrecita.cumbrecita.entities.OwnerTicket;
import com.cumbrecita.cumbrecita.entities.Reservation;
import com.cumbrecita.cumbrecita.entities.Owner;
import com.cumbrecita.cumbrecita.enumc.Type;
import com.cumbrecita.cumbrecita.repositories.ClientRepository;
import com.cumbrecita.cumbrecita.repositories.ClientTicketRepository;
import com.cumbrecita.cumbrecita.repositories.LodgingRepository;
import com.cumbrecita.cumbrecita.repositories.ReservationRepository;
import com.cumbrecita.cumbrecita.services.ClientService;
import com.cumbrecita.cumbrecita.services.ClientTicketService;
import com.cumbrecita.cumbrecita.services.ErrorService;
import com.cumbrecita.cumbrecita.services.LodgingService;
import com.cumbrecita.cumbrecita.services.ReservationService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Flia Vasquez
 */
@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository cR;
    @Autowired
    private ClientTicketRepository ctr;
    @Autowired
    private LodgingRepository lR;
    @Autowired
    private ReservationService reservationservice;
    @Autowired
    private ReservationRepository rR;
    @Autowired
    private LodgingService lodgingService;
    @Autowired
    private ClientTicketService ctS;

    @GetMapping("/client/authorize/{id}")
    public String authorize(@PathVariable("id") String id, ModelMap model) {
        System.out.println(id);
        try {
            clientService.activeClient(id);
        } catch (ErrorService ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            model.put("error", ex.getMessage());
            return "success.html";
        }

        model.put("title", "Tu usuario fue autorizado correctamente");
        return "redirect:/";
    }

    @GetMapping("/reserve")
    public String reserve(HttpSession session,ModelMap model,@RequestParam String id){
        Client client = (Client) session.getAttribute("sessionClient");
        Owner owner = (Owner) session.getAttribute("sessionOwner");
        if (client == null) {
            return "redirect:/login";
        }
        
        if (owner != null) {
            String mail = owner.getMail();
            model.put("namelog", mail);
        }
        
        if (client != null) {
            String mail = client.getMail();
            model.put("namelog", mail);
        }
        
        Lodging lodging = lodgingService.listById(id).get();

        model.put("lodgings", lodging);

        return "reserve-form.html";
    }
    
//    @GetMapping("/client/mytickets")
//    public String myTickets(ModelMap model, HttpSession session) {
//        
//        Client client = (Client) session.getAttribute("sessionClient");
//        if (client == null) {
//            return "redirect:/";
//        }
//        
//        List<ClientTicket> mytickets = ctr.showMyTickets(client.getId());
//        
//        model.put("tickets", mytickets);
//        
//        return "mytickets.html";
//    }
//
//    @GetMapping("/client/ticket/{id}")//para ver un ticket en especifico
//    public String viewTicket(@PathVariable("id") String id, ModelMap model, HttpSession session) {
//
//        Client admin = (Client) session.getAttribute("sessionClient");
//        if (admin == null) {
//            return "redirect:/";
//        }
//
//        ClientTicket ct = ctr.getById(id);
//
//        if (ct != null) {
//            model.put("ticket", ct);
//        }
//
//        return "ticket.html";
//    }
//
////    @PreAuthorize("hasAnyRole('CLIENT')")
    @PostMapping("/client/book")
    public String book(ModelMap model, @RequestParam String lodgingid, @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, @RequestParam String observations, HttpSession session, RedirectAttributes redirectAttributes) throws ErrorService {
        Client client = (Client) session.getAttribute("sessionClient");
        if (client == null) {
            return "redirect:/";
        }
        
        if (client != null) {
            String mail = client.getMail();
            model.put("namelog", mail);
        }
        Optional<Lodging> ans = lR.findById(lodgingid);
        if (ans.isPresent()) {
            Lodging lodging = ans.get();
            try {
                reservationservice.saveReservation(client, startDate, endDate, lodging, observations);
                model.put("msg", "Tu reserva fue creada exitosamente.");
            } catch (ErrorService ex) {
                model.put("error",ex.getMessage());
                return "success_reservation.html";
            }
        } else {
            throw new ErrorService("No se encontró el alojamiento solicitado");
        }

        return "success_reservation.html";
    }
//
//    @GetMapping("/client/new-ticket")
//    public String newTicket(ModelMap model, HttpSession session) {
//        Client client = (Client) session.getAttribute("sessionClient");
//        if (client == null) {
//            return "redirect:/";
//        }
//        List<Reservation> reservs = rR.searchClient(client.getId());
//
//        model.addAttribute("reservs", reservs);
//
//        return "newTicket.html";
//    }
//
//    @PostMapping("/client/create-ticket")
//    public String createTicket(HttpSession session, ModelMap model, String subjet, String text, MultipartFile file, String idReservation) {
//
//        try {
//            ctS.newTicket(rR.getById(idReservation), subjet, text, file);
//        } catch (ErrorService ex) {
//            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return "ticket.html";
//    }

}
