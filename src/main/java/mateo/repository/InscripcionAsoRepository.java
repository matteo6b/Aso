package mateo.repository;

import mateo.domain.InscripcionAso;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InscripcionAso entity.
 */
public interface InscripcionAsoRepository extends JpaRepository<InscripcionAso,Long> {

    @Query("select inscripcionAso from InscripcionAso inscripcionAso where inscripcionAso.user.login = ?#{principal.username}")
    List<InscripcionAso> findByUserIsCurrentUser();

}
