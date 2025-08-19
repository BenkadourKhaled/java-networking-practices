package uriExamples;

import java.io.File;
import java.net.MalformedURLException;

public class MainTestGetUriFile {

    public static void main(String[] args) throws MalformedURLException {
        File file = new File("uriExamples/test.txt");
        System.out.println(file.toURI().toString());
    }

}
