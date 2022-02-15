package com.cumbrecita.cumbrecita.services;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.entities.Lodging;
import com.cumbrecita.cumbrecita.entities.Reservation;
import com.cumbrecita.cumbrecita.repositories.ClientRepository;
import com.cumbrecita.cumbrecita.repositories.ReservationRepository;
import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Identification;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    //CONSULTAR
    @Transactional
    public void saveReservation(Client C, Date startDate, Date endDate, Lodging L, String observations) throws ErrorService {

        Reservation reservation = new Reservation();
        reservation.setC(C);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setL(L);
        reservation.setObservations(observations);

        int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
        float price = (float) (L.getPricepernight() * days);
        reservation.setPrice(price);
        validate(reservation);

        reservationRepository.save(reservation);
    }

    public void validate(Reservation reservation) throws ErrorService {

        if (reservation.getStartDate().after(reservation.getEndDate())) {
            throw new ErrorService("La fecha de salida no puede ser anterior a la fecha de ingreso.");
        }
        if (reservation.getStartDate() == reservation.getEndDate()) {
            throw new ErrorService("Las fechas de salida e ingreso no pueden ser iguales.");
        }
        
        if (new Date().after(reservation.getStartDate())) {
            throw new ErrorService("La fecha de ingreso debe ser posterior a la fecha actual");
        }
        if (reservation.getEndDate() == null) {
            throw new ErrorService("The reservation has to have an end date.");
        }
        if (reservation.getStartDate() == null) {
            throw new ErrorService("The reservation has to have an start date.");
        }
        if (reservation.getL() == null) {
            throw new ErrorService("The reservation has to have a lodging.");
        }
        if (reservation.getPrice() == null) {
            throw new ErrorService("The reservation has to have a price.");
        }
    }

    public List<Reservation> listReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void deleteReservationById(String id) {
        Optional<Reservation> optional = reservationRepository.findById(id);
        if (optional.isPresent()) {
            reservationRepository.delete(optional.get());
        }
    }

    public Preference pay(Reservation reserve) throws MPException {
        
        Preference preference = new Preference();
        
        try {
            MercadoPago.SDK.setAccessToken("TEST-5937986277032148-101923-b35b3a307183035d2927e473185484ed-277723064");
            //TEST-56bdbc5a-5ea2-4ee3-b96a-76eb4f6ea954 PUBLIC KEY PARA EL FRONT
            // Crea un objeto de preferencia

            BackUrls backUrls = new BackUrls("localhost:8080/payment/success",
                    "localhost:8080/payment/pending",
                    "localhost:8080/payment/failure");

            Payer payer = new Payer();

            payer.setName(reserve.getC().getFirstname());
            payer.setSurname(reserve.getC().getLastname());
            payer.setEmail(reserve.getC().getMail());
            payer.setDateCreated(new Date() + "");
            payer.setIdentification(new Identification()
                    .setType("DNI")
                    .setNumber(reserve.getC().getDni() + ""));

            // Crea un ítem en la preferencia
            Item item = new Item();
            item.setTitle("Reserva de " + reserve.getL().getName())
                    .setQuantity(1)
                    .setUnitPrice((float) reserve.getPrice())
                    .setDescription(reserve.getObservations())
                    .setCurrencyId("ARS");
            preference.appendItem(item);
            preference.save();
            preference.setAutoReturn(Preference.AutoReturn.approved);
            preference.setBackUrls(backUrls);
            preference.setPayer(payer);

            return preference;
        } catch (MPConfException ex) {
            Logger.getLogger(ReservationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return preference;
    }

}
