package com.cumbrecita.cumbrecita.services;

import com.cumbrecita.cumbrecita.enumc.Type;
import com.cumbrecita.cumbrecita.entities.Lodging;
import com.cumbrecita.cumbrecita.entities.Owner;
import com.cumbrecita.cumbrecita.entities.Photo;
import com.cumbrecita.cumbrecita.repositories.LodgingRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LodgingService {

    @Autowired
    private LodgingRepository lR;
    @Autowired
    private PhotoService photoService;

    @Transactional
    public void registerLodging(String name, String address, Integer t, Integer capacity, Double pricepernight, String desc, Owner o, List<MultipartFile> photolist) throws ErrorService {
        validate(name, address, t, capacity, pricepernight, desc);
        validateOwner(o);

        Lodging lodging = new Lodging();
        lodging.setName(name);
        lodging.setAddress(address);

        if (t == 0 || t < 1) {
            lodging.setT(Type.Casa);
        } else if (t == 1 || t > 0) {
            lodging.setT(Type.Habitacion);
        }

        lodging.setCapacity(capacity);
        lodging.setPricepernight(pricepernight);
        lodging.setDescription(desc);
        lodging.setO(o);

        List<Photo> photos = new ArrayList();
        for (MultipartFile multipartFile : photolist) {
            Photo photo = photoService.save(multipartFile);
            photos.add(photo);
        }

        lodging.setPhotolist(photos);
        lodging.setIsactive(true);
        lR.save(lodging);
    }

    @Transactional
    public void modify(String id, String name, String address, Integer t, Integer capacity, Double pricepernight,String desc, List<Photo> photolist) throws ErrorService {
        validate(name, address, t, capacity, pricepernight, desc);
        Optional<Lodging> ans = lR.findById(id);
        if (ans.isPresent()) {
            Lodging lodging = ans.get();
            lodging.setName(name);
            lodging.setAddress(address);
            if (t == 0 || t < 1) {
            } else if (t == 1 || t > 0) {
                lodging.setT(Type.Casa);
            } else if (t == 1 || t > 0) {
                lodging.setT(Type.Habitacion);
            }
            lodging.setCapacity(capacity);
            lodging.setPricepernight(pricepernight);
            lodging.setDescription(desc);
            lodging.setPhotolist(photolist);
        } else {
            throw new ErrorService("No se encontró el alojamiento solicitado");
        }
    }

    public void validate(String name, String address, Integer t, Integer capacity, Double pricepernight, String desc) throws ErrorService {
        if (name == null || name.isEmpty()) {
            throw new ErrorService("El nombre del alojamiento no puede estar vacío ni ser nulo");
        }
        if (address == null || address.isEmpty()) {
            throw new ErrorService("La dirección del alojamiento no puede estar vacía ni ser nula");
        }
        if (t == null) {
            throw new ErrorService("El tipo de alojamiento no puede ser nulo");
        }
        if (capacity == null) {
            throw new ErrorService("La capacidad del alojamiento no puede ser nula");
        }
        if (pricepernight == 0 || pricepernight == null) {
            throw new ErrorService("El precio por noche del alojamiento no puede estar vacío ni ser nulo");
        }
    }

    public void validateOwner(Owner o) throws ErrorService {
        if (o == null) {
            throw new ErrorService("El propietario no puede estar vacío");
        }
    }

    @Transactional
    public void activate(String id) throws ErrorService {

        Optional<Lodging> ans = lR.findById(id);
        if (ans.isPresent()) {
            Lodging lodging = ans.get();
            lodging.setIsactive(true);
            lR.save(lodging);
        } else {
            throw new ErrorService("No se encontró el alojamiento solicitado");
        }
    }

    @Transactional
    public void deactivate(String id) throws ErrorService {
        Optional<Lodging> ans = lR.findById(id);
        if (ans.isPresent()) {
            Lodging lodging = ans.get();
            lodging.setIsactive(false);
            lR.save(lodging);
        } else {
            throw new ErrorService("No se encontró el alojamiento solicitado");
        }
    }

    public List<Lodging> listLodgingByQ(String q) {
        return lR.findByQ("%" + q + "%");
    }
    public List<Lodging> listAllLodging() {
        return lR.findAll();
    }

    public Optional<Lodging> listById(String id) {
        return lR.findById(id);
    }
}
