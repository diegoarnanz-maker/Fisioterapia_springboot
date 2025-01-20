package fisioterapia.modelo.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="usuarios")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener entre 6 y 20 caracteres")
    private String password;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    @NotNull(message = "El estado habilitado es obligatorio")
    private boolean enabled;

    @Temporal(TemporalType.DATE)
    @Column(name="FECHA_REGISTRO")
    private Date fechaRegistro;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    private String email;

    @ManyToMany
    @JoinTable(
        name="usuario_roles",
        joinColumns={@JoinColumn(name="USERNAME")},
        inverseJoinColumns={@JoinColumn(name="ID_ROLE")}
    )
    private List<Role> roles;
}