package co.edu.udea.compumovil.gr02_20182.proyecto.Model;

public class Levante {

    private String id;
    private  String lote;
    private String name;
    private String gender;
    private  String race;
    private String type;
    private String dateI;
    private String numberPin;
    private String observation;

    public Levante() {

    }

    public Levante(String id, String lote, String name, String gender, String race, String type, String dateI, String numberPin, String observation) {
        this.id = id;
        this.lote = lote;
        this.name = name;
        this.gender = gender;
        this.race = race;
        this.type = type;
        this.dateI = dateI;
        this.numberPin = numberPin;
        this.observation = observation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateI() {
        return dateI;
    }

    public void setDateI(String dateI) {
        this.dateI = dateI;
    }

    public String getNumberPin() {
        return numberPin;
    }

    public void setNumberPin(String numberPin) {
        this.numberPin = numberPin;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
