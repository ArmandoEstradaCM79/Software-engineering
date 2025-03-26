package mx.ipn.escom.loginsystem.controller;

import mx.ipn.escom.loginsystem.model.User;
import mx.ipn.escom.loginsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyecta el PasswordEncoder

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encripta la contraseña
        userService.saveUser(user);
        return "redirect:/login";
    }
}