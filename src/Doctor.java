public class Doctor extends Persona {

    private String especialidad;

    public Doctor(String id, String nombreCompleto, String especialidad) {
        super(id, nombreCompleto);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    @Override
    public String toCsv() {
        return id + "," + nombreCompleto + "," + especialidad;
    }

    @Override
    public String toString() {
        return id + " - " + nombreCompleto + " (" + especialidad + ")";
    }
}