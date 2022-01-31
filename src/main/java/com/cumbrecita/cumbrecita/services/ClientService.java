package com.cumbrecita.cumbrecita.services;

import com.cumbrecita.cumbrecita.entities.Client;
import com.cumbrecita.cumbrecita.entities.Owner;
import com.cumbrecita.cumbrecita.repositories.ClientRepository;
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
public class ClientService implements UserDetailsService {

    @Autowired
    private ClientRepository uR;
    @Autowired
    private OwnerRepository oR;

    @Transactional
    public void registerClient(String firstname, String lastname, String email, Long dni, Date bdate, String password, String password2) throws ErrorService {
        validate(firstname, lastname, email, password, password2, dni);
        validateEmail(email);

        Client client = new Client();

        client.setFirstname(firstname);
        client.setLastname(lastname);
        client.setMail(email);
        client.setDni(dni);
        client.setBirthdate(bdate);

        String encriptada = new BCryptPasswordEncoder().encode(password);
        client.setPass(encriptada);

        client.setIsactive(true);

        uR.save(client);
    }

    @Transactional
    public void modify(String id, String firstname, String lastname, String email, Long dni, Date bdate, String password, String password2) throws ErrorService {

        validate(firstname, lastname, email, password, password2, dni);

        Optional<Client> ans = uR.findById(id);
        if (ans.isPresent()) {
            Client client = ans.get();

            client.setFirstname(firstname);
            client.setLastname(lastname);
            client.setMail(email);
            client.setDni(dni);
            client.setBirthdate(bdate);

            String encriptada = new BCryptPasswordEncoder().encode(password);
            client.setPass(encriptada);
            uR.save(client);
        } else {
            throw new ErrorService("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void changePassword(String id, String password, String password2) throws ErrorService { //OLVIDE LA CONTRASENA
        if (password.isEmpty() || password.equals(" ")) {
            throw new ErrorService("La contraseña no puede estar vacia");
        } else if (password.equals(password2)) {
            throw new ErrorService("Las contraseñas no coinciden");
        }

        Optional<Client> ans = uR.findById(id);
        if (ans.isPresent()) {
            Client client = ans.get();

            String encriptada = new BCryptPasswordEncoder().encode(password);
            client.setPass(encriptada);

            uR.save(client);
        } else {
            throw new ErrorService("No se encontro el usuario solicitado");
        }

    }

    @Transactional
    public void activeClient(String id) throws ErrorService {

        Optional<Client> ans = uR.findById(id);
        if (ans.isPresent()) {
            Client client = ans.get();
            client.setIsactive(true);
            uR.save(client);
        } else {
            throw new ErrorService("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void deactivate(String id) throws ErrorService {

        Optional<Client> ans = uR.findById(id);
        if (ans.isPresent()) {
            Client client = ans.get();
            client.setIsactive(false);
            uR.save(client);
        } else {
            throw new ErrorService("No se encontro el usuario solicitado");
        }
    }

    public List<Client> listClient() {
        return uR.findAll();
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

    public void validateEmail(String email) throws ErrorService {
        List<Client> clients = uR.findAll();
        List<Owner> owners = oR.findAll();

        for (Owner owner : owners) {
            if (owner.getMail().equals(email)) {
                throw new ErrorService("El email ingresado ya esta en uso");
            }
        }
        for (Client client : clients) {
            if (client.getMail().equals(email)) {
                throw new ErrorService("El email ingresado ya esta en uso");
            }
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = uR.searchByEmail(email);
        Owner owner = oR.searchByEmail(email);

        if (owner != null) {
            List<GrantedAuthority> permisos = new ArrayList();

            if (!owner.getIsactive()) {
                return null;
            }

            GrantedAuthority p1 = new SimpleGrantedAuthority("OWNER");
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession();
            session.setAttribute("sessionOwner", owner);

            User user = new User(owner.getMail(), owner.getPass(), permisos);
            return user;
        }

        if (!client.getIsactive()) {
            return null;
        }

        if (client != null) {

            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p1 = new SimpleGrantedAuthority("CLIENT");
            permisos.add(p1);

            if (client.getMail().equals("admin@admin.com")) {
                GrantedAuthority p2 = new SimpleGrantedAuthority("ADMIN");
                permisos.add(p2);
            }

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession();
            session.setAttribute("sessionClient", client);

            User user = new User(client.getMail(), client.getPass(), permisos);
            return user;
        } else {
            return null;
        }

    }
}
