import java.io.*;
import java.awt.Desktop;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ExcelExporter {

    public static void createAndOpenExcel(ArrayList<String[]> data, String[] headers, String fileName) {
        try {
            File tempFile = File.createTempFile(fileName, ".csv");
            tempFile.deleteOnExit();

            createCsvFile(data, headers, tempFile);
            openFile(tempFile);

        } catch (IOException e) {
            System.err.println("Ошибка при создании файла: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createCsvFile(ArrayList<String[]> data, String[] headers, File file) throws IOException {
        // Указываем кодировку UTF-8 с BOM для правильного отображения кириллицы
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            // Добавляем BOM (Byte Order Mark) для UTF-8
            writer.write("\uFEFF");

            // Записываем заголовки
            writer.write(String.join(";", headers));
            writer.newLine();

            // Записываем данные
            for (String[] row : data) {
                writer.write(String.join(";", row));
                writer.newLine();
            }
        }
    }

    private static void openFile(File file) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(file);
                } else {
                    // Альтернативный способ
                    openFileAlternative(file);
                }
            } else {
                openFileAlternative(file);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при открытии файла: " + e.getMessage());
        }
    }

    private static void openFileAlternative(File file) {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Windows - открываем в Notepad++ или Блокноте
                Runtime.getRuntime().exec("notepad \"" + file.getAbsolutePath() + "\"");
            } else if (os.contains("mac")) {
                // Mac OS
                Runtime.getRuntime().exec("open -t \"" + file.getAbsolutePath() + "\"");
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux
                Runtime.getRuntime().exec("xdg-open \"" + file.getAbsolutePath() + "\"");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при открытии файла альтернативным способом: " + e.getMessage());
        }
    }
}