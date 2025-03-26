package mx.ipn.escom.loginsystem.repository;

import java.util.Optional;
import mx.ipn.escom.loginsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}