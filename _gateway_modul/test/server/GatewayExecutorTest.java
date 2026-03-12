/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import exception.InvalidRequestException;
import exception.ProcessNotFoundException;
import exception.ServiceUnavailableException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

/**
 *
 * @author FILIP KOSTIC
 */
public class GatewayExecutorTest {

    private MockWebServer mockWebServer;
    private GatewayExecutor executor;

    @Before
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        executor = new GatewayExecutor(mockWebServer.url("/").toString().replaceAll("/$", ""));
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void startProcess_validRequest_returnsResponseFromServer() throws Exception {
        mockWebServer.enqueue(new MockResponse().setBody("Process started: uuid-123").setResponseCode(200));

        String result = executor.startProcess(20, 4);

        assertEquals("Process started: uuid-123", result);
    }

    @Test(expected = InvalidRequestException.class)
    public void startProcess_invalidNumber_throws() throws Exception {
        executor.startProcess(0, 4);
    }

    @Test(expected = InvalidRequestException.class)
    public void startProcess_invalidThreads_throws() throws Exception {
        executor.startProcess(10, 0);
    }

    @Test
    public void getStatus_validId_returnsResponseFromServer() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody("RUNNING")
                .setResponseCode(200));

        String result = executor.getStatus("valid-uuid");

        assertEquals("RUNNING", result);
    }

    @Test(expected = InvalidRequestException.class)
    public void getStatus_emptyId_throws() throws Exception {
        executor.getStatus("");
    }

    @Test(expected = ProcessNotFoundException.class)
    public void getStatus_404FromServer_throws() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody("Process not found")
                .setResponseCode(404));

        executor.getStatus("nonexistent-id");
    }

    @Test(expected = ServiceUnavailableException.class)
    public void getStatus_500FromServer_throws() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody("Internal error")
                .setResponseCode(500));

        executor.getStatus("any-id");
    }

    @Test
    public void getResult_validId_returnsResponseFromServer() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody("2432902008176640000")
                .setResponseCode(200));

        String result = executor.getResult("valid-uuid");

        assertEquals("2432902008176640000", result);
    }

    @Test
    public void stopProcess_validId_succeeds() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody("Stopped: valid-uuid")
                .setResponseCode(200));

        executor.stopProcess("valid-uuid");
        // no exception means success
    }

    @Test
    public void recommenceProcess_validId_returnsResponseFromServer() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody("Recommenced: uuid-456")
                .setResponseCode(200));

        String result = executor.recommenceProcess("valid-uuid");

        assertEquals("Recommenced: uuid-456", result);
    }
}
