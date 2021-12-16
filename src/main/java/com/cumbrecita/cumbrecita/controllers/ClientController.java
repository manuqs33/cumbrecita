/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.repositories.ClientRepository;
import com.cumbrecita.cumbrecita.services.ClientService;
import com.cumbrecita.cumbrecita.services.ErrorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/client/authorize/{id}")
    public String authorize(@PathParam("id") String id, ModelMap model) {

        try {
            clientService.activeClient(id);
        } catch (ErrorService ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            model.put("error", ex.getMessage());
            return "succes.html";
        }
        
        model.put("title","Tu usuario fue autorizado correctamente");
        return "succes.html";
    }

}
