package com.example.nikhil.testxml;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://amphibiaweb.org/cgi/amphib_ws?where-genus=Agalychnis&where-species=callidryas&src=eol";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Starting XML Download!", Toast.LENGTH_LONG).show();
        new DownloadXmlTask().execute(URL);
        //Toast.makeText(this, loadXmlFromNetwork(URL), Toast.LENGTH_SHORT).show();
    }
    private class DownloadXmlTask extends AsyncTask<String, Void, List<Species>> {
        @Override
        protected List<Species> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return null;
            } catch (XmlPullParserException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Species> result) {
            setContentView(R.layout.activity_main);
            // Displays the HTML string in the UI via a WebView
//            TextView textView = (TextView) findViewById(R.id.displayLabel);
//            textView.setText(result);
            Species tempSpecies = result.get(0);
//            for(Species species:result){
//                Toast.makeText(MainActivity.this, species.common_name, Toast.LENGTH_SHORT).show();
//            }
            TextView commonName = (TextView) findViewById(R.id.commonNameLabel);
            EditText descriptionLabel = (EditText) findViewById(R.id.descriptionLabel);
            TextView locationLabel = (TextView) findViewById(R.id.locationLabel);
            TextView familyLabel = (TextView) findViewById(R.id.familyLabel);
            TextView orderLabel = (TextView) findViewById(R.id.orderLabel);
            TextView genusLabel = (TextView) findViewById(R.id.genusLabel);
            TextView speciesLabel = (TextView) findViewById(R.id.speciesLabel);

            List<String> commonNameList = Arrays.asList(tempSpecies.common_name.split(", "));
            String displayNameList = "";
            for(String name: commonNameList){
                displayNameList +=  name;
                displayNameList += "\n";
            }
            commonName.setText(displayNameList);
            orderLabel.setText(tempSpecies.order);
            familyLabel.setText(tempSpecies.family);
            genusLabel.setText(tempSpecies.genus);
            speciesLabel.setText(tempSpecies.species);
            locationLabel.setText(tempSpecies.isocc);
            descriptionLabel.setText(tempSpecies.description);

        }
    }

    private List<Species> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException{
        InputStream stream = null;
        // Initiate parser
        SpeciesXMLParser speciesXMLParser = new SpeciesXMLParser();
        List<Species> speciesList = null;
        String common_name = null;
        try {
            stream = downloadUrl(urlString);
            speciesList = speciesXMLParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
//        for (Species species : speciesList){
//            //Toast.makeText(this, species.common_name, Toast.LENGTH_SHORT).show();
//            common_name = species.common_name;
//        }
        return speciesList;

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
