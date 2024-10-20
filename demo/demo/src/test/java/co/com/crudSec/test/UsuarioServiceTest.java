/**
 * 
 */
package co.com.crudSec.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import co.com.crudSec.dto.ClientDTO;
import co.com.crudSec.entity.Client;
import co.com.crudSec.repository.ClientRepository;
import co.com.crudSec.service.imp.ClientService;

/**
 * <b>Descripción:</b> Clase UsuarioServiceTest que contiene todo el proceso de controller para garantizar el registro de un cliente
 * @author roger
 *
 */
public class UsuarioServiceTest {
	
	@InjectMocks
	private ClientService clientService;
	@Mock
	private ClientRepository clientRepository;
	@BeforeEach
	public void setUp() {
    MockitoAnnotations.openMocks(this);
    }
	
	
	/**
	 * Test testFallidoPorFaltaDeDatos()  de forma fallida ya que no cumple con los datos necesarios para registrar usuario
	 * 
	 */
	@Test
	public void testFallidoPorFaltaDeDatos() {
		ClientDTO usuario = new ClientDTO();
		usuario.setName("");
		String exception = clientService.crearClient(usuario);
        clientService.crearClient(usuario);
        assertEquals("No es posible registrar por falta de datos", exception);
	}
	
	/**
	 * Test testCrear() de forma éxitosa que valida el debido proceso de registrar usuario
	 * 
	 */
	
	@Test
	public void testCrear() {
		ClientDTO usuario = new ClientDTO();
		usuario.setName("Roger");
		usuario.setEmail("roger@gmail.com");
		usuario.setPhone("31500000");
		usuario.setEndDate(new Date());
		usuario.setStartDate(new Date());
		Client uEntidad = new Client();
		uEntidad.setName(usuario.getName());
		uEntidad.setPhone(usuario.getPhone());
		uEntidad.setEmail(usuario.getEmail());
		uEntidad.setEndDate(usuario.getEndDate());
		uEntidad.setStartDate(usuario.getStartDate());
		when(clientRepository.save(any(Client.class))).thenReturn(uEntidad);	
		String response = clientService.crearClient(usuario);
		assertEquals("Usuario creado éxitosamente",response);
		
	}
	
	/**
	 * Test testFallidoPorMail() de forma fallida ya que el mail correspondiente de registrar usuario no cumple los requerimientos
	 * 
	 */
	@Test
	public void testFallidoPorMail() {
		ClientDTO usuario = new ClientDTO();
		usuario.setName("Juan");
		usuario.setEmail("juan-juan");
		usuario.setPhone("45698");
		usuario.setEndDate(new Date());
		usuario.setStartDate(new Date());
		String exception = clientService.crearClient(usuario);
        assertEquals("El correo no cumple con el formato requerido", exception);
	}
	
	/**
	 * Test testFallidoModificarPorId() de forma fallida ya que no hay id de usuario correspondiente para modificar al usuario
	 * 
	 */
	@Test
	public void testFallidoModificarPorId() {
		ClientDTO usuario = new ClientDTO();
		usuario.setName("Roger");
		usuario.setEmail("roger@gmail.com");
		usuario.setPhone("31500000");
		usuario.setEndDate(new Date());
		usuario.setStartDate(new Date());
		Client uEntidad = new Client();
		uEntidad.setName(usuario.getName());
		uEntidad.setPhone(usuario.getPhone());
		uEntidad.setEmail(usuario.getEmail());
		uEntidad.setEndDate(usuario.getEndDate());
		uEntidad.setStartDate(usuario.getStartDate());
		when(clientRepository.save(any(Client.class))).thenReturn(uEntidad);
		ResponseEntity<String> response = clientService.modificarClient(usuario);
		assertEquals("No se encontró usuario con ese id", response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	
	/**
	 * Test testConsultarUsuario() de forma éxitosa para consultar a los Client
	 * 
	 */
	@Test
	public void testConsultarClient() {
		Client client = new Client();
		client.setName("Roger");
		client.setEmail("roger@gmail.com");
		client.setPhone("31500000");
		client.setEndDate(new Date());
		client.setEndDate(new Date());
		Client uEntidad = new Client();
		uEntidad.setName(client.getName());
		uEntidad.setPhone(client.getPhone());
		uEntidad.setEmail(client.getEmail());
		uEntidad.setEndDate(client.getEndDate());
		uEntidad.setStartDate(client.getStartDate());
		when(clientRepository.findAll()).thenReturn(Arrays.asList(client));
		List<Client>listaUsuario= clientService.consultarClients();
		assertNotNull(listaUsuario);
	}
	
	
	/**
	 * Test testCorreoYaRegistrado() de forma fallida ya que existe ese email.
	 * 
	 */
	@Test
    public void testCorreoYaRegistrado() {
		ClientDTO client = new ClientDTO();
	    client.setName("John Doe");
	    client.setEmail("test@example.com");
	    client.setPhone("123456789");
	    client.setStartDate(new Date());
	    client.setEndDate(new Date());
	    when(clientRepository.findByEmail(client.getEmail())).thenReturn(Optional.of(new Client()));
	    String resultado = clientService.crearClient(client);
	    assertEquals("El correo ya se encuentra registrado", resultado);
    }
	
	/**
	 * Test testPhoneYaRegistrado() de forma fallida ya que existe ese phone.
	 * 
	 */
	@Test
    public void testPhoneYaRegistrado() {
		ClientDTO client = new ClientDTO();
	    client.setName("John Doe");
	    client.setEmail("test@example.com");
	    client.setPhone("123456789");
	    client.setStartDate(new Date());
	    client.setEndDate(new Date());
	    when(clientRepository.findByPhone(client.getPhone())).thenReturn(Optional.of(new Client()));
	    String resultado = clientService.crearClient(client);
	    assertEquals("El número celular ya se encuentra registrado", resultado);
    }
}
