package aed.delivery;

import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.graph.DirectedGraph;
import es.upm.aedlib.graph.DirectedAdjacencyListGraph;
import es.upm.aedlib.graph.Vertex;
import es.upm.aedlib.graph.Edge;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.set.HashTableMapSet;
import es.upm.aedlib.set.Set;
import java.util.Iterator;

public class Delivery<V> {

	private DirectedGraph<V, Integer> graph;

	// Construct a graph out of a series of vertices and an adjacency matrix.
	// There are 'len' vertices. A null means no connection. A non-negative
	// number represents distance between nodes.
	public Delivery(V[] places, Integer[][] gmat) {
		if(places != null){
			this.graph = new DirectedAdjacencyListGraph<>();
			HashTableMap<V, Vertex<V>> map = new HashTableMap<>(); //Creamos un mapa para guardar los vértices del grafo
	  
			for(int i = 0; i < places.length; i++){ //Por cada vértice
			  if(!map.containsKey(places[i])) map.put(places[i], this.graph.insertVertex(places[i])); //si no existe en nuestro grafo, lo añadimos
			  for(int j = 0; j < places.length; j++){ //revisamos sus vértices adyacentes
				Integer weight = gmat[i][j]; // y el peso de los mismos, si existen
				if(weight != null && weight > 0){ 
				  //si no existe el vértice ady. en nuestro grafo, lo añadimos
				  if(!map.containsKey(places[j])) map.put(places[j], this.graph.insertVertex(places[j])); 
				  this.graph.insertDirectedEdge(map.get(places[i]), map.get(places[j]), weight); //y añadimos la arista que los conecta
				}
			  }
			}
		  }
	}

	// Just return the graph that was constructed
	public DirectedGraph<V, Integer> getGraph() {
		return this.graph;
	}

	// Return a Hamiltonian path for the stored graph, or null if there is none.
	// The list containts a series of vertices, with no repetitions (even if the path
	// can be expanded to a cycle).
	public PositionList <Vertex<V>> tour() {
		//Listas para almacenar el camino y los vértices visitados
		PositionList<Vertex<V>> camino = new NodePositionList<>();
		PositionList<Vertex<V>> visitado = new NodePositionList<>();

		//Intenta encontrar un camino hamiltoniano comenzando desde cada vértice
		for(Vertex<V> principio : this.graph.vertices()) {
			//Lamada recursiva a un método auxiliar para intentar encontrar un camino hamiltoniano desde el vértice actual (principio)
			if(encontrarCaminoHamiltoniano(principio, camino, visitado, count(this.graph.vertices()))) {
				//Devuelve el camino si se encuentra
				return camino;
			}
		}
		//Si no hay camino, devuelve null
		return null;
	}

	//Método auxiliar recursivo que busca un camino hamiltoniano
	private boolean encontrarCaminoHamiltoniano (Vertex<V> actual, PositionList<Vertex<V>> camino, PositionList<Vertex<V>> visitado, int totalVertices) {
		//Añade el vértice actual al camino 
		camino.addLast(actual);
		//Marca el vértice como visitado
		visitado.addLast(actual);

		//Si se han visitado todos los vértices, se ha encontrado un camino hamiltoniano
		if(visitado.size() == totalVertices) {
			return true;
		}

		//Explora las aristas salientes del vértice actual
		for(Edge<Integer> edge : this.graph.outgoingEdges(actual)) {
			//Obtiene el vértice destino
			Vertex<V> siguiente = this.graph.endVertex(edge);
			//Si el vértice destino no ha sido visitado entonces hay que realizar la llamada recursiva
			if(!esVisitado(siguiente, visitado) && encontrarCaminoHamiltoniano(siguiente, camino, visitado, totalVertices)) {
				//Camino encontrado
				return true;
			}
		}

		//Retrocede si no se encontró un camino válido desde el vértice actual
		camino.remove(camino.last());
		visitado.remove(visitado.last());
		return false;
	}

	//Método auxiliar que se encarga de contar el número de vértices en un colección iterable
	private int count(Iterable<Vertex<V>> iterable) {
		int contador = 0;

		for(Vertex<V> v: iterable) {
			contador++;
		}
		return contador;
	}

	//Verifica si un vértice ya ha sido visitado
	private boolean esVisitado (Vertex<V> vertice, PositionList<Vertex<V>> visitado) {
		for(Vertex<V> v : visitado) {
			if(v.equals(vertice)) {
				return true;
			}
		}
		return false;
	}

	//Calcula la longitud total de un camino dado, es decir, se encarga de sumar los pesos de las aristas.
	public int length(PositionList<Vertex<V>> path) {
		//Si el camino es nulo o vacío entonces tiene longitud 0
		if(path == null || path.isEmpty()) {
			return 0;
		}

		Integer longitudTotal = 0;
		Vertex<V> anterior = null;

		//Recorre el camino sumando los pesos de las aristas
		for(Vertex<V> actual: path) {
			if(anterior != null) {
				//Obtiene el peso de la arista entre los vértices
				Integer peso = encontrarPeso (anterior, actual);
				if(peso != null) {
					longitudTotal += peso;
				}
			}
			//Actualiza el vértice anterior
			anterior = actual;
		}
		//Devuelve la longitud total del camino
		return longitudTotal;
	}

	//Método auxiliar que se encarga de encontrar el peso de la arista entre dos vértices.
	private Integer encontrarPeso (Vertex<V> desde, Vertex<V> hasta) {
		for(Edge<Integer> edge : this.graph.outgoingEdges(desde)) {
			//Busca la arista que conecta los dos vértices
			if(this.graph.endVertex(edge).equals(hasta)) {
				//Devuelve el peso de la arista
				return edge.element();
			}
		}
		//Devuelve null si no hay arista entre los vértices
		return null;
	}

	//Devuelve una representación textual de la clase
	public String toString() {
		return "Delivery";
	}
}
