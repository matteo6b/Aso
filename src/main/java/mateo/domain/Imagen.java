package mateo.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Imagen.
 */
@Entity
@Table(name = "imagen")
public class Imagen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "url")
    private String url;

    @ManyToOne
    private User user;

    @ManyToOne
    private Asociacion imagenaso;

    @ManyToOne
    private Evento eventoi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Asociacion getImagenaso() {
        return imagenaso;
    }

    public void setImagenaso(Asociacion asociacion) {
        this.imagenaso = asociacion;
    }

    public Evento getEventoi() {
        return eventoi;
    }

    public void setEventoi(Evento evento) {
        this.eventoi = evento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Imagen imagen = (Imagen) o;
        if(imagen.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, imagen.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Imagen{" +
            "id=" + id +
            ", url='" + url + "'" +
            '}';
    }
}
