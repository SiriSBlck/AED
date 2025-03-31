package aed.urgencias;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import es.upm.aedlib.Entry;
import es.upm.aedlib.EntryImpl;
import es.upm.aedlib.Pair;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.map.Map;
import es.upm.aedlib.priorityqueue.HeapPriorityQueue;


public class UrgenciasAED  implements Urgencias{

	private HeapPriorityQueue<Paciente, Void> listaPacientes;
	private Map<String, Entry<Paciente, Void>> pacientesPorDNI;
	private int sumaTiemposEspera;
	private int numeroPacientesAtendidos;

	public UrgenciasAED() {
		listaPacientes = new HeapPriorityQueue<>();
		this.pacientesPorDNI = new HashTableMap<>();
		this.sumaTiemposEspera = 0;
		this.numeroPacientesAtendidos = 0;
	}

	@Override
	public Paciente admitirPaciente(String DNI, int prioridad, int hora) throws PacienteExisteException {
		if(this.pacientesPorDNI.containsKey(DNI)){
			throw new PacienteExisteException();
		} 
		Paciente newPatient = new Paciente(DNI, prioridad, hora, hora);

		this.pacientesPorDNI.put(DNI, this.listaPacientes.enqueue(newPatient, null));
		return newPatient;
	}

	@Override
	public Paciente salirPaciente(String DNI, int hora) throws PacienteNoExisteException {
		if(!this.pacientesPorDNI.containsKey(DNI)){
			throw new PacienteNoExisteException();
		} 
		Entry<Paciente, Void> entry = getPacienteAux(DNI);

		Paciente newPatient = entry.getKey();
		this.listaPacientes.remove(entry);
		this.pacientesPorDNI.remove(DNI);
		return newPatient;
	}
	
	public Entry<Paciente, Void> getPacienteAux(String DNI) throws PacienteNoExisteException{
		Entry<Paciente, Void> entry = this.pacientesPorDNI.get(DNI);
		if(entry == null){
			throw new PacienteNoExisteException();
		}
		return entry;
	}
	
	@Override
	public Paciente cambiarPrioridad(String DNI, int nuevaPrioridad, int hora) throws PacienteNoExisteException {
		if(!this.pacientesPorDNI.containsKey(DNI)){
			throw new PacienteNoExisteException();
		}
		Entry<Paciente, Void> entry = this.pacientesPorDNI.get(DNI);
		Paciente newPatient = entry.getKey();
		if(newPatient.getPrioridad() != nuevaPrioridad){
			newPatient.setPrioridad(nuevaPrioridad);
			newPatient.setTiempoAdmisionEnPrioridad(hora);
			this.listaPacientes.replaceKey(entry, newPatient);
		}
		return newPatient;
	}

	@Override
	public Paciente atenderPaciente(int hora) {
		if(this.listaPacientes.isEmpty()){
			return null;
		}

		Entry<Paciente, Void> entry = this.listaPacientes.first();
		Paciente newPatient = entry.getKey();
		this.listaPacientes.remove(entry);
		this.sumaTiemposEspera += hora - newPatient.getTiempoAdmision();
		this.numeroPacientesAtendidos++;
		this.pacientesPorDNI.remove(entry.getKey().getDNI());
		return newPatient;
	}

	@Override
	public void aumentaPrioridad(int maxTiempoEspera, int hora) {
		for(Entry<Paciente, Void> entry : this.listaPacientes) {
			Paciente paciente = entry.getKey();
			int tiempoEspera = hora - paciente.getTiempoAdmisionEnPrioridad();

			if(tiempoEspera > maxTiempoEspera && paciente.getPrioridad() != 0) {
				paciente.setPrioridad(paciente.getPrioridad()- 1);
				paciente.setTiempoAdmisionEnPrioridad(hora);
				this.listaPacientes.replaceKey(entry, paciente);
			}
		}
	}

	@Override
	public Iterable<Paciente> pacientesEsperando(){
		HeapPriorityQueue<Paciente, Void> copiaListaPacientes = new HeapPriorityQueue<>();

		for(Entry<Paciente, Void> entry : this.listaPacientes) {
			copiaListaPacientes.enqueue(entry.getKey(), null);
		}

		List<Paciente>  pacientes = new LinkedList<>();
		while(!copiaListaPacientes.isEmpty()) {
			Paciente newPatient = copiaListaPacientes.dequeue().getKey();
			pacientes.add(newPatient);
		}

		return pacientes;
	}

	public Paciente getPaciente(String DNI){
		Entry<Paciente, Void> entry = this.pacientesPorDNI.get(DNI);
		return (entry != null)? entry.getKey() : null;
	}

	public Pair<Integer,Integer> informacionEspera(){
		// if(numeroPacientesAtendidos == 0) {
		// 	return new Pair<>(0,0);
		// }
		return new Pair<>(sumaTiemposEspera, numeroPacientesAtendidos);
	}
}
