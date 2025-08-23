import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import static java.lang.Long.toHexString;

public class CsvProcessorApache {

    public static void processCsv(String filePath) {
        try (Reader reader = new InputStreamReader(
                new FileInputStream(filePath),
                Charset.forName("windows-1251")); // ← Кодировка указана здесь
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withDelimiter('@') // использовать очень редкий разделитель
                     .withFirstRecordAsHeader() // если есть заголовки
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            for (CSVRecord record : csvParser) {
                // Доступ к данным по индексу
                //String col1 = record.get(0);
                //String col2 = record.get(1);

                if(record.getRecordNumber() == 8){
                    //System.out.println(record.get(0));
                    Main.CreateArraySignals(record.get(0));
                } else if(record.getRecordNumber()>8){
                    Main.FillingSignals(record.get(0));
                    //System.out.println(record.get(0).split(";").length);
                }
                //processRecord(record);

            }
            Main.CloseSignals();
            //Main.OutputSignals();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processRecord(CSVRecord record) {
        // Логика обработки записи
        System.out.println(record);
    }
}