package mateo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Asociacion.
 */
@Entity
@Table(name = "asociacion")
public class Asociacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "feccha_creacion")
    private ZonedDateTime fecchaCreacion;

    @Column(name = "cuota")
    private Integer cuota;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "instrucciones")
    private String instrucciones;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "street_adress")
    private String streetAdress;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @OneToMany(mappedBy = "imagenaso")
    @JsonIgnore
    private Set<Imagen> imagens = new HashSet<>();

    @OneToMany(mappedBy = "asociacionevent")
    @JsonIgnore
    private Set<Evento> eventos = new HashSet<>();

    @OneToMany(mappedBy = "asociacion")
    @JsonIgnore
    private Set<InscripcionAso> asociacions = new HashSet<>();

    @OneToMany(mappedBy = "asof")
    @JsonIgnore
    private Set<Favorito> favoritos = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getFecchaCreacion() {
        return fecchaCreacion;
    }

    public void setFecchaCreacion(ZonedDateTime fecchaCreacion) {
        this.fecchaCreacion = fecchaCreacion;
    }

    public Integer getCuota() {
        return cuota;
    }

    public void setCuota(Integer cuota) {
        this.cuota = cuota;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Set<Imagen> getImagens() {
        return imagens;
    }

    public void setImagens(Set<Imagen> imagens) {
        this.imagens = imagens;
    }

    public Set<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(Set<Evento> eventos) {
        this.eventos = eventos;
    }

    public Set<InscripcionAso> getAsociacions() {
        return asociacions;
    }

    public void setAsociacions(Set<InscripcionAso> inscripcionAsos) {
        this.asociacions = inscripcionAsos;
    }

    public Set<Favorito> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(Set<Favorito> favoritos) {
        this.favoritos = favoritos;
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
        Asociacion asociacion = (Asociacion) o;
        if(asociacion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, asociacion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Asociacion{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", fecchaCreacion='" + fecchaCreacion + "'" +
            ", cuota='" + cuota + "'" +
            ", tipo='" + tipo + "'" +
            ", instrucciones='" + instrucciones + "'" +
            ", descripcion='" + descripcion + "'" +
            ", streetAdress='" + streetAdress + "'" +
            ", lat='" + lat + "'" +
            ", lng='" + lng + "'" +
            '}';
    }
}
