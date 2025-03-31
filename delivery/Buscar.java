package aed.delivery;

import java.util.Iterator;

import es.upm.aedlib.Pair;
import es.upm.aedlib.positionlist.*;


public class Buscar {
  
  public static Pair<String,PositionList<Direccion>> busca(Laberinto laberinto) {
   PositionList<Direccion> camino = new NodePositionList<>();
   
   if(explorarLaberinto(laberinto, camino)) {
	   return new Pair<>(laberinto.getRegalo(), camino);
   }
   return null;
  }
  
  private static boolean explorarLaberinto (Laberinto laberinto, PositionList<Direccion> camino) {
	  if(laberinto.tieneRegalo()) {
		  return true;
	  }
	  
	  if(laberinto.sueloMarcadoConTiza()) {
		  return false;
	  }
	  
	  laberinto.marcaSueloConTiza();
	  
	  for(Direccion direccion : laberinto.direccionesPosibles()) {
			laberinto.moverHacia(direccion);
			camino.addLast(direccion);
		  
		  if(explorarLaberinto(laberinto, camino)) {
			  return true;
		  }
		  
		  camino.remove(camino.last());
		  laberinto.moverHacia(Direccion.opuesto(direccion));
	  }
	  return false;
  }
  
  
}
