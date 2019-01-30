package rashjz.info.app.sw.domain.validator;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;

@Component
public class IsValidCSV {

    public boolean isNotValid(MultipartFile file) throws Exception {

        CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream())) ;
        return csvReader.readAll()
                .stream()
                .anyMatch(line ->
                         Arrays.stream(line)
                        .anyMatch(val -> val.startsWith("@"))
                        == Boolean.TRUE
                );
    }
}
