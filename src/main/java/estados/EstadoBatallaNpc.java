package estados;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import dominio.Asesino;
import dominio.Casta;
import dominio.Elfo;
import dominio.Guerrero;
import dominio.Hechicero;
import dominio.Humano;
import dominio.NonPlayableCharacter;
import dominio.Orco;
import dominio.Personaje;
import interfaz.EstadoDePersonaje;
import interfaz.MenuBatalla;
import interfaz.MenuInfoPersonaje;
import juego.Juego;
import mensajeria.Comando;
import mensajeria.PaqueteAtacar;
import mensajeria.PaqueteBatalla;
import mensajeria.PaqueteFinalizarBatalla;
import mensajeria.PaqueteNpc;
import mensajeria.PaquetePersonaje;
import mundo.Mundo;
import recursos.Recursos;

public class EstadoBatallaNpc extends Estado {

	private Mundo mundo;
	private Personaje personaje;
	private NonPlayableCharacter enemigo;
	private int[] posMouse;
	private PaquetePersonaje paquetePersonaje;
	private PaqueteNpc paqueteNpc;
	// private PaqueteNpc paqueteEnemigo;
	// private PaqueteAtacar paqueteAtacar;
	// private PaqueteFinalizarBatalla paqueteFinalizarBatalla;
	private boolean miTurno;

	private boolean haySpellSeleccionada;
	private boolean seRealizoAccion;

	private Gson gson = new Gson();

	private BufferedImage miniaturaPersonaje;
	private BufferedImage miniaturaEnemigo;

	private MenuBatalla menuBatalla;

	public EstadoBatallaNpc(Juego juego, PaqueteNpc paqueteNpc) {
		super(juego);
		mundo = new Mundo(juego, "recursos/mundoBatalla.txt", "recursos/mundoBatallaCapaDos.txt");
		miTurno = true;

		// paquetePersonaje =
		// juego.getPersonajesConectados().get(paqueteBatalla.getId());
		paquetePersonaje = juego.getPersonaje();
		this.paqueteNpc = paqueteNpc;
		enemigo = new NonPlayableCharacter(paqueteNpc.getNombre(), paqueteNpc.getNivel(), paqueteNpc.getDificultad());

		// juego.getPersonajesConectados().get(paqueteBatalla.getIdEnemigo());

		crearPersonajes();

		menuBatalla = new MenuBatalla(miTurno, personaje);

		miniaturaEnemigo = Recursos.personaje.get(enemigo.getNombre()).get(5)[0];
		miniaturaPersonaje = Recursos.personaje.get(personaje.getNombreRaza()).get(5)[0];

		/*
		 * paqueteFinalizarBatalla = new PaqueteFinalizarBatalla();
		 * paqueteFinalizarBatalla.setId(personaje.getIdPersonaje());
		 * paqueteFinalizarBatalla.setIdEnemigo(enemigo.getIdPersonaje());
		 */
		// por defecto batalla perdida
		juego.getEstadoJuego().setHaySolicitud(true, juego.getPersonaje(), MenuInfoPersonaje.menuPerderBatalla);

		// limpio la accion del mouse
		juego.getHandlerMouse().setNuevoClick(false);

	}

