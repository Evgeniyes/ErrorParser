import java.util.ArrayList;

public class Main {
    static Signal[] signals;

    public static void main(String[] args) {

        //String filePath = "C:/outputBUD.csv";
        FileSelectorGUI.showFileSelector();
        long startTime = System.currentTimeMillis();


        //CsvProcessorApache.processCsv(filePath); //Читаем файл и заполняем сигналы
        //CloseSignals();
        //OutputSignals(); //Выводим собранную информацию

        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения: " + (endTime - startTime) + " мс");
    }

    public static void CreateArraySignals(String strSignals){
        String[] tmpSignals = strSignals.split(";");
        signals = new Signal[tmpSignals.length];

        for(int i =0; i< tmpSignals.length; i++){
            signals[i] = new Signal(tmpSignals[i]);
        }

        System.out.println("Создали сигналы, всего " + signals.length);
    }

    public static void FillingSignals(String strValues){
        String[] tmpValues = strValues.split(";");
        if(tmpValues.length == signals.length){
            //Логика записи параметров в сами сигналы
            for(int j = 0; j<tmpValues.length; j++){
                if(isInteger(tmpValues[j])){
                    signals[j].AddValue(Integer.parseInt(tmpValues[j]));
                    //System.out.println((signals[j].name + " add = " + Integer.parseInt(tmpValues[j])));
                }
            }

        } else {
            //System.out.println("Не совпадает кол-во столбцов и кол-во сигналов");
            //System.out.println("Сигналов = " + signals.length + " в строке параметров = " + strValues.split(";").length + "Строка = " + strValues);

        }
    }

    public static void  OutputSignals(){
        for(int i= 0; i< signals.length; i++){
            System.out.println((signals[i].name + " = " + signals[i].GetCountErrors()));
        }
    }

    public static void  CloseSignals(){
        for(int i= 0; i< signals.length; i++){
            signals[i].AddValue(0);
        }
    }

    //Проверяем подходит ли нам значение
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void FileFinish(){
// Собираем данные из signals
        ArrayList<String[]> data = new ArrayList<>();

        for(int i = 0; i < signals.length; i++) {
            if(!signals[i].name.contains("Banner") && !signals[i].name.contains("Coord") && signals[i].GetCountErrors()>0){
                Signal signal = signals[i];
                String[] tmpParam = new String[5];
                tmpParam[0] = signal.name.split(":")[0];
                String[] tmpName = signal.name.split("\\|");
                tmpParam[1] = tmpName.length > 1 ? tmpName[1] : tmpName[0];
                tmpParam[2] = String.valueOf(signal.GetCountErrors());
                tmpParam[3] = String.valueOf(signal.minMilisecError);
                tmpParam[4] = String.valueOf(signal.maxMilisecError);
                data.add(tmpParam);
//                data[i][0] = signal.name.split(":")[0];
//                //System.out.println("Разбили строку на " +signal.name.split("\\|").length);
//                String[] tmpName = signal.name.split("\\|");
//                data[i][1] = tmpName.length > 1 ? tmpName[1] : tmpName[0];
//
//                data[i][2] = String.valueOf(signal.GetCountErrors());
//                data[i][3] = String.valueOf(signal.minMilisecError); // замени на реальные данные
//                data[i][4] = String.valueOf(signal.maxMilisecError);
            }
        }

        String[] headers = {"Сигнал", "Название ошибки", "Кол-во", "Мин. время ошибки", "Макс. время ошибки"};

        // Создаем и открываем файл
        ExcelExporter.createAndOpenExcel(data, headers, "результат_обработки");
    }
}