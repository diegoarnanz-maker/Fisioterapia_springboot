package fisioterapia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fisioterapia.modelo.entities.Perfil;


public interface IPerfilRepository extends JpaRepository<Perfil
, Long> {

}
