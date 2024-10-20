package co.com.crudSec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.crudSec.dto.ClientDTO;
import co.com.crudSec.entity.Client;
import co.com.crudSec.service.i.IClientService;


/**
 * <b>Descripción:</b> Clase ClientController que contiene todo el proceso de controller para garantizar el registro de un Client
 * @author roger
 *
 */
@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientController {
	
	@Autowired
	private IClientService iClientService;
	
	/**
	 * <b>Descripción:</b> Método que contiene toda la lógica necesaria para realizar su debido proceso de registro de un Client
	 * @param client corresponde a los datos de usuario necesarios para el registro
	 * @author roger
	 *
	 */
	@PostMapping("/crea")
    public String crearClient(@RequestBody ClientDTO client) {
		return iClientService.crearClient(client);
    }
	
	
	/**
	 * <b>Descripción:</b> Método que contiene toda la lógica necesaria para realizar su debido proceso de modificación de un Client
	 * @param client corresponde a los datos de usuario necesarios para la modificación
	 * @author roger
	 *
	 */
	@PostMapping("/modifica")
    public ResponseEntity<?> modificarClient(@RequestBody ClientDTO client){
		return iClientService.modificarClient(client);
    }
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de consultar a los Clients
	 * @author roger
	 */
	@GetMapping("/consulta")
	public List<Client> consultarClients(){
		return iClientService.consultarClients();
	}
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de consultar a los Clients
	 * @author roger
	 */
	@GetMapping("/consultaClientSharedKey/{sharedKey}")
	public List<Client>  consultaClientSharedKey(@PathVariable String sharedKey){
		return iClientService.consultarClientSharedKey(sharedKey);
	}
}
