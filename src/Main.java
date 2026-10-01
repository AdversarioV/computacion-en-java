import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Consultorio consultorio = new Consultorio();
        consultorio.cargarDatos();

        System.out.println("=== SISTEMA DE CITAS MEDICAS ===");
        if (!login(consultorio)) {
            System.out.println("Acceso denegado.");
            return;
        }

        int opcion = 0;
        do {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            try {
                switch (opcion) {
                    case 1:
                        altaDoctor(consultorio);
                        break;
                    case 2:
                        altaPaciente(consultorio);
                        break;
                    case 3:
                        altaCita(consultorio);
                        break;
                    case 4:
                        consultar(consultorio);
                        break;
                    case 5:
                        System.out.println("Hasta luego.");
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }
            } catch (ConsultorioException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 5);
    }

    private static boolean login(Consultorio consultorio) {
        for (int intento = 1; intento <= 3; intento++) {
            System.out.print("Identificador: ");
            String id = scanner.nextLine().trim();
            System.out.print("Contrasena: ");
            String contrasena = scanner.nextLine().trim();
            if (consultorio.validarAcceso(id, contrasena)) {
                System.out.println("Acceso concedido.");
                return true;
            }
            System.out.println("Credenciales incorrectas (intento " + intento + " de 3).");
        }
        return false;
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=== MENU PRINCIPAL ===");
        System.out.println("1) Dar de alta doctor");
        System.out.println("2) Dar de alta paciente");
        System.out.println("3) Crear cita");
        System.out.println("4) Consultar registros");
        System.out.println("5) Salir");
        System.out.print("Opcion: ");
    }

    private static void altaDoctor(Consultorio consultorio) throws ConsultorioException {
        System.out.print("Identificador del doctor: ");
        String id = scanner.nextLine();
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine();
        consultorio.altaDoctor(id, nombre, especialidad);
        System.out.println("Doctor registrado correctamente.");
    }

    private static void altaPaciente(Consultorio consultorio) throws ConsultorioException {
        System.out.print("Identificador del paciente: ");
        String id = scanner.nextLine();
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        consultorio.altaPaciente(id, nombre);
        System.out.println("Paciente registrado correctamente.");
    }

    private static void altaCita(Consultorio consultorio) throws ConsultorioException {
        consultorio.validarDisponibilidad();

        System.out.print("Identificador de la cita: ");
        String id = scanner.nextLine();

        imprimir("Doctores disponibles", consultorio.getDoctores());
        System.out.print("Identificador del doctor: ");
        String idDoctor = scanner.nextLine();

        imprimir("Pacientes registrados", consultorio.getPacientes());
        System.out.print("Identificador del paciente: ");
        String idPaciente = scanner.nextLine();

        System.out.print("Fecha y hora (dd/MM/yyyy HH:mm): ");
        String fecha = scanner.nextLine();
        System.out.print("Motivo de la cita: ");
        String motivo = scanner.nextLine();

        consultorio.altaCita(id, fecha, motivo, idDoctor, idPaciente);
        System.out.println("Cita registrada correctamente.");
    }

    private static void consultar(Consultorio consultorio) {
        imprimir("Doctores", consultorio.getDoctores());
        imprimir("Pacientes", consultorio.getPacientes());
        imprimir("Citas", consultorio.getCitas());
    }

    private static void imprimir(String titulo, List<?> registros) {
        System.out.println("--- " + titulo + " ---");
        if (registros.isEmpty()) {
            System.out.println("(sin registros)");
            return;
        }
        for (Object registro : registros) {
            System.out.println(registro);
        }
    }
}