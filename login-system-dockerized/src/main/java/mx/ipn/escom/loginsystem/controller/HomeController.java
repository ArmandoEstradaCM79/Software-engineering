package mx.ipn.escom.loginsystem.controller;

import java.util.List;
import mx.ipn.escom.loginsystem.model.User;
import mx.ipn.escom.loginsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private UserService userService; // Inyecta el servicio de usuarios

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyecta el codificador de contraseñas

    @GetMapping("/home")
    public String home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Usuario autenticado: " + auth.getName()); // Log para verificar el usuario
        System.out.println("Roles: " + auth.getAuthorities()); // Log para verificar los roles

        if (auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/homeAdmin";
        } else if (auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
            return "redirect:/homeUser";
        } else {
            return "redirect:/login"; // Si no tiene un rol válido, redirige al login
        }
    }

    @GetMapping("/homeAdmin")
    public String homeAdmin(Model model) {
        // Obtiene el usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Obtiene el nombre del usuario autenticado
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtiene la lista de todos los usuarios
        List<User> users = userService.getAllUsers();

        // Pasa el usuario, el nombre de usuario y la lista de usuarios al modelo
        model.addAttribute("user", user);
        model.addAttribute("username", username); // Pasa el nombre de usuario a la vista
        model.addAttribute("users", users);
        return "homeAdmin"; // Devuelve la vista homeAdmin.html
    }

    @GetMapping("/homeUser")
    public String homeUser(Model model) {
        // Obtiene el usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Obtiene el nombre del usuario autenticado
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Pasa el usuario y el nombre de usuario al modelo
        model.addAttribute("user", user);
        model.addAttribute("username", username); // Pasa el nombre de usuario a la vista
        return "homeUser"; // Devuelve la vista homeUser.html
    }

    @PostMapping("/updateProfile")
    public String updateProfile(
            @RequestParam String username,
            @RequestParam(required = false) String password) {
        // Obtiene el usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        User user = userService.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualiza los datos del usuario
        user.setUsername(username); // Actualiza el nombre de usuario
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password)); // Encripta la nueva contraseña
        }

        // Guarda los cambios en la base de datos
        userService.saveUser(user);

        return "redirect:/homeAdmin?success"; // Redirige con un mensaje de éxito
    }

    @PostMapping("/addUser")
    public String addUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Encripta la contraseña
        user.setRole(role);
        userService.saveUser(user);
        return "redirect:/homeAdmin?success"; // Redirige con un mensaje de éxito
    }

    @PostMapping("/editUser")
    public String editUser(
            @RequestParam Long id,
            @RequestParam String username,
            @RequestParam(required = false) String password,
            @RequestParam String role) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setUsername(username);
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password)); // Encripta la nueva contraseña
        }
        user.setRole(role);
        userService.saveUser(user);
        return "redirect:/homeAdmin?success"; // Redirige con un mensaje de éxito
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/homeAdmin?success"; // Redirige con un mensaje de éxito
    }
}