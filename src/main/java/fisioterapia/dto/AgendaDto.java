package fisioterapia.dto;

import java.util.Date;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonFormat;

import fisioterapia.modelo.entities.Agenda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor

@Data
@Builder
@EqualsAndHashCode(of = "idAgenda")
public class AgendaDto {

    // private static ModelMapper modelMapper;

    private String username;

    private int idAgenda;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fecha;

    private int horaInicio;

    private int horaFin;

    private boolean disponible;

    // Metodo para convertir de AgendaDto a Agenda
    public Agenda convertToAgenda(ModelMapper modelMapper) {
        return modelMapper.map(this, Agenda.class);
    }

    public AgendaDto() {
        System.out.println("AgendaDto instanciado");
    }
    
}
