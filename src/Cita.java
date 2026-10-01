import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cita implements Almacenable {

    public static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private String id;
    private LocalDateTime fechaHora;
    private String motivo;
    private Doctor doctor;
    private Paciente paciente;

    public Cita(String id, LocalDateTime fechaHora, String motivo, Doctor doctor, Paciente paciente) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.doctor = doctor;
        this.paciente = paciente;
    }

    @Override
    public String getId() {
        return id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    @Override
    public String toCsv() {
        return id + "," + fechaHora.format(FORMATO) + "," + motivo + ","
                + doctor.getId() + "," + paciente.getId();
    }

    @Override
    public String toString() {
        return id + " | " + fechaHora.format(FORMATO) + " | " + motivo
                + " | Dr. " + doctor.getNombreCompleto()
                + " | Paciente: " + paciente.getNombreCompleto();
    }
}