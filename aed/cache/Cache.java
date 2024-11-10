package aed.cache;

import es.upm.aedlib.Entry;
import es.upm.aedlib.Position;
import es.upm.aedlib.map.*;
import es.upm.aedlib.positionlist.*;


public class Cache<Key,Value> {
  

  // Tamano de la cache
  private int maxCacheSize;

  // NO MODIFICA ESTOS ATTRIBUTOS, NI CAMBIA SUS NOMBRES: mainMemory, cacheContents, keyListLRU

  // Para acceder a la memoria M
  private Storage<Key,Value> mainMemory;
  // Un 'map' que asocia una clave con un ``CacheCell''
  private Map<Key,CacheCell<Key,Value>> cacheContents;
  // Una PositionList que guarda las claves en orden de
  // uso -- la clave mas recientemente usado sera el keyListLRU.first()
  private PositionList<Key> keyListLRU;
  


  // Constructor de la cache. Especifica el tamano maximo 
  // y la memoria que se va a utilizar
  public Cache(int maxCacheSize, Storage<Key,Value> mainMemory) {
    this.maxCacheSize = maxCacheSize;

    // NO CAMBIA
    this.mainMemory = mainMemory;
    this.cacheContents = new HashTableMap<Key,CacheCell<Key,Value>>();
    this.keyListLRU = new NodePositionList<Key>();
  }
  


  // Devuelve el valor que corresponde a una clave "Key"
  public Value get(Key key) {
	  CacheCell <Key, Value> cacheCell = this.cacheContents.get(key);

	  if(cacheCell != null) {
		  actualizarLRU(key, cacheCell);
		  return cacheCell.getValue();
	  }

		  Value valorDeMemoria = this.mainMemory.read(key);

		  if(valorDeMemoria != null) {
			  put(key, valorDeMemoria);
			  return valorDeMemoria;
		  }
		  return null;
  }

  
  // Establece un valor nuevo para la clave en la memoria cache
  public void put(Key key, Value value) {
      CacheCell<Key, Value> cacheCell = this.cacheContents.get(key);

      if (cacheCell != null) { 
          cacheCell.setValue(value);
          cacheCell.setDirty(true);
          actualizarLRU(key, cacheCell);
      } 

      else {
        if (this.cacheContents.size() >= this.maxCacheSize) { //Si no queda espacio en la memoria caché
          Position<Key> ultPos = this.keyListLRU.last(); // obtenemos un Position que referencia a la llave del elemento que lleva más tiempo sin ser accedido
          Key menosUsado = ultPos.element(); //Obtenemos el valor de dicha llave
          CacheCell<Key, Value> cellToRemove = this.cacheContents.get(menosUsado); //Creamos un CacheCell nuevo con la llave dada

          if (cellToRemove != null && cellToRemove.getDirty()) { //En caso de que haya habido modificaciones, actualizamos la memoria principal
            this.mainMemory.write(menosUsado, cellToRemove.getValue());
          }

          this.keyListLRU.remove(ultPos); //Eliminamos la posición modificada hace más tiempo de la lista de posiciones modificadas
          this.cacheContents.remove(menosUsado); //Eliminamos el elemento de la memoria caché
        }

        //Si aún hay espacio en la memoria caché
        CacheCell<Key, Value> nuevaCacheCell = new CacheCell<>(value, true, null);

        this.cacheContents.put(key, nuevaCacheCell); //Añadimos una nueva CacheCell con los parámetros dados
        this.keyListLRU.addFirst(key); //Añadimos la CacheCell como primer elemento(elemento modificado más recientemente) de la lista de elementos modificados
        nuevaCacheCell.setPos(this.keyListLRU.first()); //Indicamos la posición que ocupa en esa lista a la CacheCell nueva
      } 
  }
  

  //Método auxiliar con el fin de evitar duplicidad de código
  private void actualizarLRU (Key key, CacheCell<Key, Value> cacheCell) {
	  Position<Key> pos = cacheCell.getPos();

	  if(pos != null) {
		  this.keyListLRU.remove(pos);
	  }
    
	  this.keyListLRU.addFirst(key);
	  cacheCell.setPos(this.keyListLRU.first());
  }


  // NO CAMBIA
  public String toString() {
    return "cache";
  }
}


