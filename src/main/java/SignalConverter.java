import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class SignalConverter {
    // Создание словаря
    static Map<String, String[]> signalsMap;

    public static Map<String, String[]> loadSignalsFromResources() {
        Map<String, String[]> tmpMap = new HashMap<>();
        try (Reader reader = new InputStreamReader(
                SignalConverter.class.getClassLoader().getResourceAsStream("SignalList.csv"));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT .withDelimiter('@'))) {

            for (CSVRecord record : csvParser) {
                //System.out.println(record.get(0));

                String key = record.get(0).split(";")[0].trim();
                String[] values = {record.get(0).split(";")[1], record.get(0).split(";")[2]};
                tmpMap.put(key, values);

            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки CSV файла", e);
        }

        return tmpMap;
    }

    public static String ConvertToKSK(String s){

    if(signalsMap == null){
        signalsMap = loadSignalsFromResources();
        System.out.println(signalsMap.keySet());
    }
        /// //////////////////////
//        System.out.println("Сигналов в словаре для конвертации = " + signalsMap.size());
//        System.out.println(signalsMap.keySet());

        if(signalsMap.containsKey(s)){
            return signalsMap.get(s)[1];
        } else {
            System.out.println("Сигналкоторого нет =" + s);
            return "НЕТ ТАКОГО СИГНАЛА В ТАБЛИЦЕ";
        }

    }
}