	@Override
	public void actualizar() {

		juego.getCamara().setxOffset(-350);
		juego.getCamara().setyOffset(150);

		seRealizoAccion = false;
		haySpellSeleccionada = false;

		if (miTurno) {

			if (juego.getHandlerMouse().getNuevoClick()) {
				posMouse = juego.getHandlerMouse().getPosMouse();

				if (menuBatalla.clickEnMenu(posMouse[0], posMouse[1])) {

					if (menuBatalla.getBotonClickeado(posMouse[0], posMouse[1]) == 1) {
						if (personaje.puedeAtacar()) {
							seRealizoAccion = true;
							personaje.habilidadRaza1(enemigo);
						}
						haySpellSeleccionada = true;
					}

					if (menuBatalla.getBotonClickeado(posMouse[0], posMouse[1]) == 2) {
						if (personaje.puedeAtacar()) {
							seRealizoAccion = true;
							personaje.habilidadRaza2(enemigo);
						}
						haySpellSeleccionada = true;
					}

					if (menuBatalla.getBotonClickeado(posMouse[0], posMouse[1]) == 3) {
						if (personaje.puedeAtacar()) {
							seRealizoAccion = true;
							personaje.habilidadCasta1(enemigo);
						}
						haySpellSeleccionada = true;
					}

					if (menuBatalla.getBotonClickeado(posMouse[0], posMouse[1]) == 4) {
						if (personaje.puedeAtacar()) {
							seRealizoAccion = true;
							personaje.habilidadCasta2(enemigo);
						}
						haySpellSeleccionada = true;
					}

					if (menuBatalla.getBotonClickeado(posMouse[0], posMouse[1]) == 5) {
						if (personaje.puedeAtacar()) {
							seRealizoAccion = true;
							personaje.habilidadCasta3(enemigo);
						}
						haySpellSeleccionada = true;
					}

					if (menuBatalla.getBotonClickeado(posMouse[0], posMouse[1]) == 6) {
						seRealizoAccion = true;
						personaje.serEnergizado(10);
						haySpellSeleccionada = true;
					}
				}

				if (haySpellSeleccionada && seRealizoAccion) {
					if (!enemigo.estaVivo()) {
						juego.getEstadoJuego().setHaySolicitud(true, juego.getPersonaje(),
								MenuInfoPersonaje.menuGanarBatalla);
						if (personaje.ganarExperiencia(enemigo.getNivel() * 40)) {
							juego.getPersonaje().setNivel(personaje.getNivel());
							juego.getEstadoJuego().setHaySolicitud(true, juego.getPersonaje(),
									MenuInfoPersonaje.menuSubirNivel);
						}
						// paqueteFinalizarBatalla.setGanadorBatalla(juego.getPersonaje().getId());
						// ENVIAR AL SERVIDOR PAQUETE DEL NPC A ELIMINAR O A
						// CAMBIAR ESTADO ES_PELEABLE
						finalizarBatalla();
						Estado.setEstado(juego.getEstadoJuego());

					} else {
						/*
						 * paqueteAtacar = new
						 * PaqueteAtacar(paquetePersonaje.getId(),
						 * paqueteEnemigo.getId(), personaje.getSalud(),
						 * personaje.getEnergia(), enemigo.getSalud(),
						 * enemigo.getEnergia(), personaje.getDefensa(),
						 * enemigo.getDefensa(),
						 * personaje.getCasta().getProbabilidadEvitarDaño(),
						 * enemigo.getCasta().getProbabilidadEvitarDaño());
						 * enviarAtaque(paqueteAtacar);
						 */
						miTurno = false;
						menuBatalla.setHabilitado(false);
					}
				} else if (haySpellSeleccionada && !seRealizoAccion) {
					JOptionPane.showMessageDialog(null,
							"No posees la energía suficiente para realizar esta habilidad.");
				}

				juego.getHandlerMouse().setNuevoClick(false);
			}
		}

		if (miTurno == false) {
			enemigo.atacar(personaje);
			if (!personaje.estaVivo()) {
				finalizarBatalla();
				Estado.setEstado(juego.getEstadoJuego());
				juego.getUbicacionPersonaje().setPosX(0);
				juego.getUbicacionPersonaje().setPosY(0);
			}
			setMiTurno(true);

			// actualizar();
		}
	}

	@Override
	public void graficar(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, juego.getAncho(), juego.getAlto());
		mundo.graficar(g);

		g.drawImage(Recursos.personaje.get(paquetePersonaje.getRaza()).get(3)[0], 0, 175, 256, 256, null);
		g.drawImage(Recursos.personaje.get(enemigo.getNombre()).get(7)[0], 550, 75, 256, 256, null);

		mundo.graficarObstaculos(g);
		menuBatalla.graficar(g);

		g.setColor(Color.GREEN);

