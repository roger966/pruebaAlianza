/**
 * 
 */
package co.com.crudSec.repository;

/**
 * <b>Descripcion:</b> interface ClientRepository que contiene la interacción con bd para sus respectivas validaciones
 * @author roger
 *
 */
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.crudSec.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID>{
	Optional<Client> findByEmail(String email);
	Optional<Client> findByPhone(String phone);
	Optional<Client> findBySharedKey(String sharedKey);

}
