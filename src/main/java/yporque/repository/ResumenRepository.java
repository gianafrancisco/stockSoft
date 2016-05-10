package yporque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yporque.model.Resumen;

import java.time.Instant;
import java.util.List;

/**
 * Created by francisco on 20/01/16.
 */
public interface ResumenRepository extends JpaRepository<Resumen, Long> {
    List<Resumen> findByFechaBetween(Instant apertura, Instant cierre);
}
