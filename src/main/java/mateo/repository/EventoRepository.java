package mateo.repository;

import mateo.domain.Evento;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Evento entity.
 */
public interface EventoRepository extends JpaRepository<Evento,Long> {

}
