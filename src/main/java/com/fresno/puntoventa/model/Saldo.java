package com.fresno.puntoventa.model;

import java.util.Date;

public class Saldo {

	private Long id;
	private Empleado empleado;
	private Double fondoCaja;
	private Double abonoCompra;
	private Double compraTotal;
	private Double ventaTotal;
	private Double total;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private Date fechaEliminacion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Double getFondoCaja() {
		return fondoCaja;
	}

	public void setFondoCaja(Double fondoCaja) {
		this.fondoCaja = fondoCaja;
	}

	public Double getAbonoCompra() {
		return abonoCompra;
	}

	public void setAbonoCompra(Double abonoCompra) {
		this.abonoCompra = abonoCompra;
	}

	public Double getCompraTotal() {
		return compraTotal;
	}

	public void setCompraTotal(Double compraTotal) {
		this.compraTotal = compraTotal;
	}

	public Double getVentaTotal() {
		return ventaTotal;
	}

	public void setVentaTotal(Double ventaTotal) {
		this.ventaTotal = ventaTotal;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Date getFechaEliminacion() {
		return fechaEliminacion;
	}

	public void setFechaEliminacion(Date fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}
	
}
