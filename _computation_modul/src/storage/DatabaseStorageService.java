package storage;

import java.io.IOException;
import java.math.BigInteger;
import javax.sql.DataSource;
import model.CalculationProcess;
import model.ProcessStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DatabaseStorageService implements StorageService {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseStorageService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveProcess(CalculationProcess process) throws IOException {
        
        String sql = ""
                + "MERGE INTO calculation_process AS t "
                + "USING (VALUES (?, ?, ?, ?, ?, ?)) AS v(id, number, thread_count, status, last_computed, partial_result) "
                + "ON t.id = v.id "
                + "WHEN MATCHED THEN UPDATE SET "
                + "  number = v.number, "
                + "  thread_count = v.thread_count, "
                + "  status = v.status, "
                + "  last_computed = v.last_computed, "
                + "  partial_result = v.partial_result "
                + "WHEN NOT MATCHED THEN INSERT (id, number, thread_count, status, last_computed, partial_result) "
                + "VALUES (v.id, v.number, v.thread_count, v.status, v.last_computed, v.partial_result);";

        jdbcTemplate.update(sql,process.getId(), process.getNumber(),process.getThreadCount(),process.getStatus().name(),process.getLastComputed(), process.getPartialResult().toString());
    }

    @Override
    public CalculationProcess loadProcess(String id) throws IOException {
        String sql = "SELECT id, number, thread_count, status, last_computed, partial_result "
                   + "FROM calculation_process WHERE id = ?";

        RowMapper<CalculationProcess> mapper = (rs, rowNum) -> {
            String pid = rs.getString("id");
            int number = rs.getInt("number");
            int threads = rs.getInt("thread_count");
            String status = rs.getString("status");
            int lastComputed = rs.getInt("last_computed");
            String partial = rs.getString("partial_result");

            CalculationProcess p = new CalculationProcess(pid, number, threads);
            p.setStatus(ProcessStatus.valueOf(status));
            p.setLastComputed(lastComputed);
            p.setPartialResult(new BigInteger(partial));
            return p;
        };

        return jdbcTemplate.query(sql, mapper, id).stream().findFirst().orElse(null);
    }
}

