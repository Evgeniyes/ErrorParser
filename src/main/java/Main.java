import java.util.ArrayList;
import java.io.InputStream;
import javax.swing.JOptionPane;

public class Main {
    static Signal[] signals;

    public static void main(String[] args) {

        // Проверка наличия ресурсов
        checkResources();

        //Показываем интерфейс
        FileSelectorGUI.showFileSelector();
        long startTime = System.currentTimeMillis();
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

//    public static void  OutputSignals(){
//        for(int i= 0; i< signals.length; i++){
//            System.out.println((signals[i].name + " = " + signals[i].GetCountErrors()));
//        }
//    }

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
// Собираем данные из signals и формируем массив для выгрузки в файл
        ArrayList<String[]> data = new ArrayList<>();

        for(int i = 0; i < signals.length; i++) {
            if(!signals[i].name.contains("Banner") && !signals[i].name.contains("Coord") && signals[i].GetCountErrors()>0){
                Signal signal = signals[i];
                String[] tmpParam = new String[6];
                tmpParam[0] = signal.name.split(":")[0];
                String[] tmpName = signal.name.split("\\|");
                tmpParam[1] = StringFormated(tmpParam[0], tmpName.length > 1 ? tmpName[1] : tmpName[0]);
                tmpParam[2] = StringFormated(tmpParam[0], SignalConverter.ConvertToKSK(removeLastPart(tmpParam[0])));
                tmpParam[3] = String.valueOf(signal.GetCountErrors());
                tmpParam[4] = (signal.minMilisecError / 10) + " сек.";
                tmpParam[5] = (signal.maxMilisecError / 10) + " сек.";
                data.add(tmpParam);
            }
        }

        String[] headers = {"Сигнал", "Название ошибки EFI", "Название ошибки КСК", "Кол-во", "Мин. время ошибки", "Макс. время ошибки"};

        // Создаем и открываем файл
        ExcelExporter.createAndOpenExcel(data, headers, "результат_обработки");
    }

    public static String removeLastPart(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        int lastIndex = input.lastIndexOf('_');
        if (lastIndex == -1) {
            return input; // если нет подчеркиваний, возвращаем исходную строку
        }

        return input.substring(7, lastIndex); //возвращаем обреданные первые символы с буд и последние с вагоном
    }


    public static String StringFormated(String sign, String label){
        String vagon = sign.substring(sign.lastIndexOf("_")+1);
        System.out.println("vagon=" + vagon);
        System.out.println("sign=" + sign);

        String bud = Character.toString(sign.charAt(sign.indexOf("BUD") + 3));

        int dotIndex = label.indexOf('.');
        String tmpLabe = dotIndex != -1 ? label.substring(dotIndex + 1) : label;
        return "Вагон " + vagon + " БУД" + bud + " " + tmpLabe;
    }

    private static void checkResources() {
        try {
            System.out.println("Checking resources...");
            var url = Main.class.getClassLoader().getResource("SignalList.csv");
            System.out.println("Resource URL: " + url);

            if (url == null) {
                System.err.println("ERROR: SignalList.csv not found in resources!");
                // Получим список всех ресурсов
                try {
                    var resources = Main.class.getClassLoader().getResources("");
                    while (resources.hasMoreElements()) {
                        System.out.println("Available resource: " + resources.nextElement());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("SignalList.csv found: " + url.getPath());
                try (InputStream stream = url.openStream()) {
                    System.out.println("File size: " + stream.available() + " bytes");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}