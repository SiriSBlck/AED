package aed.multisets;

import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.positionlist.NodePositionList;


/**
 * Una implementacion de un multiset (multiconjunto) a traves de una lista
 * de posiciones.
 */
public class MultiSetList<E> implements MultiSet<E> {
  
  /**
   * La estructura de datos que guarda los elementos del multiset.
   */
  private PositionList<Pair<E,Integer>> elements;
  
  /**
   * El tamaño del multiset.
   */
  private int size;
  
  /**
   * Construye un multiset vacio.
   */
  public MultiSetList() {
    this.elements = new NodePositionList<Pair<E,Integer>>();
    this.size = 0;
  }

  @Override
  public void add(E elem, int n) throws IllegalArgumentException{
    if(n < 0){
      throw new IllegalArgumentException();
    }
    int mult = this.multiplicity(elem);
    System.out.println(mult);
    if(mult > 0 && n != 0){
      Position<Pair<E,Integer>> cent;

      //Sabemos que el elemento está en la lista por lo que basta con comprobar hasta el penúltimo elemento
      // for(cent = this.elements.first(); this.elements.next(cent) != null && !cent.element().equals(elem); cent = this.elements.next(cent));
      // cent.element().setRight(cent.element().getRight() + n);
      boolean enc = false;

      for(cent = this.elements.first(); cent != null && !enc; cent = this.elements.next(cent)){
        if(cent.element().getLeft().equals(elem)) {
          cent.element().setRight(this.multiplicity(cent.element().getLeft()) + n);
          enc = true;
        }
      }
    } else if(n != 0){
      this.elements.addLast(new Pair<>(elem, n));
    }
    this.size += n;
  }
  
  //@Override
  public void add2(E elem, int n) throws IllegalArgumentException{
	  //Comprueba si n es negativo y lanza una excepcion en dicho caso
	  if(n < 0) {
		  throw new IllegalArgumentException();
	  }

	  //Creamos un contador para controlar que se insertan el numero de elementos correctos y usamos un cursor para recorrer la lista y buscar si ya existe el elemento
	  Position<Pair<E, Integer>> cursor = elements.first();
	  int contador = 0;

	  //El bucle se detendrá cuando el contador sea igual a n
	  while(cursor != null && contador < n) {
		  Pair<E, Integer> pair = cursor.element();

		  //Si encontramos el elemnto en la lista, actualizamos su frecuencia.
		  //Usamos getLeft() para obtener el elemnto y aumentamos la cantidad con setRight()
		  if(pair.getLeft().equals(elem)) {
			  pair.setRight(pair.getRight() + n);
			  contador = n;
		  }
		  else{
			  //Avanzamos al siguiente par en la lista
			  cursor = elements.next(cursor);
		  }
	  }

	  //En caso de que el elemento no se encuentre en la lista el contador será igual a 0 y añadiremos el nuevo elemento con su frecuencia
	  if(contador==0) {
		  elements.addLast(new Pair<>(elem, n));
	  }
	  size += n;
  }

  @Override
  public int remove(E elem, int n) {
    if(n < 0){
      throw new IllegalArgumentException("El número de elementos a eliminar debe ser mayor que 0.");
    }

    if(this.elements.isEmpty() || this.multiplicity(elem) < n){
      return 0;
    }

    Position<Pair<E,Integer>> cent;
    // boolean enc = false;

    for(cent = this.elements.first(); this.elements.next(cent) != null && !cent.element().getLeft().equals(elem); cent = this.elements.next(cent));
    // if(cent.element().getLeft().equals(elem)){
      cent.element().setRight(cent.element().getRight() - n);
      this.elements.set(cent, cent.element());

      if(cent.element().getRight() == 0){
        this.elements.remove(cent);
      }
      
      this.size -= n;

    return n;
  }

  @Override
  public int multiplicity(E elem) {
    int res = 0;
    for(Position<Pair<E,Integer>> cent = this.elements.first(); cent != null && res == 0; cent = this.elements.next(cent)){
      if(cent.element().getLeft().equals(elem)){
        res = cent.element().getRight();
      }
    }
    return res;
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
  }

  @Override
  public PositionList<E> elements() {
    PositionList<E> res = new NodePositionList<>();

    for(Position<Pair<E,Integer>> cent = this.elements.first(); cent != null; cent = this.elements.next(cent)){
      if(cent.element().getRight() > 0){
        res.addLast(cent.element().getLeft());
      }
    }

   return res;
  }

////////////////////////////////////////////////
  @Override
  public MultiSet<E> sum(MultiSet<E> s) {
    MultiSetList<E> res = new MultiSetList();

    PositionList<E> elems = this.elements();
    Position<E> cent1;

    for(cent1 = elems.first(); cent1 != null; cent1 = elems.next(cent1)){
      res.add(cent1.element(), this.multiplicity(cent1.element())); 
    }

    elems = s.elements();

    for(cent1 = elems.first(); cent1 != null; cent1 = elems.next(cent1)){
      res.add(cent1.element(), s.multiplicity(cent1.element())); 
    }

    return res;
  }


