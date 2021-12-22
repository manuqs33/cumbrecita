
package com.cumbrecita.cumbrecita.controllers;

import com.cumbrecita.cumbrecita.entities.Lodging;
import com.cumbrecita.cumbrecita.entities.Owner;
import com.cumbrecita.cumbrecita.entities.Photo;
import com.cumbrecita.cumbrecita.enumc.Type;
import com.cumbrecita.cumbrecita.services.ErrorService;
import com.cumbrecita.cumbrecita.services.LodgingService;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lodging")
public class LodgingController {
    
    //PARA QUIEN LEA:
    //FALTA AGREGARLE LA SEGURIDAD. 
    //REVISAR EL "RECORRIDO" DE LA PÁGINA. SI ESTÁ BIEN QUE DESPUES DE CADA ACCION SE LO REDIRIGA A LAS VISTAS MENCIONADAS.

    
    @Autowired
    LodgingService lodgingService;
    
    @GetMapping("/list") //ESTO ES PARA LISTAR LOS ALOJAMIENTOS Y QUE SE LOS PUEDA BUSCAR METIENDO UN STRING.
    public String listLodgings(Model model, @RequestParam(required = false) String q) {
        if (q != null) {
            model.addAttribute("lodgings", lodgingService.listLodgingByQ(q));
        } else {
            model.addAttribute("lodgings", lodgingService.listAllLodging());
        }
        return "lodging-list";
    }
    
    @PreAuthorize("hasAnyRole('OWNER')")
    @GetMapping("/create") //ACÁ SE METEN LOS DATOS DEL LODGING. PONGO POR LAS DUDAS LA ESTRUCTURA PARA EDITAR UN ALOJAMIENTO (Por si se equivoca en una letra, no tenga que borrar y volver a crear)
    public String createLodging(Model model, @RequestParam(required = false) String id) {
        if (id != null) {
            Optional<Lodging> optional = lodgingService.listById(id);
            if (optional.isPresent()) {
                model.addAttribute("lodgings", optional.get()); //esto manda a la vista un lodging para modificar.
            } else {
                return "redirect:/lodging/list";
            }
        } else {
            model.addAttribute("lodging", new Lodging()); //Si no vino ningun ID, esto manda un lodging nuevo.
        }
        return "lodging-form";
    }
    
    @PostMapping("/save") //ACA VIENEN LOS DATOS CUANDO EL DUEÑO GUARDA SU ALOJAMIENTO DESDE LA PAGINA.
    public String saveLodging(@RequestParam String name, @RequestParam String address, @RequestParam Type t, @RequestParam Integer capacity, @RequestParam Double pricepernight, @RequestParam Owner o, @RequestParam List<Photo> photolist, RedirectAttributes redirectAttributes) throws ErrorService {
        try {
            lodgingService.registerLodging(name, address, t, capacity, pricepernight, o, photolist);
            redirectAttributes.addFlashAttribute("success", "The lodging has been saved"); //En caso que se haya podido guardar. Manda este mensaje a la vista (se muestra con th:success)
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong"); //Igual que arriba, pero por si sale mal algo.
        }
        return "redirect:/lodging/list"; 
    }
    
    @PreAuthorize("hasAnyRole('OWNER')")
    @GetMapping("/deactivate") //ACÁ VA A VENIR PARA CUANDO TOQUE EL BOTON "DAR DE BAJA"
    public String deleteLodging(@RequestParam(required = true) String id, HttpSession session) throws ErrorService {
        Lodging lodging = lodgingService.listById(id).get();
        Owner owner = (Owner) session.getAttribute("sessionOwner");
        if (owner == null || !lodging.getO().getId().equals(owner.getId())) {
            return "redirect:/index";
        }

        lodgingService.deactivate(id);
        return "redirect:/lodging/list";
    }

    @PreAuthorize("hasAnyRole('OWNER')")
    @GetMapping("activate") //ACÁ VIENE CUANDO TOQUE EL BOTON "DAR DE ALTA"
    public String activateLodging(@RequestParam(required = true) String id, HttpSession session) throws ErrorService {
        Lodging lodging = lodgingService.listById(id).get();
        Owner owner = (Owner) session.getAttribute("sessionOwner");
        if (owner == null || !lodging.getO().getId().equals(owner.getId())) {
            return "redirect:/index";
        }
        lodgingService.activate(id);
        return "redirect:/lodging/list";
    }

    
    
    
    
    
}
