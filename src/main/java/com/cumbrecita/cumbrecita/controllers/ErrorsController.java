/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.controllers;

import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Joaquin
 */
@Controller
public class ErrorsController implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public String renderErrorPage(HttpServletRequest httpRequest) {

        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                return "400.html";
            }
            case 403: {
                return "403.html";
            }
            case 401: {
                return "401.html";
            }
            case 404: {
                return "404.html";
            }
            case 500: {
                return "500.html";
            }
        }

        return null;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        Map mapa = httpRequest.getParameterMap();
        for (Object key : mapa.keySet()) {
            String[] valores = (String[]) mapa.get(key);
            for (String valor : valores) {
                System.out.println(key.toString() + " : " + valor);
            }
        }
        Enumeration<String> atributos = httpRequest.getAttributeNames();
        while (atributos.hasMoreElements()) {
            String key = atributos.nextElement();
            System.out.println(key + " : " + httpRequest.getAttribute(key));
        }
        return (int) httpRequest.getAttribute("javax.servlet.error.status_code");
    }

    public String getErrorPath() {
        return "/error";
    }

}
