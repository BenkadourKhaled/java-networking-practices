package uriExamples;

import java.net.URI;
import java.net.URISyntaxException;

public class MethodsInUri {

    public static void main(String[] args) throws URISyntaxException {
        URI uri = new URI("https://software.opensuse.org/123/en");
        System.out.println("Auth = " + uri.getAuthority());
        System.out.println("Fragment = " + uri.getFragment());
        System.out.println("Host = " + uri.getHost());
        System.out.println("Path = " + uri.getPath());
        System.out.println("Port = " + uri.getPort());
        System.out.println("Query = " + uri.getQuery());
        System.out.println("Scheme = " + uri.getScheme());
        System.out.println("SchemeSpecificPart = " + uri.getSchemeSpecificPart());
        System.out.println("User Info = " + uri.getUserInfo());
        System.out.println("URL is absolute = " + uri.isAbsolute());
        System.out.println("URL is opaque = " + uri.isOpaque());
    }

}
