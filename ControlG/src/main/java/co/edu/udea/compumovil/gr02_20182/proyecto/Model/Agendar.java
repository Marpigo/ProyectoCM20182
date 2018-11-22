package co.edu.udea.compumovil.gr02_20182.proyecto.Model;

public class Agendar {

    private String id;
    private String date;
    private String activity;
    private String lote;
    private String observacion;
    private String estado;

    public Agendar() {

    }

    public Agendar(String id, String date, String activity, String lote, String observacion, String estado) {
        this.id = id;
        this.date = date;
        this.activity = activity;
        this.lote = lote;
        this.observacion = observacion;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
