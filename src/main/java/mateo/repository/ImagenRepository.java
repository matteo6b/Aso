package mateo.repository;

import mateo.domain.Imagen;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Imagen entity.
 */
public interface ImagenRepository extends JpaRepository<Imagen,Long> {

    @Query("select imagen from Imagen imagen where imagen.user.login = ?#{principal.username}")
    List<Imagen> findByUserIsCurrentUser();

}
