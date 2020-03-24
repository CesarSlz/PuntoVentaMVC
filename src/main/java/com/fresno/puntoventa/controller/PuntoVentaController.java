package com.fresno.puntoventa.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fresno.puntoventa.model.Abono;
import com.fresno.puntoventa.model.DetalleCompraInfo;
import com.fresno.puntoventa.model.DetalleVentaInfo;
import com.fresno.puntoventa.model.Empleado;
import com.fresno.puntoventa.model.Producto;
import com.fresno.puntoventa.model.Proveedor;
import com.fresno.puntoventa.model.Saldo;
import com.fresno.puntoventa.service.PuntoVentaServiceImpl;

@Controller
public class PuntoVentaController {

	@Autowired
	private PuntoVentaServiceImpl service;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired 
	private HttpServletRequest request;
	
	// View login
	@GetMapping({ "/", "/login" })
	public String login() {
		return "login";
	}
	
	// View inicio
	@GetMapping("/inicio")
	public String index(Model model) throws JsonProcessingException {
		model.addAttribute("productos", service.findAll("detalleventa/masvendidos"));
		
		return "index";
	}
	
	// View inventario
	@GetMapping("/inventario")
	public String inventario(Model model) throws JsonProcessingException {
		// Destruir variables de sesion para el texto del modal de exito o error
		if(request.getSession().getAttribute("bandera").equals(0)) {
			 request.getSession().removeAttribute("swTitle");
			 request.getSession().removeAttribute("swText");
			 request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
		
		// Mandar a la vista la informacion de los producto
		model.addAttribute("productos", service.findAll("productos"));
		
		// Mandar a la vista el mensaje paara el confirm dialog
		model.addAttribute("desc", "¿Deseas eliminar el producto?");
		return "inventario";
	}
	
	// View registrar nuevo producto
	@GetMapping("/producto/nuevo")
	public String producto(Model model) throws JsonProcessingException {
		// Destruir variables de sesion para el texto del modal de exito o error
		if(request.getSession().getAttribute("bandera").equals(0)) {
			 request.getSession().removeAttribute("swTitle");
			 request.getSession().removeAttribute("swText");
			 request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
		
		// Mandar a la vista "Nuevo" para mostrar los inputs de registrar nuevo producto
		model.addAttribute("titulo", "Nuevo");
		
		// Mandar a la vista la ruta para el controlar que registra nuevos productos
		model.addAttribute("accion", "/producto/nuevo");
		
		// Mandar a la vista el mensaje paara el confirm dialog
		model.addAttribute("desc", "¿Deseas guardar el producto?");
		
		// Mandar a la vista el objeto que guardar la informacion del formulario
		model.addAttribute("producto", new Producto());
		
		// Mandar a la vista la informacion de las categorias
		model.addAttribute("categorias", service.findAll("categorias"));
		return "producto";
	}
	
	// View modificar producto
	@GetMapping("/producto/{codigoBarra}")
	public String producto(Model model, 
			@PathVariable("codigoBarra") String codigoBarra) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			 request.getSession().removeAttribute("swTitle");
			 request.getSession().removeAttribute("swText");
			 request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
		
		// Mandar a la vista "Modificar" para mostrar los inputs de registrar nuevo producto
		model.addAttribute("titulo", "Modificar");
		
		// Mandar a la vista la ruta para el controlar que modifica los productos
		model.addAttribute("accion", "/producto/" + codigoBarra);
		model.addAttribute("desc", "¿Deseas modificar el producto?");
		
		// Mandar a la vista el objeto que guardar la informacion del formulario
		model.addAttribute("producto", new Producto());
		
		// Mandar a la vista el producto que será modificado
		model.addAttribute("productoModificar", service.findById("productos/codigo/", codigoBarra));
		model.addAttribute("categorias", service.findAll("categorias"));
		return "producto";
	}
	
	// Agregar nuevo producto
	@PostMapping("/producto/nuevo")
	public RedirectView addProducto(@Valid @ModelAttribute("producto") Producto producto,
			BindingResult bindingResult) throws JsonProcessingException{
		
		// Guardar en json la respuesta del RestApi
		JsonNode mensaje = service.create("productos", producto);
		
		// Guardar en variables de sesion la informacion para el modal
		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		
		// Variable que indica cuando se podra destruir la informacion de sesion
		request.getSession().setAttribute("bandera", 1);

		// Redirigir a la vista
		return new RedirectView("nuevo");
	}
	
	// Modificar producto
	@PostMapping("/producto/{codigoBarra}")
	public RedirectView updateProducto(@PathVariable("codigoBarra") String codigoBarra,
			@Valid @ModelAttribute("producto") Producto producto,
			BindingResult bindingResult) throws JsonProcessingException{
		JsonNode mensaje = service.update("productos/codigo/", producto, codigoBarra);

		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
		
		return new RedirectView(codigoBarra);
	}
	
	// Eliminar producto
	@PostMapping("/deleteProducto/{codigoBarra}")
	public RedirectView deleteProducto(@PathVariable("codigoBarra") String codigoBarra) throws JsonProcessingException{
		service.delete("productos/codigo/", codigoBarra);

		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", "Exito al eliminar el producto");
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
		
		return new RedirectView("/inventario");
	}
	
	// View lista de ventas
	@GetMapping("/ventas")
	public String ventas(Model model) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			 request.getSession().removeAttribute("swTitle");
			 request.getSession().removeAttribute("swText");
			 request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
		
		model.addAttribute("ventas", service.findAll("ventas"));
		model.addAttribute("desc", "¿Deseas eliminar la venta?");
		return "ventas";
	}
	
