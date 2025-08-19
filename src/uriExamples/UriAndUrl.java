package uriExamples;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UriAndUrl {

    public static void main(String[] args) throws URISyntaxException, MalformedURLException {


        URI uri = null;
        URL url = null;

        uri = new URI("test.txt");
        url = uri.toURL();
        uri = new URI(url.toString());



    }

}
