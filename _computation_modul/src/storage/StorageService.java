package storage;

import java.io.IOException;
import model.CalculationProcess;

public interface StorageService {

    void saveProcess(CalculationProcess process) throws IOException;

    CalculationProcess loadProcess(String id) throws IOException;
}

