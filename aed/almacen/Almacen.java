package aed.almacen;

import es.upm.aedlib.indexedlist.IndexedList;
import es.upm.aedlib.indexedlist.ArrayIndexedList;


/**
 * Implementa la logica del almacen.
 */
public class Almacen implements ClienteAPI, AlmacenAPI, ProductorAPI {

	// Compras (sin ningun orden especial)
	private ArrayIndexedList<Compra> compras;
	// Productos ordenados ascendamente usando el productoId de un Product.
	private ArrayIndexedList<Producto> productos;

	// No es necesario cambiar el constructor
	/**
	 * Crea un almacen.
	 */
	public Almacen() {
		this.compras = new ArrayIndexedList<>();
		this.productos = new ArrayIndexedList<>();
	}

	public void reabastecerProducto(String productoId, int cantidad) {
		//Realiza una busqueda binaria para encontrar la posicion del producto con el productoId dado
		int res = busquedaBinariaEnProductos(productoId);
		if(res == -1){
			//Si la lista está vacía, lo añade en la posición 0 de la lista de productos
			this.productos.add(this.productos.size(), new Producto(productoId, cantidad));
		} else {
			//Si la lista contiene al menos un elemento, se evalúa la posición en la que insertar/modificar el producto dado
			Producto act = this.productos.get(res);
		
			if(act.getProductoId().equals(productoId)) {
				//Si el productoId del producto obtenido coincide con el dado, se actualizasu cantidad disponible
				act.setCantidadDisponible(act.getCantidadDisponible() + cantidad);
			} else {
				//En caso de que no coincida, se determina la posicion para insertarlo. Asegurando asi que la lista de productos permanezca ordenada
				int pos = (this.productos.get(res).getProductoId().compareTo(productoId) > 0)? res : res + 1;
				this.productos.add(pos, new Producto(productoId, cantidad));
			} 
		}
	}

	@Override
	public Producto getProducto(String productoId) {
		//Busca la posicion del producto con el productoId dado usando busqueda binaria
		int pos = busquedaBinariaEnProductos(productoId);

		//Si la lista de productos está vacía o no hay productos, retorna null
		if(this.productos.isEmpty() || pos == -1){
			return null;
		}

		//En caso contrariio, crea y retorna una nueva instancia del producto(deep copy), evitando modificar el objeto original en la lista
		Producto res = (this.productos.get(pos).getProductoId().equals(productoId))? 
				new Producto(this.productos.get(pos).getProductoId(), this.productos.get(pos).getCantidadDisponible()): null;
		return res;
	}

	@Override
	public Compra getCompra(Integer compraId) {
		Compra res = null;
		//Si la lista de compras no está vacía, recorre la lista buscando una compra con el compraId dado
		if(this.compras.size() != 0){
			for(int i = 0; i < this.compras.size() && res == null; i++) {
				//Si encuentra la compra con el compraId, la asigna a res y sale del bucle
				if(this.compras.get(i).getCompraId().equals(compraId)){
					res = this.compras.get(i);
				}
			}
		}
		//Retorna la compra encontrada o null si no se encontró
		return res;
	}

	@Override
	public IndexedList<Producto> getProductos() {
		//Crea una nueva lista para almacenar los productos
		IndexedList<Producto> res = new ArrayIndexedList<>();

		//Si la lista de productos no está vacía, recorre la lista y añade copias(deep copy) de cada producto a la nueva lista
		if(this.productos.size() != 0){
			for(int i = 0; i < this.productos.size(); i++) {
				Producto act = this.productos.get(i);
				res.add(i, new Producto(act.getProductoId(), act.getCantidadDisponible()));
			}
		}

		//Devuelve la lista con los productos
		return res;
	}

	@Override
	public IndexedList<Compra> getCompras() {
		//Crea una nueva lista para almacenar las compras
		IndexedList<Compra> res = new ArrayIndexedList<>();

		//Si la lista de compras no está vacía, recorre la lista y añade copias(shallow copy, debido al aumento no deseado del constructor de compras en caso de añadir una deep copy) 
		//de cada compra a la nueva lista
		if(this.compras.size() != 0){
			for(int i = 0; i < this.compras.size(); i++) {
				Compra act = this.compras.get(i);
				res.add(i, act);
			}
		}

		//Devuelve la lista con las compras
		return res;
	}


