/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import executor.Executor;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
/**
 *
 * @author FILIP KOSTIC
 */
public class GatewayExecutor implements Executor {
    
    private final String computationServerUrl;
    private final HttpClient client;
    
    public GatewayExecutor(String computationServerUrl) {
        this.computationServerUrl = computationServerUrl;
        this.client = HttpClient.newHttpClient();
    }
    private String sendPostJson(String path, String jsonBody) throws Exception {
        URI uri = new URI(computationServerUrl + path);
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    @Override
    public String startProcess(int number, int threads) throws Exception {
        String json = String.format(
                "{\"number\": %d, \"threads\": %d}",
                number, threads
        );
        return sendPostJson("/start", json);
    }
    @Override
    public String getStatus(String id) throws Exception {
        String json = String.format("{\"id\": \"%s\"}", id);
        return sendPostJson("/status", json);
    }
    @Override
    public String getResult(String id) throws Exception {
        String json = String.format("{\"id\": \"%s\"}", id);
        return sendPostJson("/result", json);
    }
    @Override
    public void stopProcess(String id) throws Exception {
        String json = String.format("{\"id\": \"%s\"}", id);
        sendPostJson("/stop", json);
    }
    @Override
    public String recommenceProcess(String id) throws Exception {
        String json = String.format("{\"id\": \"%s\"}", id);
        return sendPostJson("/recommence", json);
    }
}
