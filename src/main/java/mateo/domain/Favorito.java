package mateo.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Favorito.
 */
@Entity
@Table(name = "favorito")
public class Favorito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "likes")
    private Double likes;

    @ManyToOne
    private Asociacion asof;

    @ManyToOne
    private User user;

    @ManyToOne
    private Evento eventof;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLikes() {
        return likes;
    }

    public void setLikes(Double likes) {
        this.likes = likes;
    }

    public Asociacion getAsof() {
        return asof;
    }

    public void setAsof(Asociacion asociacion) {
        this.asof = asociacion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Evento getEventof() {
        return eventof;
    }

    public void setEventof(Evento evento) {
        this.eventof = evento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Favorito favorito = (Favorito) o;
        if(favorito.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, favorito.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Favorito{" +
            "id=" + id +
            ", likes='" + likes + "'" +
            '}';
    }
}
