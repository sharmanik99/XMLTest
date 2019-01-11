package com.example.nikhil.testxml;

public class Species {
    public final String order;
    public final String family;
    public final String genus;
    public final String species;
    public final String common_name;
    public final String isocc;
    public final String description;

    public Species(String order, String family, String genus, String species, String common_name, String isocc, String description) {
        this.order = order;
        this.family = family;
        this.genus = genus;
        this.species = species;
        this.common_name = common_name;
        this.isocc = isocc;
        this.description = description;
    }
}
