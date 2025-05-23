package mx.ipn.escom.loginsystem.repository;

import java.util.List;
import java.util.Map;
import mx.ipn.escom.loginsystem.model.User;
import mx.ipn.escom.loginsystem.model.FavoriteBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {
    List<FavoriteBook> findByUser(User user);
    boolean existsByUserAndGoogleBookId(User user, String googleBookId);
}
