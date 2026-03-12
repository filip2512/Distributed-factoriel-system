/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import com.sun.net.httpserver.HttpExchange;
import exception.InvalidRequestException;
import exception.ProcessNotFoundException;
import exception.ServiceUnavailableException;
import exception.StorageException;
import validation.RequestValidator;
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
public class RecommenceHandler implements HttpHandler {

    private final Executor executor;

    public RecommenceHandler(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            String response = "Method not allowed, use POST";
            exchange.sendResponseHeaders(405, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }

        try (InputStream is = exchange.getRequestBody()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(body);

            if (!json.has("id")) {
                throw new InvalidRequestException("Required field: id");
            }
            String id = json.getString("id");
            RequestValidator.validateProcessId(id);
            String newId = executor.recommenceProcess(id);

            String response = "Recommenced: " + newId;
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }

        } catch (InvalidRequestException e) {
            sendError(exchange, 400, "400 Bad Request: " + e.getMessage());
        } catch (ProcessNotFoundException e) {
            sendError(exchange, 404, "404 NOT_FOUND: " + e.getMessage());
        } catch (StorageException e) {
            sendError(exchange, 500, "500 Internal Server Error: " + e.getMessage());
        } catch (ServiceUnavailableException e) {
            sendError(exchange, 503, "503 Service Unavailable: " + e.getMessage());
        } catch (IllegalArgumentException | org.json.JSONException e) {
            sendError(exchange, 400, "400 Bad Request: " + e.getMessage());
        } catch (Exception e) {
            sendError(exchange, 500, "500 Internal Server Error: " + e.getMessage());
        }
    }

    private void sendError(HttpExchange exchange, int code, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }
}
