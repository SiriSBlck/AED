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
	  //Si la cantidad a agregar es negativa, lanzamos una excepción
	  if(n < 0){
		  throw new IllegalArgumentException();
	  }
	  //Calculamos la multiplicidad actual del elemento
	  int mult = this.multiplicity(elem);
	  //Si el elemento ya esxiste en el conjunto y n es distinto de 0
	  if(mult > 0 && n != 0){
		  Position<Pair<E,Integer>> cent;
		  boolean enc = false;

		  //Recorremos la lista hasta encontrar el elemento o llegar al final
		  for(cent = this.elements.first(); cent != null && !enc; cent = this.elements.next(cent)){
			  //Si encontramos el elemento en la lista
			  if(cent.element().getLeft().equals(elem)) {
				  //Actualizamos la multiplicidad sumando n a la multiplicidad actual
				  cent.element().setRight(this.multiplicity(cent.element().getLeft()) + n);
				  enc = true;
			  }
		  }
	  } 
	  //Si el elemento no existe en la lista y n no es 0
	  else if(n != 0){
		  //Agregamos el nuevo elemento con su respectiva multiplicidad al final de la lista
		  this.elements.addLast(new Pair<>(elem, n));
	  }
	  //Actualizamos el tamaño total del multiconjunto
	  this.size += n;
  }

  @Override
  public int remove(E elem, int n) {
	//Si la cantidad a eliminar es negativa, lanzamos una excepción
	  if(n < 0){
		  throw new IllegalArgumentException("El número de elementos a eliminar debe ser mayor que 0.");
	  }

	  //Si la lista está vacía o la multiplicidad del elemento es menos que n, no se puede eliminar
	  if(this.elements.isEmpty() || this.multiplicity(elem) < n){
		  return 0;
	  }

	  Position<Pair<E,Integer>> cent;

	  //Ahora que sabemos que el elemento está en la lista y hay suficientes copias recorremos la lista hasta encontrar el elemento
	  for(cent = this.elements.first(); this.elements.next(cent) != null && !cent.element().getLeft().equals(elem); cent = this.elements.next(cent));
	  //Reducimos la multiplicidad del elemento en n
	  cent.element().setRight(cent.element().getRight() - n);
	  this.elements.set(cent, cent.element());

	  //Si la multiplicidad llega a 0, eliminamos el elemento de la lista
	  if(cent.element().getRight() == 0){
		  this.elements.remove(cent);
	  }

	  //Actualizamos el tamaño del multiconjunto
	  this.size -= n;

	  //Devolvemos la cantidad eliminada
	  return n;
  }

  @Override
  public int multiplicity(E elem) {
	  int res = 0;
	  
	  //Recorremos la lista para encontrar el elemento
	  for(Position<Pair<E,Integer>> cent = this.elements.first(); cent != null && res == 0; cent = this.elements.next(cent)){
		  //Si encontramos el elemento, guardamos su multiplicidad
		  if(cent.element().getLeft().equals(elem)){
			  res = cent.element().getRight();
		  }
	  }
	  
	  //Devolvemos la multiplicidad (0 si no se encontró el elemento)
	  return res;
  }

  @Override
  public int size() {
	  //Devuelve el tamaño total del multiconjunto
	  return this.size;
  }

  @Override
  public boolean isEmpty() {
	  //Devuelve si el multiconjunto está vacío
	  return this.size == 0;
  }

  @Override
  public PositionList<E> elements() {
	  PositionList<E> res = new NodePositionList<>();

	  //Recorremos todos los elementos del multiconjunto
	  for(Position<Pair<E,Integer>> cent = this.elements.first(); cent != null; cent = this.elements.next(cent)){
		  //Solo añadimos elementos que tengan una multiplicidad mayor que 0
		  if(cent.element().getRight() > 0){
			  res.addLast(cent.element().getLeft());
		  }
	  }

	  //Devolvemos la lista de elementos
	  return res;
  }

  @Override
  public MultiSet<E> sum(MultiSet<E> s) {
	  MultiSetList<E> res = new MultiSetList<>();

	  //Obtenemos todos los elementos del multiconjunto actual
	  PositionList<E> elems = this.elements();
	  Position<E> cent1;

	  //Agregamos todos los elementos del multiconjunto actual a la suma
	  for(cent1 = elems.first(); cent1 != null; cent1 = elems.next(cent1)){
		  res.add(cent1.element(), this.multiplicity(cent1.element())); 
	  }

	  //Obtenemos todos los elementos del multiconjunto dado como parámetro
	  elems = s.elements();

	  //Añadimos todos los elementos del segundo multiconjunto a la suma
	  for(cent1 = elems.first(); cent1 != null; cent1 = elems.next(cent1)){
		  res.add(cent1.element(), s.multiplicity(cent1.element())); 
	  }

	  //Devolvemos el multiconjunto resultante de la suma
	  return res;
  }

  @Override
  public MultiSet<E> minus(MultiSet<E> s) {
	  MultiSet<E> res = new MultiSetList<>();

	  //Si cualquiera de los dos multiconjuntos está vacío, devolvemos un conjunto vacío
	  if(s.isEmpty() || this.isEmpty()){
		  return res;
	  }

	  PositionList<E> elemsThis = this.elements();
	  Position<E> cent1;

	  //Recorremos todos los elementos del multiconjunto actual
	  for(cent1 = elemsThis.first(); cent1 != null; cent1 = elemsThis.next(cent1)){
		  //Calculamos el mínimo entre la diferencia en las multiplicidades y 0 y añadimos al conjunto resultado
		  int multiplicidadMayor = (0 < this.multiplicity(cent1.element()) - s.multiplicity(cent1.element())) ? 
				  this.multiplicity(cent1.element()) - s.multiplicity(cent1.element()) : 0;
		  res.add(cent1.element(), multiplicidadMayor);
	  }

	  //Devolvemos el multiconjunto resultante de la resta
	  return res;
  }

  @Override
  public MultiSet<E> intersection(MultiSet<E> s) {
	  MultiSet<E> res = new MultiSetList<>();

	  //Si cualquiera de los dos multiconjuntos está vacío, devolvemos un conjunto vacío
	  if(s.isEmpty() || this.isEmpty()){
		  return res;
	  }

	  PositionList<E> elemsL = s.elements();
	  PositionList<E> elemsP = this.elements();
	  PositionList<E> elemsAux = elemsL;

	  //Aseguramos que elemsL tenga el mayor tamaño para obtimizar la comparación
	  elemsL = (elemsL.size() < elemsP.size()) ? elemsP : elemsL; 
	  elemsP = (elemsL.size() < elemsP.size()) ? elemsAux : elemsP;

	  //Recorremos ambos multiconjuntos para encontrar la intersección
	  for(Position<E> centL = elemsL.first(); centL != null; centL = elemsL.next(centL)){
		  int tam = res.size();
		  for(Position<E> centP = elemsP.first(); centP != null && tam == res.size(); centP = elemsP.next(centP)){
			  //Si el elemento existe en ambos conjuntos, añadimos la menor multiplicidad
			  if(centL.element().equals(centP.element())){
				  res.add(centL.element(), (s.multiplicity(centL.element()) < this.multiplicity(centP.element())) ? 
						  s.multiplicity(centL.element()) : this.multiplicity(centP.element()));
			  }
		  }
	  }

	  //Devolvemos el conjunto resultante de la intersección
	  return res;
  }

  @Override
  public boolean subsetEqual(MultiSet<E> s) {
	  //Calculamos la intersección
	  MultiSet<E> intersection = this.intersection(s);
	  PositionList<E> elemsI = intersection.elements();

	  //Si ambos multiconjuntos están vacíos, son iguales
	  if(this.isEmpty() && s.isEmpty()){
		  return true;
	  }

	  Position<E> centI = elemsI.first();

	  //Si la intersección está vacía, pero los conjuntos no lo son, entonces no son iguales
	  if(centI == null){
		  return false;
	  }

	  //Recorremos la intersección y verificamos si la multiplicidad en s es mayor o igual
	  for(centI = elemsI.first(); centI != null && (s.multiplicity(centI.element())>= this.multiplicity(centI.element()))
			  && s.multiplicity(centI.element()) > 0 && this.multiplicity(centI.element()) > 0; centI = elemsI.next(centI));

	  //Comprobamos que los tamaños sean coherentes y que se haya cumplido la condición de las multiplicidades
	  return centI == null && this.elements().size() <= s.elements().size();
  }
}