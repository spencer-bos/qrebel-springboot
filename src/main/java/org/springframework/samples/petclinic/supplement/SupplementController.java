package org.springframework.samples.petclinic.supplement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Controller
@SessionAttributes("supplements")
public class SupplementController {

    @Value("${supplements.host}")
    private String host;
    static final Logger LOG = LoggerFactory.getLogger(SupplementController.class);

    @RequestMapping(value = "/supplements", method = RequestMethod.GET)
    public String processFindForm(Map<String, Object> model) {
        Collection<Supplement> results = getSupplements();
        model.put("supplements", results);
	  	LOG.info("I am fine.");
		LOG.warn("I love programming.");
		LOG.trace("This is the tracing feature");
		LOG.debug("The debug feature");
		LOG.debug(results.toString());

//        DEMO: Show Exception is thrown
/*        if (true) {
            throw new RuntimeException("KEYBOARD NOT FOUND, PRESS F1 TO CONTINUE");
        }*/
        return "supplements/supplementList";
    }

    private Collection<Supplement> getSupplements() {
        String json = getRemoteSupplementsJson();

        try {
            ObjectMapper mapper = new ObjectMapper();
            Supplement[] supplements = mapper.readValue(json, Supplement[].class);
            return Arrays.asList(supplements);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getLocalSupplementsJSon() {
        return
            "[" +
                "{\"name\": \"Apples\", \"price\": \"123.6\"}," +
                "{\"name\": \"Bananas\", \"price\": \"234.7\"}," +
                "{\"name\": \"Carrots\", \"price\": \"345.8\"}," +
                "{\"name\": \"Vitamin D\", \"price\": \"456.9\"}," +
                "{\"name\": \"Vitamin E\", \"price\": \"987.9\"}" +
            "]";
    }

    private String getRemoteSupplementsJson() {
        StringBuilder sb = new StringBuilder();
//        DEMO: Show slow request example
        try {
            Thread.sleep(13000); // fake delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            String spec = "http://supplements-service:8080/supplements/";
            System.out.println("Calling to " + spec);

            URL url = new URL(spec);
            URLConnection urlConnection = url.openConnection();

            urlConnection.setConnectTimeout(4000);
            try {
                urlConnection.connect();
            } catch (UnknownHostException | SocketTimeoutException | ConnectException e) {
                System.out.println("Whoops! Service not available, serving static data. 8-)");
                return getLocalSupplementsJSon();
            }

            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
