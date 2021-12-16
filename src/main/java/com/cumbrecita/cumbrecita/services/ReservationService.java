package com.cumbrecita.cumbrecita.services;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.entities.Lodging;
import com.cumbrecita.cumbrecita.entities.Reservation;
import com.cumbrecita.cumbrecita.repositories.ClientRepository;
import com.cumbrecita.cumbrecita.repositories.ReservationRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ClientRepository clientRepository;

    //CONSULTAR
    @Transactional
    public Reservation saveReservation(Client C, Date startDate, Date endDate, Lodging L, String observations) throws ErrorService {

        Reservation reservation = new Reservation();
        reservation.setC(C);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setL(L);
        reservation.setObservations(observations);
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
        reservation.setPrice(L.getPricepernight() * days);

        validate(reservation);

        return reservationRepository.save(reservation);
    }

    public void validate(Reservation reservation) throws ErrorService {

        if (reservation.getStartDate() == null) {
            throw new ErrorService("The reservation has to have a start date.");
        }
        if (reservation.getEndDate() == null) {
            throw new ErrorService("The reservation has to have an end date.");
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

}
