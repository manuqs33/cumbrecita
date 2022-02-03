/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.entities.Lodging;
import com.cumbrecita.cumbrecita.enumc.Type;
import com.cumbrecita.cumbrecita.repositories.ClientRepository;
import com.cumbrecita.cumbrecita.repositories.LodgingRepository;
import com.cumbrecita.cumbrecita.services.ClientService;
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
    private LodgingRepository lR;
    @Autowired
    private ReservationService reservationservice;
    @Autowired
    private LodgingService lodgingService;

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
        return "success.html";
    }
    
    
    @GetMapping("/reserve")
    public String reserve(ModelMap model,@RequestParam String id){
        
        Lodging lodging = lodgingService.listById(id).get();
        
        model.put("lodgings", lodging);
        
        return "reserve-form.html";
    }
    
//    @PreAuthorize("hasAnyRole('CLIENT')")
    @PostMapping("/client/book")
    public String book(ModelMap model,@RequestParam String lodgingid,@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, @RequestParam String observations, HttpSession session, RedirectAttributes redirectAttributes) throws ErrorService {
        Client client = (Client) session.getAttribute("sessionClient");
        if (client == null) {
            return "redirect:/";
        }
        Optional<Lodging> ans = lR.findById(lodgingid);
        if (ans.isPresent()) {
            Lodging lodging = ans.get();
            lodging.setT(Type.house);
            try {
                reservationservice.saveReservation(client, startDate, endDate, lodging, observations);
                redirectAttributes.addFlashAttribute("success", "Su reserva ha sido exitosa");
                model.put("msg", "Reservaste con exito");
            } catch (ErrorService ex) {
                redirectAttributes.addFlashAttribute("error", "No se pudo guardar la reserva");
                model.put("msg", "Hubo un error en la reserva");
            }
        } else {
            throw new ErrorService("No se encontró el alojamiento solicitado");
        }

        
        return "success_reservation.html";
    }
    
}
