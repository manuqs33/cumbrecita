/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Reservation;
import com.cumbrecita.cumbrecita.repositories.ReservationRepository;
import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Address;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Identification;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Flia Vasquez
 */
@Controller
public class MercadoPagoController {

    @Autowired
    private ReservationRepository rR;

    @GetMapping("/pay")
    public String payReserve(ModelMap model, String idReservation) {

        Optional<Reservation> ans = rR.findById(idReservation);
        if (ans.isPresent()) {
            Reservation r = ans.get();
            try {
                MercadoPago.SDK.setAccessToken("APP_USR-5937986277032148-101923-5f7e275772736b2c39ac66ce485d6408-277723064");
                BackUrls backurls = new BackUrls("/payment/succes", "/payment/pending", "/payment/failure");//SE DEFINEN LAS RUTAS DE REDIRECCIONAMIENTO
                // Crea un objeto de preferencia
                Preference preference = new Preference();
                preference.setBackUrls(backurls);
                //creo el objeto pagador
                Payer payer = new Payer();
                payer.setName(r.getC().getFirstname())
                        .setSurname(r.getC().getLastname())
                        .setEmail(r.getC().getMail())
                        .setDateCreated(new Date()+"")
                        .setIdentification(new Identification()
                                .setType("DNI")
                                .setNumber(r.getC().getDni()+""));
                // Crea un ítem en la preferencia
                Item item = new Item();
                item.setTitle("Reserva en " + r.getL().getName() + " mediante 'La cumbrecita.")//NOMBRE DE LA COMPRA
                        .setQuantity(1)
                        .setUnitPrice((float) r.getPrice())//PRECIO DE LA RESERVA
                        .setCurrencyId("ARS")
                        .setDescription(r.getObservations());//OBSERVACIONES DISPUESTAS POR EL CLIENTE}
                preference.appendItem(item);
                preference.setAutoReturn(Preference.AutoReturn.approved);
                preference.save();
                model.put("preference", preference.getId());
                model.addAttribute("item", item);
            } catch (MPConfException ex) {
                Logger.getLogger(MercadoPagoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MPException ex) {
                Logger.getLogger(MercadoPagoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 

        return "pay.html";
    }

    @GetMapping("/payment/succes")
    public String sucess() {
        return "payment-success.html";
    }

    @GetMapping("/payment/pending")
    public String pending() {
        return "payment-pending.html";
    }

    @GetMapping("/payment/failure")
    public String failure() {
        return "payment-failure.html";
    }

}
