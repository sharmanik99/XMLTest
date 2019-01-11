package com.example.nikhil.testxml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SpeciesXMLParser {

    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException{
        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException{
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "amphibiaweb");
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the species tag
            if(name.equals("species")) {
                entries.add(readSpecies(parser));
            } else{
                skip(parser);
            }
        }
        return entries;
    }

    private Species readSpecies(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, "species");
        String order = null;
        String family = null;
        String genus = null;
        String species = null;
        String common_name = null;
        String isocc = null;
        String description = null;
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("ordr")){
                order = readOrder(parser);
            } else if(name.equals("family")){
                family = readFamily(parser);
            } else if(name.equals("genus")){
                genus = readGenus(parser);
            } else if(name.equals("species")){
                species = readSpeciesTag(parser);
            } else if(name.equals("common_name")){
                common_name = readCommonName(parser);
            } else if(name.equals("isocc")){
                isocc = readISOCC(parser);
            } else if(name.equals("description")){
                description = readDescription(parser);
            } else{
                skip(parser);
            }
        }
        return new Species(order, family, genus, species, common_name, isocc, description);
    }

    private String readOrder(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "ordr");
        String order = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "ordr");
        return order;
    }
    private String readFamily(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "family");
        String family = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "family");
        return family;
    }
    private String readGenus(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "genus");
        String genus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "genus");
        return genus;
    }
    private String readSpeciesTag(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "species");
        String speciesTag = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "species");
        return speciesTag;
    }
    private String readCommonName(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "common_name");
        String commonName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "common_name");
        return commonName;
    }
    private String readISOCC(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "isocc");
        String ISOCC = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "isocc");
        return ISOCC;
    }
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}


