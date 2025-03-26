package mx.ipn.escom.loginsystem.service;

import java.util.List;
import java.util.Optional;
import mx.ipn.escom.loginsystem.model.User;
import mx.ipn.escom.loginsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Guarda un usuario en la base de datos
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Obtiene todos los usuarios de la base de datos
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Busca un usuario por nombre de usuario
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}