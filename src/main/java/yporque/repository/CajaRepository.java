package yporque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yporque.model.Caja;

import java.time.Instant;
import java.util.List;

/**
 * Created by francisco on 30/12/15.
 */
public interface CajaRepository extends JpaRepository<Caja, Long> {
    List<Caja> findByCierre(Instant cierre);
}
