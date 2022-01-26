/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.services;

import com.cumbrecita.cumbrecita.entities.Owner;
import com.cumbrecita.cumbrecita.repositories.OwnerRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository oR;

    @Transactional
    public void registerOwner(String firstname, String lastname, String email, Long dni, Date bdate, Long phoneNumber, String password, String password2) throws ErrorService {
        validate(firstname, lastname, email, password, password2, dni);

        Owner owner = new Owner();

        owner.setFirstname(firstname);
        owner.setLastname(lastname);
        owner.setMail(email);
        owner.setDni(dni);
        owner.setBirthdate(bdate);
        owner.setPhoneNumber(phoneNumber);

        String encriptada = new BCryptPasswordEncoder().encode(password);
        owner.setPass(encriptada);

        owner.setIsactive(true);

        oR.save(owner);
    }

    @Transactional
    public void modify(String id, String firstname, String lastname, String email, Long dni, Date bdate, String password, String password2) throws ErrorService {

        validate(firstname, lastname, email, password, password2, dni);

        Optional<Owner> ans = oR.findById(id);
        if (ans.isPresent()) {
            Owner owner = ans.get();

            owner.setFirstname(firstname);
            owner.setLastname(lastname);
            owner.setMail(email);
            owner.setDni(dni);
            owner.setBirthdate(bdate);

            String encriptada = new BCryptPasswordEncoder().encode(password);
            owner.setPass(encriptada);
            oR.save(owner);

        } else {
            throw new ErrorService("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void activeOwner(String id) throws ErrorService {

        Optional<Owner> ans = oR.findById(id);
        if (ans.isPresent()) {
            Owner owner = ans.get();
            owner.setIsactive(true);
            oR.save(owner);
        } else {
            throw new ErrorService("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void deactivate(String id) throws ErrorService {

        Optional<Owner> ans = oR.findById(id);
        if (ans.isPresent()) {
            Owner owner = ans.get();
            owner.setIsactive(false);
            oR.save(owner);
        } else {
            throw new ErrorService("No se encontro el usuario solicitado");
        }
    }

    public List<Owner> listOwners() {
        return oR.findAll();
    }

    public void validate(String fistname, String lastname, String email, String password, String password2, Long dni) throws ErrorService {
        if (fistname == null || fistname.isEmpty()) {
            throw new ErrorService("El nombre del usuario no puede estar vacio");
        }
        if (lastname == null || lastname.isEmpty()) {
            throw new ErrorService("El apellido del usuario no puede estar vacio");
        }
        if (email == null || email.isEmpty()) {
            throw new ErrorService("El email no puede estar vacio");
        }
        if (password == null || password.isEmpty() || password.length() <= 6) {
            throw new ErrorService("La contraseña no puede estar vacia y debe tener mas de 6 caracteres");
        }
        if (!password.equals(password2)) {
            throw new ErrorService("Las contraseñas no coinciden");
        }
        if (dni == 0 || dni < 1000000 || dni > 100000000) {
            throw new ErrorService("El dni es incorrecto");
        }
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Owner owner = oR.searchByEmail(email);
//
//        if (owner != null) {
//
//            List<GrantedAuthority> permisos = new ArrayList();
//
//            GrantedAuthority p1 = new SimpleGrantedAuthority("OWNER");
//            permisos.add(p1);
//
//            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//            HttpSession session = attr.getRequest().getSession();
//            session.setAttribute("sessionOwner", owner);
//
//            User user = new User(owner.getMail(), owner.getPass(), permisos);
//            return user;
//        } else {
//            return null;
//        }
//
//    }
}
