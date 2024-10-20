/**
 * 
 */
package co.com.crudSec.service.imp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.com.crudSec.dto.ClientDTO;
import co.com.crudSec.entity.Client;
import co.com.crudSec.repository.ClientRepository;
import co.com.crudSec.service.i.IClientService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <b>Descripción:</b> Clase ClientService que contiene toda la lógica necesaria para realizar su debido proceso de registro
 * @author roger
 *
 */
@Service
public class ClientService implements IClientService{

	@Autowired
    private ClientRepository clientRepository;
	/**
     * Atributo para el manejo de expresión regular para el email
     */										
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    /**
     * Atributo para el manejo del pattern del email, para validar el matcher
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    /**
     * Atributo para el manejo de los nombres usados de clients
     */
    private Set<String> nombresUsados = new HashSet<>();
    /**
     * Atributo para el manejo de Logs que ayudan cuando se pase a ambiente  productivo
     */
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    
    /**
     * <b>Descripción:</b> Método encargado de registrar un Client con sus validaciones
     * @author roger
     *
     */
    public String crearClient(ClientDTO client) {
    	logger.debug("Inicio del método: registrarClient");
    	if(client.getName().isEmpty() || client.getEmail().isEmpty() || client.getPhone().isEmpty() ||
    			client.getStartDate()== null || client.getEndDate()==null) {
			return "No es posible registrar por falta de datos";
		}
		if(!validarCorreo(client.getEmail())){
			return "El correo no cumple con el formato requerido";
		}
		Optional<Client> usuarioCorreo = clientRepository.findByEmail(client.getEmail());
		Optional<Client> clientPhone = clientRepository.findByPhone(client.getPhone());
		if(!usuarioCorreo.isEmpty()) {
			return "El correo ya se encuentra registrado";
		}
		if(!clientPhone.isEmpty()) {
			return "El número celular ya se encuentra registrado";
		}
		
		Client clientEntidad=convertirDtoEntidad(client);
		clientRepository.save(clientEntidad);
		logger.debug("Finalización del método: registrarClient");
		return "Usuario creado éxitosamente";
    }

	/**
     * <b>Descripción:</b> Método encargado de modificar un Client con sus validaciones
     * @author roger
     *
     */
	public ResponseEntity<String> modificarClient(ClientDTO client) {
		logger.debug("Inicio del método: modificarClient");
		Optional<Client> clientId = clientRepository.findById(client.getId());
		Optional<Client> clientCorreo = clientRepository.findByEmail(client.getEmail());
		Optional<Client> clientPhone = clientRepository.findByPhone(client.getPhone());
		if(clientId.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("No se encontró usuario con ese id");
		}
		if(client.getName().isEmpty() || client.getEmail().isEmpty() || client.getPhone().isEmpty() ||
    			client.getStartDate()== null || client.getEndDate()==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("No es posible registrar por falta de datos");
		}
		if(!validarCorreo(client.getEmail())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("El correo no cumple con el formato requerido");
		}
		if(clientCorreo.isPresent() && clientCorreo.get().getId()!= client.getId()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("El correo ya se encuentra registrado");
		}
		if(clientPhone.isPresent() && clientPhone.get().getId()!= client.getId()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("El número celular ya se encuentra registrado");
		}
		Client clientEntidad=convertirDtoEntidad(client);
		clientEntidad.setId(client.getId());
		clientRepository.save(clientEntidad);
		logger.debug("Finalización del método: modificarClient");
		return ResponseEntity.status(HttpStatus.OK)
				.body("El usuario ha sido modificado éxitosamente");
	}
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el debido proceso de consultar a los Clients 
	 * @author roger
	 */
	public List<Client> consultarClients(){
		logger.debug("Inicio del método: consultarClients");
		List<Client> client = clientRepository.findAll();
		logger.debug("Finalización del método: consultarClients");
		return client;
	}


	/**
     * <b>Descripción:</b> Método encargado de convertir dto a entidad
     * @author roger
     *
     */
	private Client convertirDtoEntidad(ClientDTO usuario) {
		logger.debug("Inicio del método: convertirDtoEntidad");
		Client clientEntidad = new Client();
		clientEntidad.setName(usuario.getName().trim().toUpperCase());
		clientEntidad.setEmail(usuario.getEmail().trim());
		clientEntidad.setPhone(usuario.getPhone());
		clientEntidad.setStartDate(usuario.getStartDate());
		clientEntidad.setEndDate(usuario.getEndDate()); 
		clientEntidad.setSharedKey(construirSharedKey(usuario.getName().trim().toLowerCase()));
		logger.debug("Finalización del método: convertirDtoEntidad");
		return clientEntidad ;
	}

	
	private String construirSharedKey(String nombreCompleto) {
		logger.debug("Inicio del método: construirSharedKey");
		String[] partes = nombreCompleto.split(" ");
        String nombre = partes[0].toLowerCase();
        String apellido = partes[partes.length - 1].toLowerCase(); 
        String nombreUsuarioBase = nombre.charAt(0) + apellido;
        String nombreUsuario = nombreUsuarioBase;
        // Manejar colisiones
        int contador = 1;
        while (nombresUsados.contains(nombreUsuario)) {
            nombreUsuario = nombreUsuarioBase + contador;
            contador++;
        }
        nombresUsados.add(nombreUsuario);
        logger.debug("Finalización del método: construirSharedKey");
        return nombreUsuario;
	}


	/**
	 * <b>Descripción:</b> Método encargado de realizar la validación del correo
	 * @author roger
	 *
	 */
	private boolean validarCorreo(String correo) {
		logger.debug("Inicio del método: validarCorreo");
		if (correo == null || correo.isEmpty()) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(correo);
        logger.debug("Finalización del método: validarCorreo");
        return matcher.matches();
	}
	
	/**
	 * <b>Descripción:</b> Método encargado de realizar la consulta de client por su sharedKey
	 * @author roger
	 *
	 */
	public List<Client>  consultarClientSharedKey(String sharredKey){
		logger.debug("Inicio del método: consultarClientSharedKey");
		List<Client> clientShared = new ArrayList<Client>();
		Optional<Client> client = clientRepository.findBySharedKey(sharredKey);
		if(client.isPresent()) {
			clientShared.add(client.get());
		}
		logger.debug("Finalización del método: consultarClientSharedKey");
		return clientShared;
	}
}
