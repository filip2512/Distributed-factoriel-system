/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import executor.Executor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;


/**
 *
 * @author FILIP KOSTIC
 */
public class StatusHandler implements HttpHandler {

    private final Executor executor;

    public StatusHandler(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            String response = "Method not allowed, use POST";
            exchange.sendResponseHeaders(405, response.length());
            try (OutputStream os = exchange.getResponseBody()) { os.write(response.getBytes()); }
            return;
        }

        try (InputStream is = exchange.getRequestBody()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(body);

            if (!json.has("id") || json.getString("id").isEmpty()) {
                throw new IllegalArgumentException("Missing or empty id");
            }

            String id = json.getString("id");
            String status = executor.getStatus(id);

            exchange.sendResponseHeaders(200, status.length());
            try (OutputStream os = exchange.getResponseBody()) { os.write(status.getBytes()); }

        } catch (Exception e) {
            String response = "Error: " + e.getMessage();
            exchange.sendResponseHeaders(400, response.length());
            try (OutputStream os = exchange.getResponseBody()) { os.write(response.getBytes()); }
        }
    }
}