  public MultiSet<E> sum2(MultiSet<E> s) {
    MultiSetList<E> res = new MultiSetList();

    if(s.isEmpty() || this.isEmpty()){
      return res;
    }

    res = this;
    PositionList<E> elemsS = s.elements();
    PositionList<E> elemsThis = this.elements();
    MultiSet<E> elemsC = s.intersection(this);
    PositionList<E> elemsComun = elemsC.elements();

    Position<E> cent1 = elemsComun.first();

    for(int i = 0; i < elemsComun.size() && cent1 != null; i++){
      res.add(cent1.element(), s.multiplicity(cent1.element()) + this.multiplicity(cent1.element()));
      elemsS.remove(cent1);
      elemsThis.remove(cent1);
      if(elemsComun.next(cent1) != null){
        cent1 = elemsComun.next(cent1);
      }
    }

    cent1 = elemsS.first();
    Position<E> cent2 = elemsThis.first();

    int longMax = (elemsS.size() < elemsThis.size()) ? elemsThis.size() : elemsS.size();
    for(int i = 0; i < longMax; i++){
      if(i < elemsThis.size()){
        res.add(cent2.element(), this.multiplicity(cent2.element()));
        cent2 = elemsThis.next(cent2);
      }
      if(i < elemsS.size()){
        res.add(cent1.element(), s.multiplicity(cent1.element()));
        cent1 = elemsS.next(cent1);
      }
    }

    return res;
  }

  @Override
  public MultiSet<E> minus(MultiSet<E> s) {
    MultiSet<E> res = new MultiSetList();

    if(s.isEmpty() || this.isEmpty()){
      return res;
    }

    PositionList<E> elemsS = s.elements();
    PositionList<E> elemsThis = this.elements();
    Position<E> cent1;

    for(cent1 = elemsThis.first(); cent1 != null; cent1 = elemsThis.next(cent1)){
      int multiplicidadMayor = (0 < this.multiplicity(cent1.element()) - s.multiplicity(cent1.element())) ? 
                                            this.multiplicity(cent1.element()) - s.multiplicity(cent1.element()) : 0;
      res.add(cent1.element(), multiplicidadMayor);
    }

    return res;
  }


  public MultiSet<E> minus2(MultiSet<E> s) {
    MultiSet<E> res = new MultiSetList();

    if(s.isEmpty() || this.isEmpty()){
      return res;
    }

    PositionList<E> elemsS = s.elements();
    PositionList<E> elemsThis = this.elements();
    MultiSet<E> elemsC = this.intersection(s);
    PositionList<E> elemsComun = elemsC.elements();

    Position<E> cent1 = elemsComun.first();

    for(int i = 0; i < elemsComun.size(); i++){
      int multiplicidadMayor = (s.multiplicity(cent1.element()) < this.multiplicity(cent1.element())) ? 
                                      this.multiplicity(cent1.element()) : s.multiplicity(cent1.element());
      res.add(cent1.element(), multiplicidadMayor);
      elemsS.remove(cent1); ///////////////////necesita el centinela dentro de su lista, no tiene xk tener la misma pos aqui que en elemsComun...
      elemsThis.remove(cent1);
      cent1 = elemsComun.next(cent1);
    }

    cent1 = elemsS.first();
    Position<E> cent2 = elemsThis.first();

    int longMax = (elemsS.size() < elemsThis.size()) ? elemsThis.size() : elemsS.size();
    for(int i = 0; i < longMax; i++){
      if(i < elemsThis.size()){
        res.add(cent2.element(), this.multiplicity(cent2.element()));
        cent2 = elemsThis.next(cent2);
      }
      if(i < elemsS.size()){
        res.add(cent1.element(), s.multiplicity(cent1.element()));
        cent1 = elemsS.next(cent1);
      }
    }

    return res;
  }

  @Override
  public MultiSet<E> intersection(MultiSet<E> s) {
    MultiSet<E> res = new MultiSetList();

    if(s.isEmpty() || this.isEmpty()){
      return res;
    }

    PositionList<E> elemsL = s.elements();
    PositionList<E> elemsP = this.elements();
    PositionList<E> elemsAux = elemsL;

    elemsL = (elemsL.size() < elemsP.size()) ? elemsP : elemsL; 
    elemsP = (elemsL.size() < elemsP.size()) ? elemsAux : elemsP;
    
    for(Position<E> centL = elemsL.first(); centL != null; centL = elemsL.next(centL)){
      int tam = res.size();
      for(Position<E> centP = elemsP.first(); centP != null && tam == res.size(); centP = elemsP.next(centP)){
        if(centL.element().equals(centP.element())){
          res.add(centL.element(), (s.multiplicity(centL.element()) < this.multiplicity(centP.element())) ? 
                                                            s.multiplicity(centL.element()) : this.multiplicity(centP.element()));
        }
      }
    }

    return res;
  }

  @Override
  public boolean subsetEqual(MultiSet<E> s) {
    MultiSet<E> intersection = this.intersection(s);
    PositionList<E> elemsI = intersection.elements();

    if(this.isEmpty() && s.isEmpty()){
      return true;
    }

    Position<E> centI = elemsI.first();

    if(centI == null){
      return false;
    }

    for(centI = elemsI.first(); centI != null && (s.multiplicity(centI.element())>= this.multiplicity(centI.element()))
                    && s.multiplicity(centI.element()) > 0 && this.multiplicity(centI.element()) > 0; centI = elemsI.next(centI));

    return centI == null && this.elements().size() <= s.elements().size();
  }
  
}
