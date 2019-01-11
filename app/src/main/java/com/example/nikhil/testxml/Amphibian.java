package com.example.nikhil.testxml;

public class Amphibian {
    public final String order;
    public final String family;
    public final String scientificName;
    public final String genus;
    public final String species;
    public final String imageID;
    //public final String audioURL; //Add audio in later
    //public final String iucn_uuid;

    public Amphibian(String order, String family, String scientificName, String genus, String species, String imageID) {
        this.order = order;
        this.family = family;
        this.scientificName = scientificName;
        this.genus = genus;
        this.species = species;
        this.imageID = imageID;
    }
}
