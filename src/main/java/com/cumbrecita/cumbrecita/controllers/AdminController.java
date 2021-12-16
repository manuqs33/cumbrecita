/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.entities.ClientTicket;
import com.cumbrecita.cumbrecita.entities.Lodging;
import com.cumbrecita.cumbrecita.entities.Owner;
import com.cumbrecita.cumbrecita.entities.OwnerTicket;
import com.cumbrecita.cumbrecita.repositories.ClientRepository;
import com.cumbrecita.cumbrecita.repositories.ClientTicketRepository;
import com.cumbrecita.cumbrecita.repositories.LodgingRepository;
import com.cumbrecita.cumbrecita.repositories.OwnerRepository;
import com.cumbrecita.cumbrecita.repositories.OwnerTicketRepository;
import com.cumbrecita.cumbrecita.repositories.TicketAnswerRepository;
import com.cumbrecita.cumbrecita.services.ClientService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Flia Vasquez
 */
@Controller
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminController {

    @Autowired
    private ClientService adminService;
    @Autowired
    private ClientRepository cR;
    @Autowired
    private OwnerRepository oR;
    @Autowired
    private LodgingRepository lR;

    @Autowired
    private ClientTicketRepository ctr;
    @Autowired
    private OwnerTicketRepository otr;
    @Autowired
    private TicketAnswerRepository tcr;
    
    
    @GetMapping("/control-panel")
    public String panel(ModelMap model, HttpSession session) {

        Client admin = (Client) session.getAttribute("sessionClient");
        if (admin == null || !admin.getMail().equals("admin@admin.com")) {
            return "redirect:/";
        }

        List<Client> clients = cR.findAll();
        List<Owner> owners = oR.findAll();
        List<Lodging> lods = lR.findAll();

        model.put("users", clients);
        model.put("owners", owners);
        model.put("lods", lods);

        return "control-panel.html";
    }

    @GetMapping("/tickets")
    public String tickets(ModelMap model, HttpSession session) {
        
        Client admin = (Client) session.getAttribute("sessionClient");
        if (admin == null || !admin.getMail().equals("admin@admin.com")) {
            return "redirect:/";
        }
        
        List<OwnerTicket> oTickets = otr.findAll();
        List<ClientTicket> cTickets = ctr.findAll();
        
        for (ClientTicket cTicket : cTickets) {
            if (!cTicket.getIsactive()) {
                cTickets.remove(cTicket);
            }
        }
        
        for (OwnerTicket oTicket : oTickets) {
            if (!oTicket.getIsactive()) {
                oTickets.remove(oTicket);
            }
        }
        
        model.put("ownertickets", oTickets);
        model.put("clientTickets", cTickets);
        
        return "tickets.tml";
    }

}
