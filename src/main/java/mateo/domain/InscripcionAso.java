package mateo.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A InscripcionAso.
 */
@Entity
@Table(name = "inscripcion_aso")
public class InscripcionAso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "validar")
    private Boolean validar;

    @Column(name = "fecha_inscrito")
    private ZonedDateTime fechaInscrito;

    @ManyToOne
    private Asociacion asociacion;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
    }

    public ZonedDateTime getFechaInscrito() {
        return fechaInscrito;
    }

    public void setFechaInscrito(ZonedDateTime fechaInscrito) {
        this.fechaInscrito = fechaInscrito;
    }

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
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
        InscripcionAso inscripcionAso = (InscripcionAso) o;
        if(inscripcionAso.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, inscripcionAso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InscripcionAso{" +
            "id=" + id +
            ", validar='" + validar + "'" +
            ", fechaInscrito='" + fechaInscrito + "'" +
            '}';
    }
}