		EstadoDePersonaje.dibujarEstadoDePersonaje(g, 25, 5, personaje, miniaturaPersonaje);
		// EstadoDePersonaje.dibujarEstadoDePersonaje(g, 550, 5, enemigo,
		// miniaturaEnemigo);
		// System.out.println(enemigo.getNombre() + "\nSalud: " +
		// enemigo.getSalud() + "\nNivel: " + enemigo.getNivel());

	}

	private void crearPersonajes() {
		String nombre = paquetePersonaje.getNombre();
		int salud = paquetePersonaje.getSaludTope();
		int energia = paquetePersonaje.getEnergiaTope();
		int fuerza = paquetePersonaje.getFuerza();
		int destreza = paquetePersonaje.getDestreza();
		int inteligencia = paquetePersonaje.getInteligencia();
		int experiencia = paquetePersonaje.getExperiencia();
		int nivel = paquetePersonaje.getNivel();
		int id = paquetePersonaje.getId();

		Casta casta = null;
		try {
			casta = (Casta) Class.forName("dominio" + "." + paquetePersonaje.getCasta()).newInstance();
			personaje = (Personaje) Class.forName("dominio" + "." + paquetePersonaje.getRaza())
					.getConstructor(String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE,
							Casta.class, Integer.TYPE, Integer.TYPE, Integer.TYPE)
					.newInstance(nombre, salud, energia, fuerza, destreza, inteligencia, casta, experiencia, nivel, id);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			JOptionPane.showMessageDialog(null, "Error al crear la batalla");
		}

		// nombre = paqueteEnemigo.getNombre();
		nombre = enemigo.getNombre();
		salud = enemigo.getSalud();
		energia = 0;
		fuerza = enemigo.getAtaque();
		destreza = enemigo.getDefensa();
		inteligencia = enemigo.getMagia();
		experiencia = enemigo.otorgarExp();
		nivel = enemigo.getNivel();
		id = paqueteNpc.getIdPersonaje();

		casta = null;
		/*
		 * if (paqueteEnemigo.getCasta().equals("Guerrero")) { casta = new
		 * Guerrero(); } else if (paqueteEnemigo.getCasta().equals("Hechicero"))
		 * { casta = new Hechicero(); } else if
		 * (paqueteEnemigo.getCasta().equals("Asesino")) { casta = new
		 * Asesino(); }
		 * 
		 * if (paqueteEnemigo.getRaza().equals("Humano")) { enemigo = new
		 * Humano(nombre, salud, energia, fuerza, destreza, inteligencia, casta,
		 * experiencia, nivel, id); } else if
		 * (paqueteEnemigo.getRaza().equals("Orco")) { enemigo = new
		 * Orco(nombre, salud, energia, fuerza, destreza, inteligencia, casta,
		 * experiencia, nivel, id); } else if
		 * (paqueteEnemigo.getRaza().equals("Elfo")) { enemigo = new
		 * Elfo(nombre, salud, energia, fuerza, destreza, inteligencia, casta,
		 * experiencia, nivel, id); }
		 */
	}

	public void enviarAtaque(PaqueteAtacar paqueteAtacar) {
		try {
			juego.getCliente().getSalida().writeObject(gson.toJson(paqueteAtacar));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo la conexion con el servidor.");
		}
	}

	private void finalizarBatalla() {
		try {
			// juego.getCliente().getSalida().writeObject(gson.toJson(paqueteFinalizarBatalla));

			paquetePersonaje.setSaludTope(personaje.getSaludTope());
			paquetePersonaje.setEnergiaTope(personaje.getEnergiaTope());
			paquetePersonaje.setNivel(personaje.getNivel());
			paquetePersonaje.setExperiencia(personaje.getExperiencia());
			paquetePersonaje.setDestreza(personaje.getDestreza());
			paquetePersonaje.setFuerza(personaje.getFuerza());
			paquetePersonaje.setInteligencia(personaje.getInteligencia());
			paquetePersonaje.removerBonus();

			/*
			 * paqueteEnemigo.setSaludTope(enemigo.getSaludTope());
			 * paqueteEnemigo.setEnergiaTope(enemigo.getEnergiaTope());
			 * paqueteEnemigo.setNivel(enemigo.getNivel());
			 * paqueteEnemigo.setExperiencia(enemigo.getExperiencia());
			 * paqueteEnemigo.setDestreza(enemigo.getDestreza());
			 * paqueteEnemigo.setFuerza(enemigo.getFuerza());
			 * paqueteEnemigo.setInteligencia(enemigo.getInteligencia());
			 * paqueteEnemigo.removerBonus();
			 */
			paquetePersonaje.setComando(Comando.ACTUALIZARPERSONAJE);
			paqueteNpc.setComando(Comando.ACTUALIZARNPC);

			juego.getCliente().getSalida().writeObject(gson.toJson(paquetePersonaje));
			juego.getCliente().getSalida().writeObject(gson.toJson(paqueteNpc));
			juego.getNpcs().remove(paqueteNpc.getIdPersonaje());
			System.out.println("Llego a enviar el paquete...");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo la conexión con el servidor");
		}
	}

	public PaquetePersonaje getPaquetePersonaje() {
		return paquetePersonaje;
	}

	/*
	 * public PaquetePersonaje getPaqueteEnemigo() { return paqueteEnemigo; }
	 */
	public void setMiTurno(boolean b) {
		miTurno = b;
		menuBatalla.setHabilitado(b);
		juego.getHandlerMouse().setNuevoClick(false);
	}

	public Personaje getPersonaje() {
		return personaje;
	}

	/*
	 * public Personaje getEnemigo() { return enemigo; }
	 */
	@Override
	public boolean esEstadoDeJuego() {
		return false;
	}
}