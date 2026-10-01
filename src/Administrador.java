public class Administrador extends Persona {

    private String contrasena;

    public Administrador(String id, String nombreCompleto, String contrasena) {
        super(id, nombreCompleto);
        this.contrasena = contrasena;
    }

    public boolean validarAcceso(String id, String contrasena) {
        return this.id.equals(id) && this.contrasena.equals(contrasena);
    }

    @Override
    public String toCsv() {
        return id + "," + nombreCompleto + "," + contrasena;
    }
}