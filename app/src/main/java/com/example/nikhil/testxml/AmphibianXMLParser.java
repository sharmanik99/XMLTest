package com.example.nikhil.testxml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AmphibianXMLParser {
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "amphibiaweb");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the species tag
            if (name.equals("amphibian")) {
                entries.add(readAmphibians(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Amphibian readAmphibians(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "amphibian");
        String order = null;
        String family = null;
        String scientificName = null;
        String genus = null;
        String species = null;
        String imageID = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("order")) {
                order = readOrder(parser);
            } else if (name.equals("family")) {
                family = readFamily(parser);
            } else if (name.equals("scientificname")) {
                scientificName = readScientificName(parser);
                genus = scientificName.split(" ")[0];
                species = scientificName.split(" ")[1];
            } else if (name.equals("image")) {
                imageID = readImageID(parser);
            } else {
                skip(parser);
            }
        }
        return new Amphibian(order, family, scientificName, genus, species, imageID);
    }


    private String readOrder(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "order");
        String order = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "order");
        return order;
    }

    private String readFamily(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "family");
        String family = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "family");
        return family;
    }
    private String readScientificName(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "scientificname");
        String scientificname = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "scientificname");
        return scientificname;
    }
    private String readImageID(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "image");
        String image = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "image");
        return image;
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