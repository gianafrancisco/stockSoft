package yporque.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yporque.model.Retiro;

import java.time.Instant;
import java.util.List;

/**
 * Created by francisco on 30/12/15.
 */
public interface RetiroRepository extends JpaRepository<Retiro, Long> {
    List<Retiro> findByFechaBetween(Instant startTime, Instant stopTime);

    Page<Retiro> findByFechaBetween(Instant startTime, Instant stopTime, Pageable pageable);
}
