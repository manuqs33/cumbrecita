/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.repositories.ClientRepository;
import com.cumbrecita.cumbrecita.services.ClientService;
import com.cumbrecita.cumbrecita.services.EmailService;
import com.cumbrecita.cumbrecita.services.ErrorService;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
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
    private EmailService emailService;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {
        if (error != null) {
            model.put("error", "Correo electrocnico o clave incorrectos");
            return "error.html";
        }
        return "login.html";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup.html";
    }

    @PostMapping("/signup")
    public String register(ModelMap model, String fname, String lname, Long dni, String email, String password, String password2, @DateTimeFormat(pattern = "yyyy-MM-dd") Date bdate) {

        try {
            clientService.registerClient(fname, lname, email, dni, bdate, password, password2);
        } catch (ErrorService e) {
            model.put("error", e.getMessage());
            model.put("fname", fname);
            model.put("lname", lname);
            model.put("password", password);
            model.put("password2", password2);
            model.put("email", email);
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
            return "signup.html";
        }

        String emailBody = "Tu email ha sido utilizado para registrarse en La Cumbrecita. De no haber sido tú ignora este link, de manera contraria por favor da click aqui: (hipervinculo).\n"
                + "Si no puedes ver el link puedes utilizar esta direccion URL en tu navegador: \n"
                + "localhost:8080/client/autorize/" + cR.searchByEmail(email).getId();

        emailService.send(email, emailBody, "Bienvenido a La Cumbrecita");

        model.put("title", "Bienvenido a la Libreria Online");
        model.put("desc", "Tu usuario fue registrado de manera satisfactioria. Revisa tu casilla de correos para completar el registro.");
        return "succes.html";
    }

}
