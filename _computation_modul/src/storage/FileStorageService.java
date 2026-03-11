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

        Path file = Paths.get(DIRECTORY + "/" + id + ".txt");

        if (!Files.exists(file)) {
            return null;
        }

        String content = Files.readString(file);

        String[] parts = content.split("\n");

        String processId = parts[0].substring(4);
        int number = Integer.parseInt(parts[1].substring(8));
        int threads = Integer.parseInt(parts[2].substring(14));
        String statusStr = parts[3].substring(8).trim();
        int lastComputed = Integer.parseInt(parts[4].substring(15).trim());
        String partialResultStr = parts[5].substring(16).trim();

        CalculationProcess process = new CalculationProcess(processId, number, threads);

        process.setStatus(ProcessStatus.valueOf(statusStr));
        process.setLastComputed(lastComputed);
        process.setPartialResult(new BigInteger(partialResultStr));

        return process;
    }
}
