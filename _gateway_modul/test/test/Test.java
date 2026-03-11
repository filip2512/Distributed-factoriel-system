/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.io.IOException;

/**
 *
 * @author FILIP KOSTIC
 */
public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
    
        var client = java.net.http.HttpClient.newHttpClient();
        var request = java.net.http.HttpRequest.newBuilder(
                java.net.URI.create("http://localhost:8080/start"))
            .header("Content-Type", "application/json")
            .POST(java.net.http.HttpRequest.BodyPublishers.ofString(
                "{\"number\":20,\"threads\":4}"))
            .build();

        var response = client.send(request,java.net.http.HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());
    }
}
