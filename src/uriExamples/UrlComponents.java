package uriExamples;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlComponents {

    public static void main(String[] args) throws MalformedURLException, URISyntaxException {

        URI uri1 = new URI("http", null, "hostname", 80, "/index.html", null, null);
        URL url = uri1.toURL();

        URI uri2 = new URI("http://hostname:8080/index.html");

        System.out.println(url.toExternalForm());

    }
}
