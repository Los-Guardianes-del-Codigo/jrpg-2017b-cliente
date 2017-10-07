package mensajeria;

import java.io.Serializable;
import java.util.Map;

	public class PaqueteNpcs extends Paquete implements Serializable, Cloneable {

		private Map<Integer, PaqueteNpc> npcs;

		public PaqueteNpcs(){
			setComando(Comando.ACTUALIZARNPCS);
		}

		public PaqueteNpcs(Map<Integer, PaqueteNpc> npcs){
			this.npcs = npcs;
			setComando(Comando.ACTUALIZARNPCS);
		}

		public Map<Integer, PaqueteNpc> getNpcs(){
			return npcs;
		}

		@Override
		public Object clone() {
			Object obj = null;
			obj = super.clone();
			return obj;
		}
	}