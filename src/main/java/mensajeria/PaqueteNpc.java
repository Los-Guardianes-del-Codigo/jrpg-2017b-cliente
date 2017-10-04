package mensajeria;

import java.io.Serializable;

public class PaqueteNpc extends Paquete implements Serializable, Cloneable {

	private int id;
	private float posX;
	private float posY;
	private int direccion;
	private int frame;
	private String nombre;
	private int nivel;
	private int dificultad;

	public PaqueteNpc() {
		setComando(Comando.ACTUALIZARNPCS);
	}

	public PaqueteNpc(int idNpc) {
		id = idNpc;
		setComando(Comando.ACTUALIZARNPCS);
	}

	public PaqueteNpc(int idPersonaje, float posX, float posY) {
		this.id = idPersonaje;
		this.posX = posX;
		this.posY = posY;
		this.direccion=0;
		this.frame=0;
		setComando(Comando.ACTUALIZARNPCS);
	}

	public int getIdPersonaje() {
		return id;
	}

	public void setIdPersonaje(int idNpc) {
		this.id = idNpc;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public int getDireccion() {
		return direccion;
	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getDificultad() {
		return dificultad;
	}

	public void setDificultad(int dificultad) {
		this.dificultad = dificultad;
	}

	@Override
	public Object clone() {
		Object obj = null;
		obj = super.clone();
		return obj;
	}

}

