package aed.individual3;

import java.util.Iterator;

public class Utils {
  public static boolean isArithmeticSequence(Iterable<Integer> l) {
    Iterator<Integer> it = l.iterator();

    if(!it.hasNext()){
      return true;
    } // si hay un solo elemento

    Integer itValPos = it.next();

    if(!it.hasNext()){
      return  true;
    } //si solo hay dos elementos

    boolean res = true;
    it = l.iterator();
    Integer itValPrev = it.next(); //guarda el primer Integer del iterable
    itValPos = it.next(); //guarda el segundo Integer del iterable
    int sum = itValPos - itValPrev;

    while (it.hasNext() && res) {

      if(itValPos == null){ //sabemos que existe siguiente elemento por lo que al menos avanzamos una posición 
                            // y volvemos a comprobar en la siguiente iteración del bucle while
          itValPos = it.next();

      } else if(itValPrev == null){ //en caso de que el primer elemento de la lista sea null
        itValPrev = it.next();

      } else { // si ninguno de los elementos es null se calcula la suma y se compara con la anterior
        res = (sum == (itValPos - itValPrev));
        sum = itValPos - itValPrev;
        
        if(it.hasNext()){ // si existe siguiente elemento en la lista, se avanzan las variables a los siguientes elementos, evitando que se solapen
          itValPrev = itValPos;
          itValPos = it.next();
        }
      } //else
    } //while

    return res && sum == (itValPos - itValPrev); // se retorna la variable booleana y se realiza la última comprobación
  } //isArithmeticSequence
}
