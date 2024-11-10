package aed.recursion;


import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;
import java.util.Arrays;



public class Utils {

	//Método público para calcular la raíz cuadrada de un número dado
	public static int sqrt(int n) {
		return sqrtAux(n, 1, n);
	}


	//Método auxiliar que realiza una búsqueda binaria para encontrar la raíz cuadrada de n
	private static int sqrtAux (int n, int min, int max) {
		//Caso base: cuando el rango no es válido
		if(min > max) {
			return max;
		}
		//Encontramos el punto medio para dividir el rango 
		int medio = min + (max - min)/2;
		//Calcula el cuadrado de 'medio'
		int sqrtMedio = medio * medio;

		//Si el cuadrado es igual a n, devuelve el valor
		if(sqrtMedio == n) {
			return medio;
		}

		//Si el cuadrado es menor a n y el siguiente cuadrado es mayor a n, devuelve el valor
		if(sqrtMedio < n && (medio + 1) * (medio + 1) > n ) {
			return medio;
		}

		//Llamada recursiva para reducir el rango de búsqueda
		return sqrtMedio < n ? sqrtAux(n, medio + 1, max): sqrtAux(n, min, medio -1);
	}

	//método público para generar todos los números primos menores a n usando el método de la criba de Eratóstenes
	public static Iterable<Integer> primes(int n) {
		boolean[] nums = new boolean[n];
		//Rellenamos el array con true, asumiendo así inicialmente que todos son primos
		Arrays.fill(nums, true);
		PositionList<Integer> primos = new NodePositionList<Integer>();
		int numPrimo = 2;
		//Marcamos los múltiplos de cada número primo
		recorrido(nums, numPrimo);
		//Construimos la lista de números primos
		primos = resultado(nums, 2, primos);

		return primos;
	}


	//Marca los múltiplos de un número primos como no primos en el array números
	private static void coprimo(boolean[] numeros, int primo, int i){
		//Si el múltiplo es menor que el tamaño del array
		if(primo * i <= numeros.length - 1){
			//marcamos el múltiplo como no primo
			numeros[primo*i] = false;
			i++;
			//Lamada recursiva para el siguiente múltiplo
			coprimo(numeros, primo, i);
		}
	}

	//Recorre los números del array, aplicando el método coprimo si el número es primo
	private static void recorrido(boolean[] numeros, int primo){
		//Condición de parada para la recursión
		if(primo != numeros.length){
			//Si el número es primo
			if(numeros[primo]){
				//Marcamos sus múltiplos como no primos
				coprimo(numeros, primo, primo);
				primo++;
				//Llmada recursiva para el siguiente número
				recorrido(numeros, primo);
			} else{ //e.o.c.
				primo++;
				recorrido(numeros, primo);
			}
		}
	}

	//Método que construye la lista de números primos a partir del array de booleanos
	private static PositionList<Integer> resultado(boolean[] numeros, int pos, PositionList<Integer> res){
		//Si aún hay posiciones que evaluar
		if(pos <= numeros.length - 1){
			//Si el número es primo, lo agregamos a la lista de primos
			if(numeros[pos]){
				res.addLast(pos);
			}
			pos++;
			//Lamada recursiva para la siguiente posición
			resultado(numeros, pos, res);
		}
		//Devuelve la lista de primos
		return res;
	}

	//Método que compara si dos listas de pares contienen los mismos elementos(el orden es irrelevante)
	public static <E> boolean equals(PositionList<Pair<E,Integer>> p1, PositionList<Pair<E,Integer>> p2) {
		//Si una de las listas está vacía, ambas deben de estar vacías para ser iguales
		if (p1.isEmpty() || p2.isEmpty()) {
			return p1.isEmpty() && p2.isEmpty();
		}

		//verificación en ambas direcciones
		return equalsAux(p1, p2, p1.first()) && equalsAux(p2, p1, p2.first());
	}

	//Método auxiliar para comparar cada elemento de una lista con los elementos de otra
	private static <E> boolean equalsAux (PositionList<Pair<E, Integer>> lista, PositionList<Pair<E, Integer>> lista2, Position<Pair<E, Integer>> actual){
		
		//Si no quedan elementos por comparar
		if(actual == null) {
			return true;
		}

		//Obtenemos el par actual
		Pair<E, Integer> parActual = actual.element();

		//Si no se encuentra un par correspondiente en lista2, las listas no son iguales
		if (!buscarEnLista(parActual, lista2, lista2.first(), parActual.getRight())) {
			return false;
		}
		
		//Comprobamos el siguiente elemento
		return equalsAux(lista, lista2, lista.next(actual));
	}

	//método auxiliar para buscar un par en una lista y verificar que se encuentra en la cantidad adecuada
	private static <E> boolean buscarEnLista (Pair<E, Integer> par, PositionList<Pair<E, Integer>> lista, Position<Pair<E, Integer>> actual, int cantidadRestante) {
		//Si no quedan elementos, verificamos que no haya cantidad restante pendiente
		if (actual == null) {
			return cantidadRestante == 0;
		}

		//Si el elemento coincide, restamos la cantidad correspondiente
		if(par.getLeft().equals(actual.element().getLeft())) {
			cantidadRestante -= actual.element().getRight();
		}

		//Llamada recursiva para el siguiente elemento
		return buscarEnLista (par, lista, lista.next(actual), cantidadRestante);
	}

}
