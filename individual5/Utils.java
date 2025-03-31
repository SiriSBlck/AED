package aed.individual5;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;
import es.upm.aedlib.tree.*;

public class Utils {
  public static <E> PositionList<Position<E>> longestPath(Tree<E> t) {
    PositionList<Position<E>> caminoMax = new NodePositionList<>();

    // Si el árbol está vacío, devolvemos una lista vacía.
    if (t.isEmpty()) {
        return caminoMax;
    }
    // Llamada inicial al método recursivo para encontrar el camino más largo.
    longestPathAux(t, t.root(), new NodePositionList<>(), caminoMax);
    return caminoMax;
  }

  private static <E> void longestPathAux(Tree<E> t, Position<E> act, PositionList<Position<E>> camino, PositionList<Position<E>> caminoMax) {
    camino.addLast(act);
    if (t.isExternal(act)) {
        if (camino.size() > caminoMax.size()) { //Si el camino explorado es más largo que el almacenado como más largo
            while (!caminoMax.isEmpty()) { //vaciamos el camino más largo
                caminoMax.remove(caminoMax.first());
            }
           
            for (Position<E> node : camino) { //guardamos el camino más largo 
                caminoMax.addLast(node);
            }
        }
    } else {
        for (Position<E> child : t.children(act)) {
          longestPathAux(t, child, camino, caminoMax);
        }
    }
    camino.remove(camino.last()); //retrocedemos a la última posición de partida en lugar de empezar desde la raíz
  }
}	
