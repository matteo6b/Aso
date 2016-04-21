package mateo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Evento.
 */
@Entity
@Table(name = "evento")
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fecha")
    private ZonedDateTime fecha;

    @Column(name = "street_adress")
    private String streetAdress;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "entrada")
    private Integer entrada;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "evento")
    @JsonIgnore
    private Set<InscripcionEvento> eventos = new HashSet<>();

    @OneToMany(mappedBy = "eventoi")
    @JsonIgnore
    private Set<Imagen> imagens = new HashSet<>();

    @OneToMany(mappedBy = "eventof")
    @JsonIgnore
    private Set<Favorito> favoritos = new HashSet<>();

    @ManyToOne
    private Asociacion asociacionevent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ZonedDateTime getFecha() {
        return fecha;
    }

    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    public String getStreetAdress() {
        return streetAdress;
    }

    public void setStreetAdress(String streetAdress) {
        this.streetAdress = streetAdress;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Integer getEntrada() {
        return entrada;
    }

    public void setEntrada(Integer entrada) {
        this.entrada = entrada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<InscripcionEvento> getEventos() {
        return eventos;
    }

    public void setEventos(Set<InscripcionEvento> inscripcionEventos) {
        this.eventos = inscripcionEventos;
    }

    public Set<Imagen> getImagens() {
        return imagens;
    }

    public void setImagens(Set<Imagen> imagens) {
        this.imagens = imagens;
    }

    public Set<Favorito> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(Set<Favorito> favoritos) {
        this.favoritos = favoritos;
    }

    public Asociacion getAsociacionevent() {
        return asociacionevent;
    }

    public void setAsociacionevent(Asociacion asociacion) {
        this.asociacionevent = asociacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Evento evento = (Evento) o;
        if(evento.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, evento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Evento{" +
            "id=" + id +
            ", nombre='" + nombre + "'" +
            ", fecha='" + fecha + "'" +
            ", streetAdress='" + streetAdress + "'" +
            ", lat='" + lat + "'" +
            ", lng='" + lng + "'" +
            ", capacidad='" + capacidad + "'" +
            ", entrada='" + entrada + "'" +
            ", descripcion='" + descripcion + "'" +
            '}';
    }
}