	// View registrar nueva venta
	@GetMapping("/venta/nueva")
	public String venta(Model model) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			 request.getSession().removeAttribute("swTitle");
			 request.getSession().removeAttribute("swText");
			 request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
		
		// Obtener el ultimo cliente
		int noCliente = Integer.parseInt(service.findUltimoCliente("ventas/ultimocliente")) + 1;
		
		model.addAttribute("titulo", "Nueva");
		model.addAttribute("accion", "/venta/nueva");
		model.addAttribute("desc", "¿Deseas guardar la venta?");
		
		model.addAttribute("dventa", new DetalleVentaInfo());
		model.addAttribute("noCliente", noCliente);
		return "venta";
	}
	
	// View modificar venta
	@GetMapping("/venta/{venta}")
	public String venta(Model model, 
			@PathVariable("venta") String venta) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			 request.getSession().removeAttribute("swTitle");
			 request.getSession().removeAttribute("swText");
			 request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
		
		model.addAttribute("titulo", "Modificar");
		model.addAttribute("accion", "/venta/" + venta);
		model.addAttribute("desc", "¿Deseas modificar la venta?");
		
		model.addAttribute("dventa", new DetalleVentaInfo());
		
		// Mandar a la vista la venta que será modificada
		model.addAttribute("ventaModificar", service.findById("ventas/codigo/", venta));
		model.addAttribute("detalleVentaModificar", service.findAll("detalleventa/codigo/" + venta));
		return "venta";
	}
	
	// Agregar nueva venta
	@PostMapping("/venta/nueva")
	public RedirectView addVenta(@Valid @ModelAttribute("dventa") DetalleVentaInfo dventa,
			BindingResult bindingResult) throws JsonProcessingException{
		
		// Guardar en json la respuesta del RestApi
		JsonNode mensaje = service.create("detalleventa", dventa);
		
		// Guardar en variables de sesion la informacion para el modal
		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		
		// Variable que indica cuando se podra destruir la informacion de sesion
		request.getSession().setAttribute("bandera", 1);

		// Redirigir a la vista
		return new RedirectView("nueva");
	}
	
	// Modificar venta
	@PostMapping("/venta/{venta}")
	public RedirectView updateVenta(@PathVariable("venta") String venta,
			@Valid @ModelAttribute("dventa") DetalleVentaInfo dventa,
			BindingResult bindingResult) throws JsonProcessingException{
		JsonNode mensaje = service.update("detalleventa/codigo/", dventa, venta);
		
		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
			
		return new RedirectView(venta);
	}
	
	// Eliminar venta
	@PostMapping("/deleteVenta/{venta}")
	public RedirectView deleteVenta(@PathVariable("venta") String venta) throws JsonProcessingException{
		service.delete("ventas/codigo/", venta);

		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", "Exito al eliminar la venta");
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
		
		return new RedirectView("/ventas");
	}
	
	// View lista de compras
	@GetMapping("/compras")
	public String compras(Model model) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			request.getSession().removeAttribute("swText");
			request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
			
		model.addAttribute("compras", service.findAll("compras"));
		model.addAttribute("desc", "¿Deseas eliminar la compra?");
		return "compras";
	}
	
	// View registrar nueva compra
	@GetMapping("/compra/nueva")
	public String compra(Model model) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			request.getSession().removeAttribute("swText");
			request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
			
		model.addAttribute("titulo", "Nueva");
		model.addAttribute("accion", "/compra/nueva");
		model.addAttribute("desc", "¿Deseas guardar la compra?");
			
		model.addAttribute("dventa", new DetalleCompraInfo());
		model.addAttribute("proveedores", service.findAll("proveedores"));
		return "compra";
	}
	
	// View modificar compra
	@GetMapping("/compra/{compra}")
	public String compra(Model model, 
			@PathVariable("compra") String compra) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			request.getSession().removeAttribute("swText");
			request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
			
		model.addAttribute("titulo", "Modificar");
		model.addAttribute("accion", "/compra/" + compra);
		model.addAttribute("desc", "¿Deseas modificar la compra?");
			
		model.addAttribute("dcompra", new DetalleCompraInfo());
			
		// Mandar a la vista la compra que será modificada
		model.addAttribute("compraModificar", service.findById("compras/codigo/", compra));
		model.addAttribute("detalleCompraModificar", service.findAll("detallecompra/codigo/" + compra));
		return "compra";
	}
	
	// Dirigir a compras cuando en deleteCompra surge un error
	@GetMapping("/deleteCompra/{compra}")
	public RedirectView deleteCompra() throws JsonProcessingException {
		return new RedirectView("/compras");
	}
	
	// Agregar nueva compra
	@PostMapping("/compra/nueva")
	public RedirectView addCompra(@Valid @ModelAttribute("dcompra") DetalleCompraInfo dcompra,
			BindingResult bindingResult) throws JsonProcessingException {
		
		// Guardar en json la respuesta del RestApi
		JsonNode mensaje = service.create("detallecompra", dcompra);
			
		// Guardar en variables de sesion la informacion para el modal
		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
			
		// Variable que indica cuando se podra destruir la informacion de sesion
		request.getSession().setAttribute("bandera", 1);

		// Redirigir a la vista
		return new RedirectView("nueva");
	}
		
	// Modificar compra
	@PostMapping("/compra/{compra}")
	public RedirectView updateCompra(@PathVariable("compra") String compra,
			@Valid @ModelAttribute("dcompra") DetalleCompraInfo dcompra,
			BindingResult bindingResult) throws JsonProcessingException{
		JsonNode mensaje = service.update("detallecompra/codigo/", dcompra, compra);
			
		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
				
		return new RedirectView(compra);
	}
		
	// Eliminar compra
	@PostMapping("/deleteCompra/{compra}")
	public RedirectView deleteCompra(@PathVariable("compra") String compra) throws JsonProcessingException{
		service.delete("compras/codigo/", compra);

		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", "Exito al eliminar la compra");
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
			
		return new RedirectView("/compras");
	}
	
	// View proveedores
	@GetMapping("/proveedores")
	public String proveedores(Model model) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			 request.getSession().removeAttribute("swTitle");
			 request.getSession().removeAttribute("swText");
			 request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
			
		model.addAttribute("proveedores", service.findAll("proveedores"));
		model.addAttribute("desc", "¿Deseas eliminar el proveedor?");
		return "proveedores";
	}
	
	// View registrar nuevo proveedor
	@GetMapping("/proveedor/nuevo")
	public String proveedor(Model model) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			request.getSession().removeAttribute("swText");
			request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
			
		model.addAttribute("titulo", "Nuevo");
		model.addAttribute("accion", "/proveedor/nuevo");
		model.addAttribute("desc", "¿Deseas guardar el proveedor?");
		model.addAttribute("proveedor", new Proveedor());
			
		return "proveedor";
	}
	
	// View modificar proveedor
	@GetMapping("/proveedor/{codigo}")
	public String proveedor(Model model, 
			@PathVariable("codigo") String codigo) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			request.getSession().removeAttribute("swText");
			request.getSession().removeAttribute("swIcon");
			}
		request.getSession().setAttribute("bandera", 0);
			
		model.addAttribute("titulo", "Modificar");
		model.addAttribute("accion", "/proveedor/" + codigo);
		model.addAttribute("desc", "¿Deseas modificar el proveedor?");
		model.addAttribute("producto", new Proveedor());
			
		model.addAttribute("proveedorModificar", service.findById("proveedores/codigo/", codigo));
		return "proveedor";
	}
	
	// Agregar nuevo proveedor
	@PostMapping("/proveedor/nuevo")
	public RedirectView addProveedor(@Valid @ModelAttribute("proveedor") Proveedor proveedor,
			BindingResult bindingResult) throws JsonProcessingException{
		
		JsonNode mensaje = service.create("proveedores", proveedor);
		
		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);

		return new RedirectView("nuevo");
	}
		
	// Modificar proveedor
	@PostMapping("/proveedor/{codigo}")
	public RedirectView updateProveedor(@PathVariable("codigo") String codigo,
			@Valid @ModelAttribute("proveedor") Proveedor proveedor,
			BindingResult bindingResult) throws JsonProcessingException{
		
		JsonNode mensaje = service.update("proveedores/codigo/", proveedor, codigo);

		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
			
		return new RedirectView(codigo);
	}
		
	// Eliminar proveedor
	@PostMapping("/deleteProveedor/{codigo}")
	public RedirectView deleteProveedor(@PathVariable("codigo") String codigo) throws JsonProcessingException{
		service.delete("proveedores/codigo/", codigo);

		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", "Exito al eliminar el proveedor");
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
			
		return new RedirectView("/proveedores");
	}
	
	// View empleados
	@GetMapping("/empleados")
	public String empleados(Model model) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			 request.getSession().removeAttribute("swTitle");
			 request.getSession().removeAttribute("swText");
			 request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
			
		model.addAttribute("empleados", service.findAll("empleados"));
		model.addAttribute("desc", "¿Deseas eliminar al empleado?");
		return "empleados";
	}
	
	// View registrar nuevo empleado
	@GetMapping("/empleado/nuevo")
	public String empleado(Model model) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			request.getSession().removeAttribute("swText");
			request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
				
		model.addAttribute("titulo", "Nuevo");
		model.addAttribute("accion", "/empleado/nuevo");
		model.addAttribute("desc", "¿Deseas guardar el empleado?");
		model.addAttribute("empleado", new Empleado());
		model.addAttribute("puestos", service.findAll("puestos"));
		model.addAttribute("usuarios", service.findAll("usuarios"));
		
		return "empleado";
	}
	
	// View modificar empleado
	@GetMapping("/empleado/{codigo}")
	public String empleado(Model model, 
			@PathVariable("codigo") String codigo) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			request.getSession().removeAttribute("swText");
			request.getSession().removeAttribute("swIcon");
			}
		request.getSession().setAttribute("bandera", 0);
				
		model.addAttribute("titulo", "Modificar");
		model.addAttribute("accion", "/empleado/" + codigo);
		model.addAttribute("desc", "¿Deseas modificar el empleado?");
		model.addAttribute("empleado", new Empleado());
		model.addAttribute("puestos", service.findAll("puestos"));
		model.addAttribute("usuarios", service.findAll("usuarios"));
		model.addAttribute("empleadoModificar", service.findById("empleados/codigo/", codigo));
		
		return "empleado";
	}
	
	// Agregar nuevo empleado
	@PostMapping("/empleado/nuevo")
	public RedirectView addEmpleado(@Valid @ModelAttribute("empleado") Empleado empleado,
			BindingResult bindingResult) throws JsonProcessingException{
			
		JsonNode mensaje = service.create("empleados", empleado);
			
		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);

		return new RedirectView("nuevo");
	}
			
	// Modificar empleado
	@PostMapping("/empleado/{codigo}")
	public RedirectView updateEmpleado(@PathVariable("codigo") String codigo,
			@Valid @ModelAttribute("empleado") Empleado empleado,
			BindingResult bindingResult) throws JsonProcessingException{
			
		JsonNode mensaje = service.update("empleados/codigo/", empleado, codigo);

		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
				
		return new RedirectView(codigo);
	}
			
	// Eliminar empleado
	@PostMapping("/deleteEmpleado/{codigo}")
	public RedirectView deleteEmpleado(@PathVariable("codigo") String codigo) throws JsonProcessingException{
		service.delete("empleados/codigo/", codigo);

		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", "Exito al eliminar el empleado");
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
				
		return new RedirectView("/empleados");
	}
	
	// View adeudos
	@GetMapping("/adeudos")
	public String adeudos(Model model) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			request.getSession().removeAttribute("swText");
			request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
				
		model.addAttribute("adeudos", service.findAll("adeudos"));
		model.addAttribute("desc", "¿Deseas eliminar el adeudo?");
		return "adeudos";
	}
	
	// View abonar adeudo
	@GetMapping("/adeudo/{codigo}")
	public String adeudo(Model model, 
			@PathVariable("codigo") String codigo) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			request.getSession().removeAttribute("swText");
			request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
	
		model.addAttribute("accion", "/adeudo/" + codigo);
		model.addAttribute("desc", "¿Deseas realizar el abono?");
				
		model.addAttribute("abono", new Abono());
				
		// Mandar a la vista la informacion del adeudo y la informacion de los abonos
		model.addAttribute("adeudo", service.findById("adeudos/codigo/", codigo));
		model.addAttribute("abonos", service.findAll("abonos/codigo/" + codigo));
		model.addAttribute("abonado", service.findCantidad("abonos/total/" + codigo));
		
		return "adeudo";
	}
	
	// Agregar nuevo abono
	@PostMapping("/adeudo/{codigo}")
	public RedirectView addAbono(@PathVariable("codigo") String codigo,
			@Valid @ModelAttribute("abono") Abono abono,
			BindingResult bindingResult) throws JsonProcessingException{
		JsonNode mensaje = service.create("abonos", abono);
				
		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
					
		return new RedirectView(codigo);
	}
	
	// Eliminar abono
	@PostMapping("/deleteAbono/{abono}")
	public RedirectView deleteAbono(@PathVariable("abono") Abono abono) throws JsonProcessingException{
		service.delete("abonos/codigo/", abono.getId().toString());

		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", "Exito al eliminar el abono");
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
					
		return new RedirectView(abono.getAdeudo().getId().toString());
	}
	
	// View saldos
	@GetMapping("/saldos")
	public String saldos(Model model) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			request.getSession().removeAttribute("swText");
			request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
					
		model.addAttribute("saldos", service.findAll("saldos"));
		model.addAttribute("desc", "¿Deseas eliminar el saldo?");
		return "saldos";
	}
	
	// View registrar nuevo saldo
	@GetMapping("/saldo/nuevo")
	public String saldo(Model model) throws JsonProcessingException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			request.getSession().removeAttribute("swText");
			request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
			
		model.addAttribute("titulo", "Nuevo");
		model.addAttribute("accion", "/saldo/nuevo");
		model.addAttribute("desc", "¿Deseas guardar el saldo?");
		model.addAttribute("saldo", new Saldo());
		
		model.addAttribute("abonoCompra", service.findCantidad("abonos/total"));
		model.addAttribute("compraTotal", service.findCantidad("compras/total"));
		model.addAttribute("ventaTotal", service.findCantidad("ventas/total"));
		model.addAttribute("existeSaldo", service.findByFecha("saldos/fecha", simpleDateFormat.format(new Date())));
		
		return "saldo";
	}
	
	// View modificar saldo
	@GetMapping("/saldo/{codigo}")
	public String saldo(Model model, 
			@PathVariable("codigo") String codigo) throws JsonProcessingException {
		if(request.getSession().getAttribute("bandera").equals(0)) {
			request.getSession().removeAttribute("swTitle");
			 request.getSession().removeAttribute("swText");
			 request.getSession().removeAttribute("swIcon");
		}
		request.getSession().setAttribute("bandera", 0);
			
		model.addAttribute("titulo", "Modificar");
		model.addAttribute("accion", "/saldo/" + codigo);
		model.addAttribute("desc", "¿Deseas modificar el saldo?");
		model.addAttribute("saldo", new Saldo());
		model.addAttribute("saldoModificar", service.findById("saldos/codigo/", codigo));
		
		return "saldo";
	}
	
	// Agregar nuevo saldo
	@PostMapping("/saldo/nuevo")
	public RedirectView addSaldo(@Valid @ModelAttribute("saldo") Saldo saldo,
			BindingResult bindingResult) throws JsonProcessingException{
		
		JsonNode mensaje = service.create("saldos", saldo);
	
		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);

		return new RedirectView("nuevo");
	}
	
	// Modificar saldo
	@PostMapping("/saldo/{codigo}")
	public RedirectView updateSaldo(@PathVariable("codigo") String codigo,
			@Valid @ModelAttribute("saldo") Saldo saldo,
			BindingResult bindingResult) throws JsonProcessingException{
		JsonNode mensaje = service.update("saldos/codigo/", saldo, codigo);

		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
			
		return new RedirectView(codigo);
	}
	
	// Eliminar saldo
	@PostMapping("/deleteSaldo/{codigo}")
	public RedirectView deleteSaldo(@PathVariable("codigo") String codigo) throws JsonProcessingException{
		service.delete("saldos/codigo/", codigo);

		request.getSession().setAttribute("swTitle", "¡Exito!");
		request.getSession().setAttribute("swText", "Exito al eliminar el producto");
		request.getSession().setAttribute("swIcon", "success");
		request.getSession().setAttribute("bandera", 1);
			
		return new RedirectView("/saldos");
	}
	
	@ExceptionHandler(HttpClientErrorException.class)
    public RedirectView handleClientError(
    		HttpClientErrorException e) throws JsonParseException, JsonMappingException, IOException {
        JsonNode mensaje = mapper.readValue(e.getResponseBodyAsByteArray(), JsonNode.class).get("mensaje");
		
		request.getSession().setAttribute("swTitle", "¡Error!");
		request.getSession().setAttribute("swText", mensaje);
		request.getSession().setAttribute("swIcon", "error");
		request.getSession().setAttribute("bandera", 1);
		
        return new RedirectView(request.getRequestURL().toString());
    }
}
