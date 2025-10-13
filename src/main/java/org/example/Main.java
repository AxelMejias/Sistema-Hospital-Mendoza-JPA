package org.example;

import jakarta.persistence.*;
import org.example.entidades.*;
import org.example.servicio.CitaManager;
import org.example.servicio.CitaException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static void main(String[] args) {
        imprimirEncabezado();
        
        try {
            inicializarJPA();
            ejecutarDemo();
            imprimirPie();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }
    }

    private static void inicializarJPA() {
        System.out.println("Inicializando JPA...");
        emf = Persistence.createEntityManagerFactory("hospital-persistence-unit");
        em = emf.createEntityManager();
        System.out.println("JPA inicializado");
    }

    private static void ejecutarDemo() {
        Hospital hospital = crearHospital();
        List<Medico> medicos = crearMedicos(hospital);
        List<Paciente> pacientes = crearPacientes(hospital);
        persistirDatos(hospital, medicos, pacientes);
        crearCitas(pacientes, medicos, hospital);
        ejecutarConsultas();
    }

    private static Hospital crearHospital() {
        Hospital hospital = Hospital.builder()
            .nombre("Hospital Gral Mendoza")
            .direccion("Av. San Martin 1234, Mendoza")
            .telefono("0261-4567-8900")
            .build();

        Departamento cardiologia = Departamento.builder()
            .nombre("Cardiologia")
            .especialidad(EspecialidadMedica.CARDIOLOGIA)
            .build();

        Departamento pediatria = Departamento.builder()
            .nombre("Pediatria")
            .especialidad(EspecialidadMedica.PEDIATRIA)
            .build();

        Departamento traumatologia = Departamento.builder()
            .nombre("Traumatologia")
            .especialidad(EspecialidadMedica.TRAUMATOLOGIA)
            .build();

        hospital.agregarDepartamento(cardiologia);
        hospital.agregarDepartamento(pediatria);
        hospital.agregarDepartamento(traumatologia);

        cardiologia.crearSala("C-101", "Consultorio Cardiologia");
        pediatria.crearSala("P-201", "Consultorio Pediatria");
        traumatologia.crearSala("T-301", "Consultorio Traumatologia");

        return hospital;
    }

    private static List<Medico> crearMedicos(Hospital hospital) {
        Medico cardiologo = Medico.builder()
            .nombre("Luciano")
            .apellido("Mas")
            .dni("43789456")
            .fechaNacimiento(LocalDate.of(2001, 3, 15))
            .numeroMatricula("MP-5896")
            .especialidad(EspecialidadMedica.CARDIOLOGIA)
            .build();

        Medico pediatra = Medico.builder()
            .nombre("Pipo")
            .apellido("Vernier")
            .dni("44123789")
            .fechaNacimiento(LocalDate.of(2001, 7, 22))
            .numeroMatricula("MP-3698")
            .especialidad(EspecialidadMedica.PEDIATRIA)
            .build();

        Medico traumatologo = Medico.builder()
            .nombre("Leandro")
            .apellido("Nunez")
            .dni("42852963")
            .fechaNacimiento(LocalDate.of(2001, 9, 10))
            .numeroMatricula("MP-7412")
            .especialidad(EspecialidadMedica.TRAUMATOLOGIA)
            .build();

        hospital.getDepartamentos().get(0).agregarMedico(cardiologo);
        hospital.getDepartamentos().get(1).agregarMedico(pediatra);
        hospital.getDepartamentos().get(2).agregarMedico(traumatologo);

        return List.of(cardiologo, pediatra, traumatologo);
    }

    private static List<Paciente> crearPacientes(Hospital hospital) {
        Paciente paciente1 = Paciente.builder()
            .nombre("Axel")
            .apellido("Mejias")
            .dni("42736970")
            .fechaNacimiento(LocalDate.of(2001, 12, 18))
            .telefono("011-1234-5678")
            .tipoSangre(TipoSangre.O_POSITIVO)
            .build();

        Paciente paciente2 = Paciente.builder()
            .nombre("Nicolas")
            .apellido("Hassan")
            .dni("43568453")
            .fechaNacimiento(LocalDate.of(2002, 5, 23))
            .telefono("011-9876-5432")
            .tipoSangre(TipoSangre.A_POSITIVO)
            .build();

        hospital.agregarPaciente(paciente1);
        hospital.agregarPaciente(paciente2);

        paciente1.getHistoriaClinica().agregarDiagnostico("Hipertension");
        paciente1.getHistoriaClinica().agregarAlergia("Penicilina");
        paciente2.getHistoriaClinica().agregarDiagnostico("Control rutinario");

        return List.of(paciente1, paciente2);
    }

    private static void persistirDatos(Hospital hospital, List<Medico> medicos, List<Paciente> pacientes) {
        System.out.println("Persistiendo datos...");
        try {
            em.getTransaction().begin();
            em.persist(hospital);
            for (Medico medico : medicos) {
                em.persist(medico);
            }
            for (Paciente paciente : pacientes) {
                em.persist(paciente);
            }
            em.getTransaction().commit();
            System.out.println("Datos persistidos exitosamente");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al persistir: " + e.getMessage(), e);
        }
    }

    private static void crearCitas(List<Paciente> pacientes, List<Medico> medicos, Hospital hospital) {
        System.out.println("Programando citas...");
        CitaManager citaManager = new CitaManager();
        
        try {
            citaManager.programarCita(
                pacientes.get(0),
                medicos.get(0),
                hospital.getDepartamentos().get(0).getSalas().get(0),
                LocalDateTime.now().plusDays(1),
                new BigDecimal("150.00")
            );
            
            citaManager.programarCita(
                pacientes.get(1),
                medicos.get(1),
                hospital.getDepartamentos().get(1).getSalas().get(0),
                LocalDateTime.now().plusDays(2),
                new BigDecimal("120.00")
            );
            
            // Cita con traumatólogo
            citaManager.programarCita(
                pacientes.get(0), // Axel con el traumatólogo
                medicos.get(2),   // Dr. Sofia Rodriguez
                hospital.getDepartamentos().get(2).getSalas().get(0),
                LocalDateTime.now().plusDays(3),
                new BigDecimal("180.00")
            );
            
            System.out.println("Citas programadas: " + citaManager.getTodasLasCitas().size());
        } catch (CitaException e) {
            System.err.println("Error programando cita: " + e.getMessage());
        }
    }

    private static void ejecutarConsultas() {
        TypedQuery<Hospital> queryHospitales = em.createQuery("SELECT h FROM Hospital h", Hospital.class);
        List<Hospital> hospitales = queryHospitales.getResultList();
        
        for (Hospital hospital : hospitales) {
            mostrarTablaHospital(hospital);
        }
        
        mostrarEstadisticasGenerales();
    }

    private static void cerrarRecursos() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
    
    private static void imprimirEncabezado() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                    SISTEMA DE GESTION HOSPITALARIA");
        System.out.println("                         Hospital Gral Mendoza");
        System.out.println("=".repeat(80));
        System.out.println("Inicializando sistema...\n");
    }
    
    private static void imprimirPie() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                   SISTEMA EJECUTADO EXITOSAMENTE");
        System.out.println("=".repeat(80));
    }
    
    private static void mostrarTablaHospital(Hospital hospital) {
        System.out.println("\n" + "+" + "-".repeat(78) + "+");
        System.out.printf("| %-76s |%n", "[HOSPITAL] " + hospital.getNombre().toUpperCase());
        System.out.printf("| %-76s |%n", "[DIRECCION] " + hospital.getDireccion());
        System.out.printf("| %-76s |%n", "[TELEFONO] " + hospital.getTelefono());
        System.out.println("+" + "-".repeat(78) + "+");
        
        // Tabla de Departamentos y Medicos
        System.out.printf("| %-25s | %-25s | %-25s |%n", "DEPARTAMENTO", "MEDICO", "ESPECIALIDAD");
        System.out.println("+" + "-".repeat(25) + "+" + "-".repeat(25) + "+" + "-".repeat(25) + "+");
        
        for (Departamento dept : hospital.getDepartamentos()) {
            if (dept.getMedicos().isEmpty()) {
                System.out.printf("| %-25s | %-25s | %-25s |%n", 
                    truncar(dept.getNombre(), 25), "Sin medicos", "");
            } else {
                boolean first = true;
                for (Medico medico : dept.getMedicos()) {
                    String deptName = first ? truncar(dept.getNombre(), 25) : "";
                    System.out.printf("| %-25s | %-25s | %-25s |%n", 
                        deptName, 
                        truncar("Dr. " + medico.getNombreCompleto(), 25),
                        truncar(medico.getEspecialidad().getDescripcion(), 25));
                    first = false;
                }
            }
        }
        
        System.out.println("+" + "-".repeat(78) + "+");
        
        // Tabla de Pacientes
        System.out.printf("| %-30s | %-15s | %-15s | %-15s |%n", 
            "PACIENTES REGISTRADOS", "DNI", "TIPO SANGRE", "EDAD");
        System.out.println("+" + "-".repeat(30) + "+" + "-".repeat(15) + "+" + "-".repeat(15) + "+" + "-".repeat(15) + "+");
        
        for (Paciente paciente : hospital.getPacientes()) {
            System.out.printf("| %-30s | %-15s | %-15s | %-15s |%n",
                truncar(paciente.getNombreCompleto(), 30),
                paciente.getDni(),
                paciente.getTipoSangre().getSimbolo(),
                paciente.getEdad() + " anos");
        }
        
        System.out.println("+" + "-".repeat(78) + "+");
        
        // Mostrar salas disponibles
        mostrarSalasDisponibles(hospital);
    }
    
    private static void mostrarSalasDisponibles(Hospital hospital) {
        System.out.println("\n" + "+" + "-".repeat(60) + "+");
        System.out.printf("| %-58s |%n", "[SALAS] DISPONIBLES");
        System.out.println("+" + "-".repeat(15) + "+" + "-".repeat(20) + "+" + "-".repeat(20) + "+");
        System.out.printf("| %-15s | %-20s | %-20s |%n", "SALA", "DESCRIPCION", "ESTADO");
        System.out.println("+" + "-".repeat(15) + "+" + "-".repeat(20) + "+" + "-".repeat(20) + "+");
        
        for (Departamento dept : hospital.getDepartamentos()) {
            for (Sala sala : dept.getSalas()) {
                String estado = sala.getEstaDisponible() ? "[OK] Disponible" : "[X] Ocupada";
                System.out.printf("| %-15s | %-20s | %-20s |%n",
                    truncar(sala.getNumero(), 15),
                    truncar(sala.getDescripcion(), 20),
                    estado);
            }
        }
        
        System.out.println("+" + "-".repeat(60) + "+");
    }
    
    private static void mostrarEstadisticasGenerales() {
        Long totalPacientes = em.createQuery("SELECT COUNT(p) FROM Paciente p", Long.class)
            .getSingleResult();
        Long totalMedicos = em.createQuery("SELECT COUNT(m) FROM Medico m", Long.class)
            .getSingleResult();
        Long totalSalas = em.createQuery("SELECT COUNT(s) FROM Sala s", Long.class)
            .getSingleResult();
        Long totalDepartamentos = em.createQuery("SELECT COUNT(d) FROM Departamento d", Long.class)
            .getSingleResult();
        
        System.out.println("\n" + "+" + "-".repeat(50) + "+");
        System.out.printf("| %-48s |%n", "[ESTADISTICAS] GENERALES DEL HOSPITAL");
        System.out.println("+" + "-".repeat(25) + "+" + "-".repeat(23) + "+");
        System.out.printf("| %-25s | %-23s |%n", "Total Pacientes", totalPacientes);
        System.out.printf("| %-25s | %-23s |%n", "Total Medicos", totalMedicos);
        System.out.printf("| %-25s | %-23s |%n", "Total Departamentos", totalDepartamentos);
        System.out.printf("| %-25s | %-23s |%n", "Total Salas", totalSalas);
        System.out.println("+" + "-".repeat(50) + "+");
    }
    
    private static String truncar(String texto, int maxLength) {
        if (texto == null) return "";
        return texto.length() <= maxLength ? texto : texto.substring(0, maxLength - 3) + "...";
    }
}