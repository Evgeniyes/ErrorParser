import java.util.Map;

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
}