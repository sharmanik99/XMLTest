package com.example.nikhil.testxml;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private static final String URL = "https://amphibiaweb.org/cgi/amphib_ws_locality?where-isocc=fr&rel-isocc=like";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toast.makeText(this, "Testing <amphibian> XML tags", Toast.LENGTH_SHORT).show();
        //new DownloadXmlTask.exe
        new DownloadXmlTask().execute(URL);
    }
    private class DownloadXmlTask extends AsyncTask<String, Void, List<Amphibian>>{
        @Override
        protected List<Amphibian> doInBackground(String... urls) {
            try{
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e){
                return null;
            } catch (XmlPullParserException e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<Amphibian> amphibians) {
            setContentView(R.layout.activity_main2);
            //MAKE A LIST HERE
            ArrayList<String> amphibianNames = new ArrayList<>();


            for(Amphibian amphibian : amphibians){
                //Toast.makeText(Main2Activity.this, amphibian.scientificName, Toast.LENGTH_SHORT).show();
                amphibianNames.add(amphibian.scientificName);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_list_item_1, amphibianNames);
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(Main2Activity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main2Activity.this);
                    Amphibian tempAmph = amphibians.get(position);
                    alertDialogBuilder.setTitle("Learn More About " + tempAmph.scientificName);
                    alertDialogBuilder.setMessage("Order: " + tempAmph.order + "\nFamily: " + tempAmph.family + "\nGenus: " + tempAmph.genus + "\nSpecies: " + tempAmph.species);
                    alertDialogBuilder.setPositiveButton("View More", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Main2Activity.this, "Open Species Page about this", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialogBuilder.show();
                }
            });
        }
    }

    private List<Amphibian> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException{
        InputStream stream = null;
        //Initiate Amphibian XML Parser
        AmphibianXMLParser amphibianXMLParser = new AmphibianXMLParser();
        List<Amphibian> results = null;
        try {
            stream = downloadUrl(urlString);
            results = amphibianXMLParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return results;
    }
    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

}
