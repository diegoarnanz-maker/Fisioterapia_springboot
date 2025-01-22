package fisioterapia.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(of = "idAgenda")
public class AgendaDto {

    @JsonProperty("idUsuarioFisio")
    private String idUsuarioFisio;
    
    private int idAgenda;

    private Date fecha;

    private int horaInicio;

    private int horaFin;

    private boolean disponible;
}
