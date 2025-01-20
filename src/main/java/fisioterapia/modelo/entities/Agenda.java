package fisioterapia.modelo.entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//La agenda es el horario disponible del fisioterapeuta para atender a los clientes. Es decir, una agenda contiene la disponibilidad del fisioterapeuta para recibir pacientes en una fecha y hora determinada.

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "agenda")
public class Agenda implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AGENDA")
    private int idAgenda;

    @ManyToOne
    @JoinColumn(name = "FISIOTERAPEUTA_USERNAME")
    private Usuario fisioterapeuta;

    private Date fecha;

    @NotNull(message = "La hora de inicio no puede ser nula")
    @Column(name = "HORA_INICIO")
    private int horaInicio;

    @NotNull(message = "La hora de fin no puede ser nula")
    @Column(name = "HORA_FIN")
    private int horaFin;

    private boolean disponible;

    //METODOS PROPIOS
    public String getFisioterapeutaUsername() {
        return this.fisioterapeuta != null ? this.fisioterapeuta.getUsername() : null;
    }
}