	////////////////REVISAR
	@Override
	public IndexedList<Compra> comprasCliente(String clienteId) {
		//Inicializa la lista para almacenar las compras del cliente
		IndexedList<Compra> res = new ArrayIndexedList<>();

		//Si la lista de compras no está vacía, inicializa la lista y recorre las compras buscando coincidencias con clienteId 
		if(this.compras.size() != 0){
			int j = 0;
		
			//Recorre todas las compras y si el clienteId coincide, añade la compra a la lista
			for(int i = 0; i < this.compras.size(); i++) {
				if((this.compras.get(i).getClienteId()).equals(clienteId)) {
					res.add(j, this.compras.get(i));
					j++;
				} //if
			} //for
		}
		
		//Devuelve la lista de compras del cliente, vacía si no hay productos en el atributo de compras
		return res;
	}

	@Override
	public IndexedList<Compra> comprasProducto(String productoId) {
		//Inicializa la lista para almacenar las compras del producto
		IndexedList<Compra> res = new ArrayIndexedList<>();

		//Si la lista de compras no está vacía, inicializa la lista y recorre las compras buscando coincidencias con productoId 
		if(this.compras.size() != 0){
			int j = 0;
		
			//Recorre todas las compras y si el productoId coincide, añade la compra a la lista
			for(int i = 0; i < this.compras.size(); i++) {
				if(this.compras.get(i).getProductoId().equals(productoId)) {
					res.add(j, this.compras.get(i));
					j++;
				} // if
			} //for
		}
		
		//Devuelve la lista de compras del producto, vacía si no hay productos en el atributo de compras
		return res;
	}

	@Override
	public Integer pedir(String clienteId, String productoId, int cantidad) {
		//Busca la posición del producto usando busqueda binaria
		int pos = busquedaBinariaEnProductos(productoId);
		Integer res = null;

		//Si la lista contiene al menos un producto, el producto está en la lista y tiene suficiente cantidad, se procesa el pedido
		if(pos != -1 && this.productos.get(pos).getProductoId().equals(productoId) &&
				this.productos.get(pos).getCantidadDisponible() >= cantidad) {

			//Crea una nueva compra y la añade a la lista de compras
			Compra compra = new Compra(clienteId, productoId, cantidad);
			this.compras.add(this.compras.size(), compra);
			//Resta la cantidad pedida de la cantidad disponible del producto
			this.productos.get(pos).setCantidadDisponible(this.productos.get(pos).getCantidadDisponible() - cantidad);
			//Retorna el ID de la compra
			res = compra.getCompraId();
		}
		
		//Si no se puede procesar el pedido, retorna null
		return res;
	}


	///////////////////////////////// MÉTODOS AUXILIARES

	public int busquedaBinariaEnProductos(String id) {
		//Inicializa la variable centro en -1, que se devolverá encaso de que la lista esté vacía
		int centro = -1;
		//Si la lista de productos no está vacía, comienza con la busqueda binaria
		if(this.productos.size() != 0){
			int enc = -1;
			int sup = this.productos.size()-1;
			int inf = 0;
			//Realiza el bucle de busqueda binaria
			while(sup >= inf) {
				centro = (sup + inf)/2;
				//Compara el producto en el centro con el id buscado
				enc = this.productos.get(centro).getProductoId().compareTo(id);
				if(enc == 0){
					//Si encuentra el producto, devuelve la posición
					return centro;
				}
				else if(enc > 0) {
					//Si el producto del centro tiene un ID mayor al buscado, ajusta el limite superior
					sup = centro - 1;
				} else{
					//Si el producto del centro tiene un ID menor al buscado, ajusta el limite inferior
					inf = centro + 1;
				} 
			}
		}
		
		//Devuleve la posición centro final, en caso de no encontrarla será la posicion donde podría insertarse
		return centro;
	}
	}
