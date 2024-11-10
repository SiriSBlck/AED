package aed.individual4;

import java.util.Iterator;
import java.util.NoSuchElementException;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.PositionList;

public class OrderedIterator implements Iterator<Integer> {

  private Position<Integer> posAct;
  private PositionList<Integer> lista;

  public OrderedIterator(PositionList<Integer> list) {
    this.lista = list;
    this.posAct = (list.size() >= 1) ? list.first() : null;
  }

  @Override
  public boolean hasNext() {
    if(lista == null || lista.isEmpty() || lista.size() == 1){
      return false;
    }

    return this.posAct != null;
  }


@Override
  public Integer next() {
    if(lista == null || lista.isEmpty() || lista.size() == 1 || this.posAct == null){
      throw new NoSuchElementException();
    }

    boolean enc = false;
    Integer act = this.posAct.element();

    for(Position<Integer> aux = lista.next(this.posAct); aux != null && !enc; aux = lista.next(aux)){
      if(aux.element().intValue() >= this.posAct.element().intValue()){
        this.posAct = aux;
        enc = true;
      }
    }

    this.posAct = enc ?  this.posAct : null;
   
    return act;
  }
}
