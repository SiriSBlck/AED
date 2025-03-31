package aed.delivery;

import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.graph.DirectedGraph;
import es.upm.aedlib.Position;
import es.upm.aedlib.graph.DirectedAdjacencyListGraph;
import es.upm.aedlib.graph.Vertex;
import es.upm.aedlib.indexedlist.ArrayIndexedList;
import es.upm.aedlib.indexedlist.IndexedList;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.graph.Edge;
import es.upm.aedlib.set.HashTableMapSet;
import es.upm.aedlib.set.Set;
import java.util.Iterator;

public class Delivery2<V> {  
  private DirectedGraph<V, Integer> terrain;

  // Construct a terrain out of a series of vertices and an adjacency matrix.
  // There are 'len' vertices. A null means no connection. A non-negative
  // number represents distance between nodes.
  public Delivery2(V[] places, Integer[][] gmat) {
    if(places != null){
      this.terrain = new DirectedAdjacencyListGraph<>();
      HashTableMap<V, Vertex<V>> map = new HashTableMap<>();

      for(int i = 0; i < places.length; i++){ //Por cada vértice
        if(!map.containsKey(places[i])) map.put(places[i], this.terrain.insertVertex(places[i])); //si no existe en nuestro grafo, lo añadimos
        for(int j = 0; j < places.length; j++){ //revisamos sus vértices adyacentes
          Integer weight = gmat[i][j]; // y el peso de las mismas, si existen
          if(weight != null && weight > 0){ 
            //si no existe el vértice ady. en nuestro grafo, lo añadimos
            if(!map.containsKey(places[j])) map.put(places[j], this.terrain.insertVertex(places[j])); 
            this.terrain.insertDirectedEdge(map.get(places[i]), map.get(places[j]), weight); //y añadimos la arista que los conecta
          }
        }
      }
    }
  }

  public PositionList<Vertex<V>> tour() {
    PositionList<Vertex<V>> path = new NodePositionList<>();
    HashTableMapSet<Vertex<V>> visited = new HashTableMapSet<>();

    for(Vertex<V> ver: this.terrain.vertices()) {
      if(tourAux(visited, ver, path)) {
        return path;
      }
      path = new NodePositionList<>();
      visited = new HashTableMapSet<>();
    }

    return null;
  }

  private boolean tourAux(HashTableMapSet<Vertex<V>> visited, Vertex<V> verAct, PositionList<Vertex<V>> path) {
    if(visited.contains(verAct)) {
      return false;
    }

    path.addLast(verAct);
    visited.add(verAct);

    if(path.size() == this.terrain.size()) {
      return true;
    }

    boolean found = false;
    Iterator<Edge<Integer>> it = this.terrain.outgoingEdges(verAct).iterator();

    while(it.hasNext() && !found) {
      found = tourAux(visited, terrain.endVertex(it.next()), path);
    }

    if(!found) {
      path.remove(path.last());
      visited.remove(verAct);
    }
    return found;
  }
  
  // Just return the terrain that was constructed
  public DirectedGraph<V, Integer> getGraph() {
    return this.terrain;
  }

  public int length(PositionList<Vertex<V>> path) { 
    if(path.size() < 2) {
      return 0;
    }

    int length = 0;
    Position<Vertex<V>> act = path.first();
    Position<Vertex<V>> next = path.next(act);

    while(next != null) {   
      boolean found = false;
      Iterable<Edge<Integer>> edges = this.terrain.outgoingEdges(act.element());
      for(Iterator<Edge<Integer>> it = edges.iterator(); it.hasNext() && !found; ) {
        Edge<Integer> edg = it.next();
        if(this.terrain.endVertex(edg).element().equals(next.element().element())){
          length += edg.element();
          found = true;
        }
      }
      act = next;
      next = path.next(next);
    }
    
    return length;
  }

  public String toString() {
    return "Delivery";
  }
}
