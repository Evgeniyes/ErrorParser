import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileSelectorGUI {

    public static void showFileSelector() {
        // Устанавливаем системный внешний вид
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Создаем основное окно
        JFrame frame = new JFrame("Загрузчик CSV файлов");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 150);
        frame.setLocationRelativeTo(null); // центрируем окно

        // Создаем основную панель
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Текстовое поле для пути к файлу
        JTextField filePathField = new JTextField();
        filePathField.setEditable(false);

        // Кнопка выбора файла
        JButton selectButton = new JButton("Выбрать CSV файл");
        selectButton.setPreferredSize(new Dimension(150, 30));

        // Кнопка запуска обработки
        JButton processButton = new JButton("Запустить обработку");
        processButton.setPreferredSize(new Dimension(150, 30));
        processButton.setEnabled(false);

        // Обработчик кнопки выбора файла
        selectButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Выберите CSV файл");

            // Фильтр для CSV файлов
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
                }

                @Override
                public String getDescription() {
                    return "CSV файлы (*.csv)";
                }
            });

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
                processButton.setEnabled(true);
            }
        });

        // Обработчик кнопки запуска обработки
        processButton.addActionListener(e -> {
            String filePath = filePathField.getText();
            if (!filePath.isEmpty()) {
                //processSelectedFile(filePath);
                CsvProcessorApache.processCsv(filePath);
            }
        });

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(selectButton);
        buttonPanel.add(processButton);

        // Добавляем компоненты
        mainPanel.add(new JLabel("Выбранный файл:"), BorderLayout.NORTH);
        mainPanel.add(filePathField, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}