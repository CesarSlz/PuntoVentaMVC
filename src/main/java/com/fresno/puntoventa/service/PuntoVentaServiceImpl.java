package com.fresno.puntoventa.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fresno.puntoventa.model.Empleado;
import com.fresno.puntoventa.model.Usuario;

@Service
public class PuntoVentaServiceImpl implements UserDetailsService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HttpServletRequest request;

	public static final String URI_BASE = "http://localhost:8888/";

	public Object findAll(String complemento) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = restTemplate.getForObject(URI_BASE + complemento, JsonNode.class).get("datos");
		Object salida = mapper.treeToValue(jsonNode, Object.class);
		return salida;
	}

	public Object findById(String complemento, String id) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = restTemplate.getForObject(URI_BASE + complemento + id, JsonNode.class).get("datos");
		Object salida = mapper.treeToValue(jsonNode, Object.class);
		return salida;
	}
	
	public Object findByFecha(String complemento, String fecha) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = restTemplate.getForObject(URI_BASE + complemento + "?q=" + fecha, JsonNode.class).get("datos");
		Object salida = mapper.treeToValue(jsonNode, Object.class);
		return salida;
	}
	
	public String findUltimoCliente(String complemento) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = restTemplate.getForObject(URI_BASE + complemento, JsonNode.class).get("datos");
		String salida = mapper.treeToValue(jsonNode, String.class);
		return salida;
	}
	
	public String findCantidad(String complemento) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = restTemplate.getForObject(URI_BASE + complemento, JsonNode.class).get("datos");
		String salida = mapper.treeToValue(jsonNode, String.class);
		return salida;
	}
	
	public JsonNode create(String complemento, Object objeto) {
		JsonNode jsonNode = restTemplate.postForObject(URI_BASE + complemento, objeto, JsonNode.class).get("mensaje");
		return jsonNode;
	}
	
	public JsonNode update(String complemento, Object objeto, String id) {
		JsonNode jsonNode = restTemplate.exchange(URI_BASE + complemento + id, HttpMethod.PUT, 
				new HttpEntity<>(objeto), JsonNode.class).getBody().get("mensaje");
		return jsonNode;
	}
	
	public void delete(String complemento, String id) {
		restTemplate.delete(URI_BASE + complemento + id);
	}

	@Override
	public UserDetails loadUserByUsername(String telefono) throws UsernameNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		Empleado empleado = null;
		
		JsonNode jsonNode = restTemplate
				.getForObject(URI_BASE + "empleados/telefono?q=" + telefono, JsonNode.class)
				.get("datos");
		
		try {
			empleado = mapper.treeToValue(jsonNode, Empleado.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		List<Usuario> tipoUsuario = new ArrayList<Usuario>();
		tipoUsuario.add(empleado.getUsuario());
		
		List<GrantedAuthority> autoridades = new ArrayList<GrantedAuthority>();
		for (Usuario autoridad: tipoUsuario) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(autoridad.getTipo());
			autoridades.add(grantedAuthority);
		}
		
		request.getSession().setAttribute("empleado", empleado);
		request.getSession().setAttribute("bandera", 0);
		
		UserDetails user = (UserDetails) new User(empleado.getTelefono(), Passgenerator(empleado.getContrasena()), 
				autoridades);
	
		return user;
	}
	
	public String Passgenerator(String contrasena) {
		 BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(4);
		 return bCryptPasswordEncoder.encode(contrasena);
	}
}
