package aed.individual1;

public class ArrayCheckSumUtils {

	// a no es null, podria tener tamaño 0, n>0
	public static int[] arrayCheckSum(int[] arr, int n) {
		int[] res = null;

		if(arr.length == 0){
			res = new int[0];
		} else if(arr != null){ //COMPROBAR LA PRE
			int nChecks = (arr.length % n == 0) ? arr.length / n : (arr.length / n) + 1;
			res = new int[arr.length + nChecks];
			int suma = 0;
			int j = 0;

			for (int i = 0; i < arr.length; i++, j++) {
				int elem = arr[i];
				suma += elem;
				res[j] = elem;

				if(((i+1) % n == 0 && nChecks > 1) || (i == arr.length - 1)){
					j++;
					res[j] = suma;
					nChecks--;
					suma = 0;
				} 

			} //for
		
		} //else if
		
		return res;
	} // arrayCheckSum
} // ArrayCheckSumUtils
