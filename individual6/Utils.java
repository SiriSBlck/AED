package aed.individual6;


import es.upm.aedlib.positionlist.*;
import es.upm.aedlib.set.*;
import es.upm.aedlib.graph.*;
import es.upm.aedlib.map.HashTableMap;


public class Utils {

  /**
   * Devuelve un conjunto con todos los vertices alcanzables desde AMBOS
   * v1 y v2.
   */
  public static <V> Set<Vertex<V>> reachableFromBoth(DirectedGraph<V,Boolean> g, Vertex<V> v1, Vertex<V> v2) {
    if(g.isEmpty() || v1 == null || v2 == null){
      return null;
    }
    HashTableMapSet<Vertex<V>> vertex = new HashTableMapSet<>();
    HashTableMap<Vertex<V>, Void> mapV1 = reachable(g, v1);

    if(v1.equals(v2)){
      mapV1 = reachable(g, v1);
      for(Vertex<V> ver1 : mapV1.keys()){
        vertex.add(ver1);
      }
      vertex.add(v1);
      return vertex;
    }

    HashTableMap<Vertex<V>, Void> mapV2 = reachable(g, v2);
    if(mapV2.containsKey(v1)){
      vertex.add(v1);
    }

    if(mapV1.containsKey(v2)){
      vertex.add(v2);
    }

    for(Vertex<V> ver1 : mapV1.keys()){
        if(mapV2.containsKey(ver1)){
          vertex.add(ver1);
        }
    }

    return vertex;
  }

  public static <V> HashTableMap<Vertex<V>, Void> reachable(DirectedGraph<V,Boolean> g, Vertex<V> v) {
    Iterable<Edge<Boolean>> adjacent = g.outgoingEdges(v);
    HashTableMap<Vertex<V>, Void> map = new HashTableMap<>();
    HashTableMap<Vertex<V>, Void> children = new HashTableMap<>();

    for(Edge<Boolean> ver : adjacent){ // añadimos todos los hijos al mapa
      if(ver.element()){
        Vertex<V> child = g.endVertex(ver);
        map.put(child, null);
        children.put(child, null);
      }
    }

    while(!children.isEmpty()){
      for(Vertex<V> ver : children.keys()){ //para cada hijo
        adjacent = g.outgoingEdges(ver);
        for(Edge<Boolean> vertex : adjacent){ // añadimos todos los hijos al mapa
          if(vertex.element()){
            Vertex<V> child = g.endVertex(vertex);
            map.put(child, null);
            children.put(child, null);
          }
        }
        children.remove(ver); //eliminamos el padre para la próxima iteración
      }
    }
    return map;
  }
  
  /**
   * Devuelve un camino (una lista de aristas) que llevan desde from y to,
   * donde la suma de los elementos de las aristas del camino <= limit.
   * Si no existe ningun camino que cumple con esta restriccion se devuelve
   * el valor null. 
   */
  public static <V> PositionList<Edge<Integer>> existsPathLess(UndirectedGraph<V, Integer> g, Vertex<V> from, Vertex<V> to, int limit) {
    if (!(g == null || from == null || to == null || limit <= 0 || g.isEmpty())) {   
      PositionList<Edge<Integer>> path = new NodePositionList<>();
      HashTableMap<Vertex<V>, Integer> visited = new HashTableMap<>();

      if (findPath(g, from, to, limit, path, visited, 0)) {
          return path;
      }
    }

    return null;
}

  private static <V> boolean findPath(UndirectedGraph<V, Integer> g, Vertex<V> current, Vertex<V> to, int limit,
                                    PositionList<Edge<Integer>> path, HashTableMap<Vertex<V>, Integer> visited, int currentWeight) {
    if (current.equals(to)) {
        return true;
    }

    visited.put(current, currentWeight);

    for (Edge<Integer> edg : g.edges(current)) {
      Vertex<V> newCurrent = g.opposite(current, edg);
      int weight = edg.element();
      //Si ya está visitado o tiene un peso mayor que el actual
      if (weight + currentWeight < limit && (!visited.containsKey(newCurrent) || visited.get(newCurrent) > weight + currentWeight)) {
        path.addLast(edg);
        if (findPath(g, newCurrent, to, limit, path, visited, weight + currentWeight)) {
            return true;
        }
        path.remove(path.last()); // Retrocedemos si el camino no es válido.
      }
    }

    visited.remove(current); // Eliminamos el nodo actual para permitir otras exploraciones.
    return false;
}

}

