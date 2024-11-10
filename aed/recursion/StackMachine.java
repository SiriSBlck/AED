package aed.recursion;

import es.upm.aedlib.map.*;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;
import es.upm.aedlib.lifo.*;
import aed.recursion.Instruction.InstType;

public class StackMachine {
	Map<String,PositionList<Instruction>> code;
	LIFO<Integer> stack;

	public StackMachine(Map<String,PositionList<Instruction>> code) {
		this.stack = new LIFOArray<>();
		this.code = code;
	}

	//Método principal que ejecuta el conjunto de instrucciones asociado a name
	public void run(String name) {
		//Verifica que el mapa no esté vacío
		if(!this.code.isEmpty()){
			//Obtiene las instrucciones para name
			PositionList<Instruction> instrucciones = this.code.get(name);
			//Empieza desde la primera instrucción
			Position<Instruction> pos = instrucciones.first();
			//LLama al método resursivo para ejecutar cada instrucción
			this.recorrer(instrucciones, pos);
		} //if !isEmpty
	}

	//Método recursivo que ejecuta cada instrucción según el tipo
	public void recorrer(PositionList<Instruction> instrucciones, Position<Instruction> posicion){
		//Si hay instrucciones por ejecutar
		if(posicion != null){
			//Obtiene la instrucción actual
			Instruction inst = posicion.element(); 
			switch (inst.getInstType()) {
			//Suma los dos elementos de la pila
			case ADD:
				if(this.stack.size() >= 2){
					Integer intA1 = this.stack.pop();
					Integer intA2 = this.stack.pop();
					//El resultado de la suma se inserta en la pila
					this.stack.push(intA1 + intA2);
				}
				this.recorrer(instrucciones, instrucciones.next(posicion));
				break;
			//Intercambia los dos últimos elementos de la pila
			case SWAP:
				if(this.stack.size() >= 2){
					Integer intW1 = this.stack.pop();
					Integer intW2 = this.stack.pop();
					this.stack.push(intW1);
					this.stack.push(intW2); //Intercambia ambos Integers invirtiendo la inserción
				}
				this.recorrer(instrucciones, instrucciones.next(posicion));
				break;
			//Resta los dos últimos elementos de la pila
			case SUB:
				if(this.stack.size() >= 2){
					Integer intS1 = this.stack.pop();
					Integer intS2 = this.stack.pop();
					//Inserta el resultado en la pila
					this.stack.push(intS1 - intS2);
				}
				this.recorrer(instrucciones, instrucciones.next(posicion));
				break;
			//Duplica el último elemento de la pila
			case DUP:
				if(this.stack.size() >= 1){
					//Inserta una copia del último elemento
					this.stack.push(this.stack.top());
				}
				this.recorrer(instrucciones, instrucciones.next(posicion));
				break;
			//Elimina el último elemento de la pila
			case DROP:
				if(this.stack.size() >= 1){
					//Elimina el elemento superior(lo extrae sin almacenarlo)
					this.stack.pop();
					this.recorrer(instrucciones, instrucciones.next(posicion));
				}
				break;
			//Empuja un valor en la pila
			case PUSH:
				//Obtiene el parámetro de la instrucción y lo inserta
				this.stack.push(inst.getIntParm());
				this.recorrer(instrucciones, instrucciones.next(posicion));
				break;
			//Multiplica los dos últimos elementos de la pila
			case MULT:
				if(this.stack.size() >= 2){
					Integer intM1 = this.stack.pop();
					Integer intM2 = this.stack.pop();
					//Inserta el producto en la pila
					this.stack.push(intM1 * intM2);
				}
				this.recorrer(instrucciones, instrucciones.next(posicion));
				break;
			//Imprime el último elemento de la pila
			case PRINT:
				if(this.stack.size() >= 1){
					//Saca y muestra el valor
					System.out.println(this.stack.pop());  
				}
				this.recorrer(instrucciones, instrucciones.next(posicion));
				break;
				//Compara los dos últimos elementos y empuja1 si son iguales o 0 en caso contrario
			case EQ:
				if(this.stack.size() >= 2){
					Integer intEQ1 = this.stack.pop();
					Integer intEQ2 = this.stack.pop();
					//1 para la igualdad, 0 si no se cumple
					this.stack.push((intEQ1 == intEQ2) ? 1 : 0);
				}
				this.recorrer(instrucciones, instrucciones.next(posicion));
				break;
			//Llama a otro conjunto de instrucciones
			case CALL:
				//Obtiene el nombre del conjunto de instrucciones
				String name = inst.getNameParm();
				//Ejecuta las instrucciones
				this.run(name);
				this.recorrer(instrucciones, instrucciones.next(posicion));
				break;
			//Devolver sin hacer nada (finaliza la llamada actual)
			case RET:
				break;
			//Salta un número de instrucciones si el último valor de la pila es distinto de 0
			case IF_SKIP:
				if(this.stack.pop() != 0){
					//Avanza el cursor según el parámetro
					this.avanzarCursor(instrucciones, posicion, posicion.element().getIntParm());
				} else{
					//Continua si es 0
					this.recorrer(instrucciones, instrucciones.next(posicion));
				}
				break;
			default:
				throw new RuntimeException("No existe una instrucción con el nombre " + inst);
			} //Switch
		}
	}

	//Método que avanza la posición de la instrucción en la lista
	public void avanzarCursor(PositionList<Instruction> instrucciones, Position<Instruction> pos, int cont){
		//Si hay instrucciones restantes para avanzar
		if(cont > 0 && pos != null){
			//Avanza el número especificado de instrucciones
			this.avanzarCursor(instrucciones, instrucciones.next(pos), cont - 1);
		} else {
			//Continua la ejecución desde la poscición actual
			this.recorrer(instrucciones, pos);
		}
	}
}



