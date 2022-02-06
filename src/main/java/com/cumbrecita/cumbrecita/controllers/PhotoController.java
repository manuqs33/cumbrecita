/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Lodging;
import com.cumbrecita.cumbrecita.repositories.LodgingRepository;
import com.cumbrecita.cumbrecita.services.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author sara
 */
@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private LodgingRepository lR;
    
    @GetMapping("/lodging/{id}")
    public ResponseEntity<byte[]> lodgingPhotos(@PathVariable String id) throws ErrorService {

        Lodging lod = lR.getById(id);

        if (lod.getPhotolist() == null) {
            throw new ErrorService("El alojamiento no tiene una foto");
        }

        byte[] photo = lod.getPhotolist().get(0).getContain();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(photo, headers, HttpStatus.OK);
    }

}
