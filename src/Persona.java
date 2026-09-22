public abstract class Persona {

    protected String id;
    protected String nombreCompleto;

    public Persona(String id, String nombreCompleto) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
    }

    public String getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public abstract String toCsv();

    @Override
    public String toString() {
        return id + " - " + nombreCompleto;
    }
}