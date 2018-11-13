package co.edu.udea.compumovil.gr02_20182.proyecto.Model;

public class User {

    private String id;
    private String name;
    private String email;
    private  String password;
    private String imagen;
    private int autenticado = 0;

    public User() {
    }

    public User(String id, String name, String email, String password, String imagen, int autenticado) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.imagen = imagen;
        this.autenticado = autenticado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getAutenticado() {
        return autenticado;
    }

    public void setAutenticado(int autenticado) {
        this.autenticado = autenticado;
    }
}
