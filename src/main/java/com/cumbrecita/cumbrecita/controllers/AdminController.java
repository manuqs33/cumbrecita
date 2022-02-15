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
import com.cumbrecita.cumbrecita.services.ClientTicketService;
import com.cumbrecita.cumbrecita.services.ErrorService;
import com.cumbrecita.cumbrecita.services.LodgingService;
import com.cumbrecita.cumbrecita.services.OwnerService;
import com.cumbrecita.cumbrecita.services.OwnerTicketService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Flia Vasquez
 */
@Controller
//@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminController {

    @Autowired
    private ClientService adminService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private LodgingService lodgingService;
    @Autowired
    private ClientRepository cR;
    @Autowired
    private OwnerRepository oR;
    @Autowired
    private LodgingRepository lR;

    @Autowired
    private ClientTicketRepository ctr;
    @Autowired
    private ClientTicketService ctS;
    @Autowired
    private OwnerTicketService otS;
    @Autowired
    private OwnerTicketRepository otr;
    @Autowired
    private TicketAnswerRepository tar;

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

    @GetMapping("/tickets")//para ver todos los tickets
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

        return "ticket-list.html";
    }

    @GetMapping("/ticket/{id}")//para ver un ticket en especifico
    public String viewTicket(@PathVariable("id") String id, ModelMap model, HttpSession session) {

        Client admin = (Client) session.getAttribute("sessionClient");
        if (admin == null || !admin.getMail().equals("admin@admin.com")) {
            return "redirect:/";
        }

        ClientTicket ct = ctr.getById(id);
        OwnerTicket ot = otr.getById(id);

        if (ct != null) {
            model.put("ticket", ct);
        }
        if (ot != null) {
            model.put("ticket", ct);
        }

        return "ticket.html";
    }

    /*POST MAPPINGS*/
    @PostMapping("/answer-ticket")//para responder el ticket
    public String answerTicket(@RequestParam String idTicket, @RequestParam String content, @RequestParam(required = false) MultipartFile file, ModelMap model, HttpSession session) {
        Client admin = (Client) session.getAttribute("sessionClient");
        if (admin != null) {
            try {
                ctS.answerTicket(content, file, idTicket);
            } catch (ErrorService ex) {
                model.put("msg", ex.getMessage());
                return "ticket.hmtl";
            }
        }

        Owner owner = (Owner) session.getAttribute("sessionOWner");
        if (owner != null) {
            try {
                otS.answerTicket(content, file, idTicket);
            } catch (ErrorService ex) {
                model.put("msg", ex.getMessage());
                return "ticket.hmtl";
            }
        }

        return "redirect:/ticket/";
    }

    @PostMapping("/tickets/deactivate")//dar de baja un ticket
    public String deactivateTicket(@RequestParam String idTicket) {

        try {
            ctS.closeTicket(idTicket);
            otS.closeTicket(idTicket);
        } catch (ErrorService ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/tickets";
    }

    @PostMapping("/deactivate-client")
    public String deactivateClient(@RequestParam String iduser) {
        try {
            adminService.deactivate(iduser);
        } catch (ErrorService e) {
            return "500.html";
        }
        return "redirect:/control-panel";
    }

    @PostMapping("/activate-client")
    public String activateClient(@RequestParam String iduser) {
        try {
            adminService.activeClient(iduser);
        } catch (ErrorService e) {
            return "500.html";
        }
        return "redirect:/control-panel";
    }

    @PostMapping("/deactivate-owner")
    public String deactivateOwner(@RequestParam String idowner) {
        try {
            ownerService.deactivate(idowner);
        } catch (ErrorService e) {
            return "500.html";
        }
        return "redirect:/control-panel";
    }

    @PostMapping("/activate-owner")
    public String activateOwner(@RequestParam String idowner) {
        try {
            ownerService.activeOwner(idowner);
        } catch (ErrorService e) {
            return "500.html";
        }
        return "redirect:/control-panel";
    }

    @PostMapping("/deactivate-lodging")
    public String deactivateLodging(@RequestParam String idlod) {
        try {
            lodgingService.deactivate(idlod);
        } catch (ErrorService e) {
            return "500.html";
        }
        return "redirect:/control-panel";
    }

    @PostMapping("/activate-lodging")
    public String activateLodging(@RequestParam String idlod) {
        try {
            lodgingService.activate(idlod);
        } catch (ErrorService e) {
            return "500.html";
        }
        return "redirect:/control-panel";
    }
}
