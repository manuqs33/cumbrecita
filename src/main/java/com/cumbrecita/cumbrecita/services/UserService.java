package com.cumbrecita.cumbrecita.services;

import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository uR;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = uR.findbyEmail(email);

        if (client != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p1 = new SimpleGrantedAuthority("CLIENT");
            permisos.add(p1);

            if (client.getEmail().equals("admin@admin.com")) {
                GrantedAuthority p2 = new SimpleGrantedAuthority("ADMIN");
                permisos.add(p1);
            }

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession();
            session.setAttribute("sessionClient", client);

            User user = new User(client.getEmail(), client.getPassword(), permisos);
            return user;
        } else {
            return null;
        }

    }
}
