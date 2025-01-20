package fisioterapia.modelo.entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "citas")

public class Cita implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITA")
    private int idCita;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CITA")
    private Date fechaCita;

    @ManyToOne
    @JoinColumn(name = "USERNAME")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "FISIOTERAPEUTA_USERNAME")
    private Usuario fisioterapeuta;

    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    private String observaciones;

    private String tratamiento;
}