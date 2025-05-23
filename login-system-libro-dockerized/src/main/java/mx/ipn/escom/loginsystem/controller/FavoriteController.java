package mx.ipn.escom.loginsystem.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import mx.ipn.escom.loginsystem.model.User;
import mx.ipn.escom.loginsystem.repository.UserRepository;
import mx.ipn.escom.loginsystem.repository.FavoriteBookRepository;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import mx.ipn.escom.loginsystem.model.FavoriteBook;

@Controller
public class FavoriteController {

    @Autowired
    private FavoriteBookRepository favoriteRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/addFavorite")
    @ResponseBody
    public ResponseEntity<?> addFavorite(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Map<String, String> data) {
        try {
            Optional<User> optionalUser = userRepo.findByUsername(userDetails.getUsername());
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }
            User user = optionalUser.get();

            String googleBookId = data.get("id");

            if (favoriteRepo.existsByUserAndGoogleBookId(user, googleBookId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya en favoritos");
            }

            FavoriteBook fav = new FavoriteBook();
            fav.setUser(user);
            fav.setGoogleBookId(googleBookId);
            fav.setTitle(data.get("title"));
            fav.setAuthors(data.get("authors"));
            fav.setThumbnail(data.get("thumbnail"));
            fav.setInfoLink(data.get("infoLink"));

            favoriteRepo.save(fav);
            return ResponseEntity.ok("Añadido a favoritos");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al añadir a favoritos: " + e.getMessage());
        }
    }

    @GetMapping("/favorites")
    public String viewFavorites(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userRepo.findByUsername(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            return "error-page"; 
        }

        User user = userOptional.get();
        List<FavoriteBook> favorites = favoriteRepo.findByUser(user);
        model.addAttribute("favorites", favorites);
        return "admin-home";
    }

    // Nuevo endpoint para obtener la lista de favoritos en JSON
    @GetMapping("/api/favorites")
    @ResponseBody
    public List<FavoriteBook> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userRepo.findByUsername(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado");
        }
        User user = userOptional.get();
        return favoriteRepo.findByUser(user);
    }

    @DeleteMapping("/removeFavorite/{id}")
    @ResponseBody
    public ResponseEntity<?> removeFavorite(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        Optional<User> optionalUser = userRepo.findByUsername(userDetails.getUsername());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }
        User user = optionalUser.get();

        Optional<FavoriteBook> favOptional = favoriteRepo.findById(id);
        if (favOptional.isEmpty() || !favOptional.get().getUser().equals(user)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorito no encontrado");
        }

        favoriteRepo.delete(favOptional.get());
        return ResponseEntity.ok("Eliminado de favoritos");
    }

}
