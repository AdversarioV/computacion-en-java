public class Paciente extends Persona {

    public Paciente(String id, String nombreCompleto) {
        super(id, nombreCompleto);
    }

    @Override
    public String toCsv() {
        return id + "," + nombreCompleto;
    }
}