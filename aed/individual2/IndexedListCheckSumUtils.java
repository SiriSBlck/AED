package aed.individual2;

import es.upm.aedlib.indexedlist.*;

public class IndexedListCheckSumUtils {

  // a no es null, podria tener tamaño 0, n>0
  public static IndexedList<Integer> indexedListCheckSum(IndexedList<Integer> list, int n) {
    IndexedList<Integer> res = new ArrayIndexedList<>();

		if(list != null){
			int nChecks = (list.size() % n == 0) ? list.size() / n : (list.size() / n) + 1;
			int suma = 0;
			int j = 0;

			for (int i = 0; i < list.size(); i++, j++) {
				int elem = list.get(i);
				suma += elem;
				res.add(j, elem);

				if(((i+1) % n == 0 && nChecks > 1) || (i == list.size() - 1 && nChecks == 1)){
					j++;
					res.add(j, suma);
					nChecks--;
					suma = 0;
				} 

			} //for
		} //else if
		
		return res;
  }

  // list no es null, podria tener tamaño 0, n>0
  public static boolean checkIndexedListCheckSum(IndexedList<Integer> list, int n) {
    boolean res = true;
   
		if(list != null){
			int suma = 0;
			int cont = n;

			for (int i = 0; i < list.size() && res; i++, cont--) {
				if(cont == 0 || i == list.size() - 1){
					res = list.get(i) == suma;
					suma = 0;
					cont = n + 1;
				} else{
					suma += list.get(i);
				}
			} //for
		} //if
		
    return res;
  }
}

