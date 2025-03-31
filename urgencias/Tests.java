package aed.urgencias;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
public class Tests {

  @Test
  public void testAdmitir() throws PacienteExisteException
  {
    Urgencias u = new UrgenciasAED();
    u.admitirPaciente("111", 5, 1);
    Paciente p = u.atenderPaciente(10);

    // Check expected DNI ("111") == observed DNI (p.getDNI())
    assertEquals("111", p.getDNI());
  }
  
  @Test
  void testAtenderPacienteOrdenMismaPrioridad() {
	  Urgencias urgencia = new UrgenciasAED();
	  
	  Paciente p1 = new Paciente("P1", 3);
	  Paciente p2 = new Paciente("P2", 3);
	  
	  urgencia.admitirPaciente(p1);
	  urgencia.admitirPaciente(p2);
	  
	  asertEquals(p1, urgencia.atenderPaciente(), "Debería atender primero al paciente P1");
  }
  
  @Test
  void testAtenderPacientesConsecutivosMismaPrioridad() {
	Urgencias urgencia = new UrgenciasAED();
	  
	  Paciente p1 = new Paciente("P1", 3);
	  Paciente p2 = new Paciente("P2", 3);
	  
	  urgencia.admitirPaciente(p1);
	  urgencia.admitirPaciente(p2);
	  
	  asertEquals(p1, urgencia.atenderPaciente(), "Debería atender primero al paciente P1");
	  asertEquals(p2, urgencia.atenderPaciente(), "Debería atender después al paciente P2");
  }
  
  @Test
  void testAtenderPacientesDiferentePrioridad() {
	Urgencias urgencia = new UrgenciasAED();
	  
	  Paciente p1 = new Paciente("P1", 5);
	  Paciente p2 = new Paciente("P2", 1);
	  
	  urgencia.admitirPaciente(p1);
	  urgencia.admitirPaciente(p2);
	  
	  asertEquals(p2, urgencia.atenderPaciente(), "Debería atender primero al paciente P2 con prioridad más alta");
  }
  
  @Test
  void testSalirPaciente() {
	Urgencias urgencia = new UrgenciasAED();
	  
	  Paciente p1 = new Paciente("P1", "12345678B", 3);
	  Paciente p2 = new Paciente("P2", "48789074W", 3);
	  
	  urgencia.admitirPaciente(p1);
	  urgencia.admitirPaciente(p2);
	  
	  urgencia.salirPaciente("12345678B");
	  
	  asertEquals(p2, urgencia.atenderPaciente(), "Debería atender al paciente P2 tras salir P1");
  }
  
  @Test
  void testCambiarPrioridad() {
	Urgencias urgencia = new UrgenciasAED();
	  
	  Paciente p1 = new Paciente("P1", "12345678B", 3);
	  Paciente p2 = new Paciente("P2", "48789074W", 3);
	  
	  urgencia.admitirPaciente(p1);
	  urgencia.admitirPaciente(p2);
	  
	  urgencia.cambiarPrioridad("48789074W", 5);
	  
	  asertEquals(p2, urgencia.atenderPaciente(), "Debería atender al paciente P1 con la nueva prioridad más alta");
  }
}

