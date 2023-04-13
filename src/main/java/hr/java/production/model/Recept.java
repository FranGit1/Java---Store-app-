package hr.java.production.model;

import java.time.LocalDateTime;

public class Recept {
    public String napomena;
    public Integer redniBroj;
    public Lijek lijek;
    public LocalDateTime time;
    public String dostava;


    public Recept(String napomena, Integer redniBroj, Lijek lijek, LocalDateTime time, String dostava) {
        this.napomena = napomena;
        this.redniBroj = redniBroj;
        this.lijek = lijek;
        this.time = time;
        this.dostava = dostava;
    }
}
