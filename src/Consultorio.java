import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Consultorio {

    private static final Path DIR_DB = Paths.get("db");
    private static final Path ARCH_ADMINS = DIR_DB.resolve("administradores.csv");
    private static final Path ARCH_DOCTORES = DIR_DB.resolve("doctores.csv");
    private static final Path ARCH_PACIENTES = DIR_DB.resolve("pacientes.csv");
    private static final Path ARCH_CITAS = DIR_DB.resolve("citas.csv");

    private List<Administrador> administradores = new ArrayList<>();
    private List<Doctor> doctores = new ArrayList<>();
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Cita> citas = new ArrayList<>();

    // Crea la carpeta db y los archivos faltantes, y carga los datos existentes
    public void cargarDatos() {
        try {
            Files.createDirectories(DIR_DB);
            asegurarArchivo(ARCH_ADMINS, "admin,Administrador,1234" + System.lineSeparator());
            asegurarArchivo(ARCH_DOCTORES, "");
            asegurarArchivo(ARCH_PACIENTES, "");
            asegurarArchivo(ARCH_CITAS, "");

            for (String[] r : leerCsv(ARCH_ADMINS, 3)) {
                administradores.add(new Administrador(r[0], r[1], r[2]));
            }
            for (String[] r : leerCsv(ARCH_DOCTORES, 3)) {
                doctores.add(new Doctor(r[0], r[1], r[2]));
            }
            for (String[] r : leerCsv(ARCH_PACIENTES, 2)) {
                pacientes.add(new Paciente(r[0], r[1]));
            }
            for (String[] r : leerCsv(ARCH_CITAS, 5)) {
                Doctor doctor = buscarDoctor(r[3]);
                Paciente paciente = buscarPaciente(r[4]);
                if (doctor == null || paciente == null) {
                    continue;
                }
                try {
                    LocalDateTime fecha = LocalDateTime.parse(r[1], Cita.FORMATO);
                    citas.add(new Cita(r[0], fecha, r[2], doctor, paciente));
                } catch (DateTimeParseException e) {
                    System.out.println("Cita " + r[0] + " omitida: fecha invalida en el archivo.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error al preparar los archivos: " + e.getMessage());
        }
    }

    private void asegurarArchivo(Path archivo, String contenidoInicial) throws IOException {
        if (!Files.exists(archivo)) {
            Files.write(archivo, contenidoInicial.getBytes(StandardCharsets.UTF_8));
        }
    }

    private List<String[]> leerCsv(Path archivo, int columnas) throws IOException {
        List<String[]> registros = new ArrayList<>();
        for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
            if (linea.isBlank()) {
                continue;
            }
            String[] partes = linea.split(",", -1);
            if (partes.length == columnas) {
                registros.add(partes);
            }
        }
        return registros;
    }

    private void guardar(Path archivo, List<? extends Almacenable> registros) throws ConsultorioException {
        List<String> lineas = new ArrayList<>();
        for (Almacenable registro : registros) {
            lineas.add(registro.toCsv());
        }
        try {
            Files.write(archivo, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ConsultorioException("No se pudo guardar en " + archivo + ": " + e.getMessage());
        }
    }

    // Valida que no este vacio y quita comas para no romper el CSV
    private String limpiar(String texto, String campo) throws ConsultorioException {
        if (texto == null || texto.isBlank()) {
            throw new ConsultorioException("El campo " + campo + " no puede estar vacio.");
        }
        return texto.trim().replace(",", " ");
    }

    public boolean validarAcceso(String id, String contrasena) {
        for (Administrador admin : administradores) {
            if (admin.validarAcceso(id, contrasena)) {
                return true;
            }
        }
        return false;
    }

    public void altaDoctor(String id, String nombre, String especialidad) throws ConsultorioException {
        id = limpiar(id, "identificador");
        nombre = limpiar(nombre, "nombre completo");
        especialidad = limpiar(especialidad, "especialidad");
        if (buscarDoctor(id) != null) {
            throw new ConsultorioException("Ya existe un doctor con el identificador " + id);
        }
        doctores.add(new Doctor(id, nombre, especialidad));
        guardar(ARCH_DOCTORES, doctores);
    }

    public void altaPaciente(String id, String nombre) throws ConsultorioException {
        id = limpiar(id, "identificador");
        nombre = limpiar(nombre, "nombre completo");
        if (buscarPaciente(id) != null) {
            throw new ConsultorioException("Ya existe un paciente con el identificador " + id);
        }
        pacientes.add(new Paciente(id, nombre));
        guardar(ARCH_PACIENTES, pacientes);
    }

    public void validarDisponibilidad() throws ConsultorioException {
        if (doctores.isEmpty() || pacientes.isEmpty()) {
            throw new ConsultorioException("Debe existir al menos un doctor y un paciente registrados.");
        }
    }

    public void altaCita(String id, String fechaTexto, String motivo,
                         String idDoctor, String idPaciente) throws ConsultorioException {
        validarDisponibilidad();
        id = limpiar(id, "identificador");
        motivo = limpiar(motivo, "motivo");
        if (buscarCita(id) != null) {
            throw new ConsultorioException("Ya existe una cita con el identificador " + id);
        }
        Doctor doctor = buscarDoctor(idDoctor.trim());
        if (doctor == null) {
            throw new ConsultorioException("Doctor no encontrado: " + idDoctor);
        }
        Paciente paciente = buscarPaciente(idPaciente.trim());
        if (paciente == null) {
            throw new ConsultorioException("Paciente no encontrado: " + idPaciente);
        }
        LocalDateTime fecha;
        try {
            fecha = LocalDateTime.parse(fechaTexto.trim(), Cita.FORMATO);
        } catch (DateTimeParseException e) {
            throw new ConsultorioException("Formato de fecha invalido, use dd/MM/yyyy HH:mm");
        }
        citas.add(new Cita(id, fecha, motivo, doctor, paciente));
        guardar(ARCH_CITAS, citas);
    }

    public Doctor buscarDoctor(String id) {
        for (Doctor d : doctores) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }

    public Paciente buscarPaciente(String id) {
        for (Paciente p : pacientes) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Cita buscarCita(String id) {
        for (Cita c : citas) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public List<Doctor> getDoctores() {
        return doctores;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Cita> getCitas() {
        return citas;
    }
}