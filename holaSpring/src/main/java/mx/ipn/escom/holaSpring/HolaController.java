package mx.ipn.escom.holaspring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HolaController {

    @GetMapping("/hola")
    public String hola() {
        return "hola"; // Esto devolverá el nombre de la plantilla HTML
    }
}