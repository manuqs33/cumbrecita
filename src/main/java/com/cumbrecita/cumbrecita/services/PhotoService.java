package com.cumbrecita.cumbrecita.services;

import com.cumbrecita.cumbrecita.entities.Photo;
import com.cumbrecita.cumbrecita.repositories.PhotoRepository;
import java.io.IOException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository pR;

    @Transactional
    public Photo save(MultipartFile file) throws ErrorService {

        if (file != null) {
            try {
                Photo photo = new Photo();
                photo.setName(file.getName());
                photo.setMime(file.getContentType());
                photo.setContain(file.getBytes());

                return pR.save(photo);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    @Transactional
    public Photo edit(String idPhoto, MultipartFile file) throws ErrorService {

        if (file != null) {
            try {
                Photo photo = new Photo();

                if (idPhoto != null) {
                    Optional<Photo> ans = pR.findById(idPhoto);
                    if (ans.isPresent()) {
                        photo = ans.get();
                    }
                }
                photo.setName(file.getName());
                photo.setMime(file.getContentType());
                photo.setContain(file.getBytes());

                return pR.save(photo);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Transactional
    public void delete(String idPhoto) throws ErrorService {

        if (idPhoto != null) {
            Optional<Photo> ans = pR.findById(idPhoto);
            if (ans.isPresent()) {
                Photo photo = ans.get();
                pR.delete(photo);
            } else {
                throw new ErrorService("No se encontró el archivo solicitado");
            }
        }
    }
}
