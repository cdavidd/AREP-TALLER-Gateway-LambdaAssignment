package edu.escuelaing.arep;

import static spark.Spark.get;
import static spark.Spark.port;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import spark.Request;
import spark.Response;

/**
 *
 * @author cristian.lopez-a
 */
public class SparkWebApp {

    private static final String url = "https://3d7xiohuc0.execute-api.us-east-1.amazonaws.com/beta?value=";
    private final static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) {
        port(getPort());
        // get("/", (req, res) -> inputDataPage(req, res));
        get("/", SparkWebApp::inputDataPage);
        // get("/results", (req, res) -> resultsPage(req, res));
        get("/results", SparkWebApp::resultsPage);
    }

    /**
     * @param req tipo Request
     * @param res tipo Response
     * @return pageContent contenido html de la pagina
     */
    public static String inputDataPage(Request req, Response res) {
        String pageContent = "<!DOCTYPE html>" + "<html>" + "<body>" + "<h1>Calculate Square</h1>"
                + "<p>Ingresar numero</p>" + "<form action=\"/results\">" + "  Datos a ingresar:<br>"
                + "  <input type=\"number\" name=\"numero\" placeholder=\"0\">" + "  <br>"
                + "  <input type=\"submit\" value=\"Calcular\">" + "</form>" + "</body>" + "</html>";
        return pageContent;
    }

    public static String resultsPage(Request req, Response res) {
        String ress;
        try {
            ress = getResult(req.queryParams("numero"));
            String pageContent = "<html><body><h1>Result<h1><h2>" + ress + "</h2></body></html>";
            return pageContent;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String pageContent = "<html><body><h1>Result<h1>" + "</body></html>";
        return pageContent;
    }

    public static String getResult(String number) throws ParseException, IOException {
        HttpGet request = new HttpGet(url + number);
        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
                return result;
            }
        }
        return "404";
    }

    /**
     * @return port retorne al puerto a la cual la aplicacion sale
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; // returns default port if heroku-port isn't set(i.e. on localhost)
    }
}