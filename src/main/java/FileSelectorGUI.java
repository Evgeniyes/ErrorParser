import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

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
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);

        // Создаем основную панель
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Создаем панель для перетаскивания
        JPanel dropPanel = new JPanel();
        dropPanel.setLayout(new BorderLayout());
        dropPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(30, 20, 30, 20) // Увеличили отступы
        ));
        dropPanel.setBackground(new Color(240, 240, 240));
        dropPanel.setPreferredSize(new Dimension(400, 180));

        // Текст инструкции - УПРОЩЕННАЯ ВЕРСИЯ
        JLabel instructionLabel = new JLabel(
                "<html><center>Перетащите CSV файл сюда<br>" +
                        "<small>или нажмите для выбора файла</small></center></html>",
                SwingConstants.CENTER
        );
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Уменьшили шрифт

        // Альтернатива: простой текст без HTML
        // JLabel instructionLabel = new JLabel("Перетащите CSV файл сюда", SwingConstants.CENTER);
        // JLabel subLabel = new JLabel("или нажмите для выбора файла", SwingConstants.CENTER);
        // subLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        // subLabel.setForeground(Color.GRAY);

        // Индикатор прогресса
        JProgressBar progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);

        // Метка статуса
        JLabel statusLabel = new JLabel("Ожидание файла...", SwingConstants.CENTER);
        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        // Настраиваем drag-and-drop
        setupDropTarget(dropPanel, frame, progressBar, statusLabel);

        // Обработчик клика по панели
        dropPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectFileViaDialog(frame, progressBar, statusLabel);
            }
        });

        // Добавляем текст на панель
        dropPanel.add(instructionLabel, BorderLayout.CENTER);

        // Добавляем компоненты на основную панель
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(dropPanel, BorderLayout.CENTER);
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void setupDropTarget(JPanel dropPanel, JFrame frame, JProgressBar progressBar, JLabel statusLabel) {
        dropPanel.setDropTarget(new DropTarget(dropPanel, DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> files = (List<File>) dtde.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);

                    if (!files.isEmpty()) {
                        File file = files.get(0);
                        if (file.getName().toLowerCase().endsWith(".csv")) {
                            processFile(file.getAbsolutePath(), progressBar, statusLabel);
                        } else {
                            statusLabel.setText("Ошибка: выберите CSV файл");
                            statusLabel.setForeground(Color.RED);
                        }
                    }
                } catch (Exception e) {
                    statusLabel.setText("Ошибка при обработке файла");
                    statusLabel.setForeground(Color.RED);
                    e.printStackTrace();
                }
            }
        }));
    }

    private static void selectFileViaDialog(JFrame frame, JProgressBar progressBar, JLabel statusLabel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите CSV файл");

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
            processFile(selectedFile.getAbsolutePath(), progressBar, statusLabel);
        }
    }

    private static void processFile(String filePath, JProgressBar progressBar, JLabel statusLabel) {
        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                    statusLabel.setText("Обработка файла...");
                    statusLabel.setForeground(Color.BLUE);
                });

                CsvProcessorApache.processCsv(filePath);

                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(false);
                    statusLabel.setText("Обработка завершена успешно!");
                    statusLabel.setForeground(new Color(0, 128, 0));

                    Main.FileFinish();

                    JOptionPane.showMessageDialog(null,
                            "Файл успешно обработан: " + new File(filePath).getName(),
                            "Готово", JOptionPane.INFORMATION_MESSAGE);
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(false);
                    statusLabel.setText("Ошибка: " + e.getMessage());
                    statusLabel.setForeground(Color.RED);

                    JOptionPane.showMessageDialog(null,
                            "Ошибка при обработке файла: " + e.getMessage(),
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileSelectorGUI::showFileSelector);
    }
}