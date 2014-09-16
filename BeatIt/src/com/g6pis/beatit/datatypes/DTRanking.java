package com.g6pis.beatit.datatypes;

public class DTRanking {
	private String nombreUsuario;
	private Integer puntaje;
	private Integer posicion;
	private String imagen;
	
	public DTRanking(String nombreUsuario, Integer puntaje, Integer posicion,
			String imagen) {
		super();
		this.nombreUsuario = nombreUsuario;
		this.puntaje = puntaje;
		this.posicion = posicion;
		this.imagen = imagen;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public Integer getPuntaje() {
		return puntaje;
	}
	public void setPuntaje(Integer puntaje) {
		this.puntaje = puntaje;
	}
	public Integer getPosicion() {
		return posicion;
	}
	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
}
