package mateo.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A InscripcionEvento.
 */
@Entity
@Table(name = "inscripcion_evento")
public class InscripcionEvento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "entrada")
    private Double entrada;

    @Column(name = "fechains")
    private ZonedDateTime fechains;

    @ManyToOne
    private Evento evento;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getEntrada() {
        return entrada;
    }

    public void setEntrada(Double entrada) {
        this.entrada = entrada;
    }

    public ZonedDateTime getFechains() {
        return fechains;
    }

    public void setFechains(ZonedDateTime fechains) {
        this.fechains = fechains;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InscripcionEvento inscripcionEvento = (InscripcionEvento) o;
        if(inscripcionEvento.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, inscripcionEvento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InscripcionEvento{" +
            "id=" + id +
            ", entrada='" + entrada + "'" +
            ", fechains='" + fechains + "'" +
            '}';
    }
}
