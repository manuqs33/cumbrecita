/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.entities.Owner;
import com.cumbrecita.cumbrecita.repositories.ClientRepository;
import com.cumbrecita.cumbrecita.services.ClientService;
import com.cumbrecita.cumbrecita.services.EmailService;
import com.cumbrecita.cumbrecita.services.ErrorService;
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

    @GetMapping("/")
    public String index(HttpSession session, ModelMap model) {
        Client client = (Client) session.getAttribute("sessionClient");
        Owner owner = (Owner) session.getAttribute("sessionOwner");

        if (owner != null) {
            model.put("namelog", owner.getFirstname());
        }
        if (client != null) {
            model.put("namelog", client.getFirstname());
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
    public String lodgingForm(){
        return "lodging-form.html";
    }
    
    @GetMapping("/lodging-list")
    public String lodgingList(){
        return "lodging-list.html";
    }
    
     @GetMapping("/faq")
    public String faq() {
        return "faq.html";
    }
    
     @GetMapping("/about")
    public String about() {
        return "about.html";
    }
    
    
    

    /*PostMapping*/
    @PostMapping("/contact/sendmessage")
    public String sendEmail(ModelMap model, String subject, String message, String email, String phoneNumber) {
        try {
            String emailBody = message + "\n Email de contacto: " + email + "\n Numero celular de contacto: " + phoneNumber;
            emailService.send(email, emailBody, subject);
        } catch (ErrorService ex) {
            model.put("error", ex.getMessage());
            return "contact.html";
        }

        model.put("message", "El mensaje fue enviado correctamente. Nos contactaremos lo antes posible, muchas gracias!");
        return "contact.html";
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

        model.put("title", "Bienvenido a la Libreria Online");
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
