package co.edu.udea.compumovil.gr02_20182.proyecto.Model;

public class Insumo {

    private String id;
    private String supply;
    private String presentation;
    private String line;
    private String brand;
    private String species;
    private String datei;
    private String balance;

    public Insumo() {

    }

    public Insumo(String id, String supply, String presentation, String line, String brand, String species, String datei, String balance) {
        this.id = id;
        this.supply = supply;
        this.presentation = presentation;
        this.line = line;
        this.brand = brand;
        this.species = species;
        this.datei = datei;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDatei() {
        return datei;
    }

    public void setDatei(String datei) {
        this.datei = datei;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
