package mateo.repository;

import mateo.domain.Asociacion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Asociacion entity.
 */
public interface AsociacionRepository extends JpaRepository<Asociacion,Long> {

    @Query("select asociacion from Asociacion asociacion where asociacion.user.login = ?#{principal.username}")
    List<Asociacion> findByUserIsCurrentUser();

}
