package uriExamples.practiceExamples;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class UrlInfo {

    public static void getInfo(URL url) throws IOException {
        URLConnection urlCon = url.openConnection();
        urlCon.connect();
        System.out.println("Content type : " + urlCon.getContentType());
        System.out.println("Content Encoding : " + urlCon.getContentEncoding());
        System.out.println("Content Length : " + urlCon.getContentLength());
        System.out.println("Date : " + new Date(urlCon.getDate()));
        System.out.println("last Modified Date : " + new Date(urlCon.getLastModified()));
        System.out.println("Expiration  : " + urlCon.getExpiration());
        if (urlCon instanceof HttpURLConnection httpCon) {
            System.out.println("Request Method : " + httpCon.getRequestMethod());
            System.out.println("Response Message : " + httpCon.getResponseMessage());
            System.out.println("Response Code : " + httpCon.getResponseCode());
        }
    }

}
