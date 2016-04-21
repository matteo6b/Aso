package mateo.repository;

import mateo.domain.Favorito;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Favorito entity.
 */
public interface FavoritoRepository extends JpaRepository<Favorito,Long> {

    @Query("select favorito from Favorito favorito where favorito.user.login = ?#{principal.username}")
    List<Favorito> findByUserIsCurrentUser();

}
