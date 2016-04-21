package mateo.repository;

import mateo.domain.InscripcionEvento;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InscripcionEvento entity.
 */
public interface InscripcionEventoRepository extends JpaRepository<InscripcionEvento,Long> {

    @Query("select inscripcionEvento from InscripcionEvento inscripcionEvento where inscripcionEvento.user.login = ?#{principal.username}")
    List<InscripcionEvento> findByUserIsCurrentUser();

}
