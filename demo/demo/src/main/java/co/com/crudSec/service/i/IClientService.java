/**
 * 
 */
package co.com.crudSec.service.i;

import java.util.List;

import org.springframework.http.ResponseEntity;
import co.com.crudSec.dto.ClientDTO;
import co.com.crudSec.entity.Client;

/**
 * <b>Descripción:</b> Interface IClientService que contiene lógica necesaria para realizar el llamado a su debido proceso de Client
 * @author roger
 *
 */
public interface IClientService {
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de registrar Client
	 * @param usuario corresponde a los datos de usuario necesarios para el registro
	 */
	public String crearClient(ClientDTO client);
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de modificar Client
	 * @param usuario corresponde a los datos de usuario necesarios para la modificación
	 */
	public ResponseEntity<String> modificarClient(ClientDTO Client);
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de consultar a los Client
	 */
	public List<Client> consultarClients();

	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de consultar Client por su sharedKey
	 * @param id dato de busqueda para la acción de eliminar
	 */
	public List<Client>  consultarClientSharedKey(String sharredKey); 
}
