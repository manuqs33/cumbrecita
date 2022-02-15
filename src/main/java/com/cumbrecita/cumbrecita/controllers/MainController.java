/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.entities.Owner;
import com.cumbrecita.cumbrecita.enumc.Type;
import com.cumbrecita.cumbrecita.repositories.ClientRepository;
import com.cumbrecita.cumbrecita.repositories.LodgingRepository;
import com.cumbrecita.cumbrecita.services.ClientService;
import com.cumbrecita.cumbrecita.services.EmailService;
import com.cumbrecita.cumbrecita.services.ErrorService;
import com.cumbrecita.cumbrecita.services.LodgingService;
import com.cumbrecita.cumbrecita.services.OwnerService;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private ClientRepository cR;
    @Autowired
    private ClientService clientService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private LodgingService lodgingService;
    @Autowired
    private LodgingRepository lR;

    @GetMapping("/")
    public String index(HttpSession session, ModelMap model) {
        Client client = (Client) session.getAttribute("sessionClient");
        Owner owner = (Owner) session.getAttribute("sessionOwner");

        if (owner != null) {
            String mail = owner.getMail();
            model.put("namelog", mail);
        }

        if (client != null) {
            String mail = client.getMail();
            model.put("namelog", mail);
        }

        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Correo electrónico o clave incorrectos");

        }
        return "login.html";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup.html";
    }

    @GetMapping("/owner")
    public String ownerSignup() {
        return "owner-form.html";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact.html";
    }

    @GetMapping("/lodging")
    public String lodgingForm(HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("sessionClient");
        Owner owner = (Owner) session.getAttribute("sessionOwner");

        if (owner != null) {
            String mail = owner.getMail();
            model.addAttribute("namelog", mail);
        }

        if (client != null) {
            String mail = client.getMail();
            model.addAttribute("namelog", mail);
        }
        return "lodging-form.html";
    }

    @GetMapping("/lodging-list")
    public String listLodgings(HttpSession session, Model model, @RequestParam(required = false) String q, @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer capacity, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkin, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkout) {

        
        if (q != null) {
            model.addAttribute("lodgings", lodgingService.listLodgingByQ(q));

        } else {
            model.addAttribute("lodgings", lodgingService.listAllLodging());
        }

        Client client = (Client) session.getAttribute("sessionClient");
        Owner owner = (Owner) session.getAttribute("sessionOwner");
        
        
        if (owner != null) {
            String mail = owner.getMail();
            model.addAttribute("namelog", mail);
        }

        if (client != null) {
            String mail = client.getMail();
            model.addAttribute("namelog", mail);
        }
        return "lodging-list.html";
    }

    @PostMapping("lodging-search")
    public String searchLodgings(HttpSession session, Model model, @RequestParam(required = false) String q, @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer capacity, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkin, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkout){
        
        Type t;
        if (type == 0) {
            t = Type.Casa;
            model.addAttribute("lodgings", lR.searchQuery(t, capacity));
        }else if (type == 1) {
            t = Type.Habitacion;
            model.addAttribute("lodgings", lR.searchQuery(t, capacity));
        }
        
        
        
        return "lodging-list.html";
    }
    
    @GetMapping("/faq")
    public String faq() {
        return "faq.html";
    }

    @GetMapping("/about")
    public String about(HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("sessionClient");
        Owner owner = (Owner) session.getAttribute("sessionOwner");

        if (owner != null) {
            String mail = owner.getMail();
            model.addAttribute("namelog", mail);
        }

        if (client != null) {
            String mail = client.getMail();
            model.addAttribute("namelog", mail);
        }
        return "about.html";
    }

    /*PostMapping*/
    @PostMapping("/contact/sendmessage")
    public String sendEmail(ModelMap model, String subject, String message, String email, String name) {
        try {
            String emailBody = message + "\n Email de contacto: " + email + "\n Nombre: " + name;
            emailService.send("noreply.cumbrecita@gmail.com", emailBody, subject);
        } catch (ErrorService ex) {
            model.put("error", ex.getMessage());
            return "redirect:/";
        }
        model.put("msg", "El mensaje fue enviado correctamente. Nos contactaremos lo antes posible, muchas gracias!");
        return "redirect:/";
    }

    @PostMapping("/signup")
    public String register(ModelMap model, String firstname, String lastname, Long dni, String email, String password, String password2, @DateTimeFormat(pattern = "yyyy-MM-dd") Date bdate) {

        try {
            clientService.registerClient(firstname, lastname, email, dni, bdate, password, password2);
        } catch (ErrorService e) {
            model.put("error", e.getMessage());
            model.put("fname", firstname);
            model.put("lname", lastname);
            model.put("password", password);
            model.put("password2", password2);
            model.put("email", email);
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
            return "signup.html";
        }

        String emailBody = "Tu email ha sido utilizado para registrarse en La Cumbrecita. De no haber sido tú ignora este link, de manera contraria por favor da click aqui: (hipervinculo).\n"
                + "Si no puedes ver el link puedes utilizar esta direccion URL en tu navegador: \n"
                + "localhost:8080/client/authorize/" + cR.searchByEmail(email).getId();
        try {
            emailService.send(email, emailBody, "Bienvenido a La Cumbrecita");
        } catch (ErrorService ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        model.put("title", "Bienvenido a La Cumbrecita");
        model.put("desc", "Tu usuario fue registrado de manera satisfactioria. Revisa tu casilla de correos para completar el registro.");
        return "redirect:/";
    }

    @PostMapping("/owner/signup")
    public String ownerRegister(ModelMap model, String firstname, String lastname, Long dni, String email, Long phonenumber, String password, String password2, @DateTimeFormat(pattern = "yyyy-MM-dd") Date bdate) {

        try {
            ownerService.registerOwner(firstname, lastname, email, dni, bdate, phonenumber, password, password2);
        } catch (ErrorService e) {
            model.put("error", e.getMessage());
            model.put("firstname", firstname);
            model.put("lastname", lastname);
            model.put("password", password);
            model.put("password2", password2);
            model.put("email", email);
            model.put("phonenumber", phonenumber);
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
            return "owner-form.html";
        }

        model.put("title", "Bienvenido a la Libreria Online");
        model.put("desc", "Tu usuario fue registrado de manera satisfactioria. Ahora espera a que el equipo verifique tus datos y se contacte contigo, hasta entonces mmuchas gracias!");
        return "redirect:/";
    }

}
