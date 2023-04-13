package hr.java.production.enumeration;

public enum Gradovi {
    ZAGREB("Zagreb",10000),
    RIJEKA("Rijeka",20000),
    SPLIT("Split",50000);

    private Integer broj;
    private String ime;
    Gradovi(String ime, Integer broj) {
        this.ime = ime;
        this.broj=broj;
    }

    public Integer getBroj() {
        return broj;
    }

    public String getIme() {
        return ime;
    }
}
