/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import model.CalculationProcess;
import model.ProcessStatus;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class FileStorageService implements StorageService {
    
    private static final String DIRECTORY = "C:\\process_storage";

    public FileStorageService() throws IOException {

        Path path = Paths.get(DIRECTORY);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    @Override
    public void saveProcess(CalculationProcess process) throws IOException {

        Path file = Paths.get(DIRECTORY + "/" + process.getId() + ".txt");

        String content =
                "ID: " + process.getId() + "\n" +
                "Number: " + process.getNumber() + "\n" +
                "Thread count: " + process.getThreadCount() + "\n" +
                "Status: " + process.getStatus() + "\n" +
                "Last computed: " + process.getLastComputed() + "\n" +
                "Partial result: " + process.getPartialResult();

        Files.writeString(file, content);
    }
    
    @Override
    public CalculationProcess loadProcess(String id) throws IOException {

        try {
            Path file = Paths.get(DIRECTORY + "/" + id + ".json");
            if (!Files.exists(file)) {
                return null;
            }
            
            String content = Files.readString(file);
            JSONObject json = new JSONObject(content);
            
            String processId = json.getString("id");
            int number = json.getInt("number");
            int threads = json.getInt("threads");
            String statusStr = json.getString("status");
            int lastComputed = json.getInt("lastComputed");
            String partialResultStr = json.getString("partialResult");
            
            CalculationProcess process = new CalculationProcess(processId, number, threads);
            
            process.setStatus(ProcessStatus.valueOf(statusStr));
            process.setLastComputed(lastComputed);
            process.setPartialResult(new BigInteger(partialResultStr));
            
            return process;
        } catch (JSONException ex) {
            Logger.getLogger(FileStorageService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
