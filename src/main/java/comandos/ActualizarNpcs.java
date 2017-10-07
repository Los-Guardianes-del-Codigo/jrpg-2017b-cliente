package comandos;

import mensajeria.PaqueteNpcs;

public class ActualizarNpcs extends ComandosEscucha{

	@Override
	public void ejecutar() {
		PaqueteNpcs npcs = (PaqueteNpcs) gson.fromJson(cadenaLeida,PaqueteNpcs.class);
		juego.setNpcs(npcs.getNpcs());
	}

}
