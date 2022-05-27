import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class WorkWindow extends JFrame {
    private static JTextArea textAreaDecoded;
    private static JScrollPane scrollPaneDecoded;
    private static JTextArea textAreaEncoded;
    private static JScrollPane scrollPaneEncoded;
    private static JButton buttonOpen;
    private static JButton buttonEncoded;
    private static JButton buttonDecoded;
    private static JButton buttonParameters;
    private static JButton buttonSaveFile;
    private static JButton buttonBruteForce;
    private static JFileChooser filechoose;
    private static StringBuilder result;
    private static int key;
    private static JLabel shift;
    private static JButton buttonOk;
    private static ArrayList<Character> alphabet;


    public WorkWindow() {
        initAlphabet();

        //Создание объектов с параметрами
        setSize(640, 520);
        setResizable(false);
        setTitle("Криптоанализатор");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.lightGray);
        setLayout(null);

        scrollPaneDecoded = new JScrollPane();
        scrollPaneDecoded.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPaneEncoded = new JScrollPane();
        scrollPaneEncoded.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        Font font = new Font("Serif", Font.ITALIC, 18);

        textAreaDecoded = new JTextArea();
        textAreaDecoded.setBounds(10, 10, 600, 170);
        textAreaDecoded.setLineWrap(true);
        textAreaDecoded.setWrapStyleWord(true);
        textAreaDecoded.setBorder(new LineBorder(Color.BLACK));
        textAreaDecoded.setFont(font);
        textAreaDecoded.repaint();

        textAreaEncoded = new JTextArea();
        textAreaEncoded.setBounds(10, 190, 600, 170);
        textAreaEncoded.setLineWrap(true);
        textAreaEncoded.setWrapStyleWord(true);
        textAreaEncoded.setBorder(new LineBorder(Color.BLACK));
        textAreaEncoded.setFont(font);
        textAreaEncoded.repaint();

        scrollPaneDecoded.setBounds(10, 10, 600, 170);
        scrollPaneDecoded.getViewport().setBackground(Color.WHITE);
        scrollPaneDecoded.getViewport().add(textAreaDecoded, textAreaEncoded);

        scrollPaneEncoded.setBounds(10, 190, 600, 170);
        scrollPaneEncoded.getViewport().setBackground(Color.WHITE);
        scrollPaneEncoded.getViewport().add(textAreaEncoded);

        buttonOpen = new JButton("Загрузить файл");
        buttonOpen.setBounds(10, 370, 160, 30);

        buttonEncoded = new JButton("Зашифровать текст");
        buttonEncoded.setBounds(230, 370, 160, 30);

        buttonDecoded = new JButton("Расшифровать текст");
        buttonDecoded.setBounds(449, 370, 160, 30);

        buttonParameters = new JButton("Указать параметры");
        buttonParameters.setBounds(230, 420, 160, 30);

        buttonSaveFile = new JButton("Сохранить файл");
        buttonSaveFile.setBounds(10, 420, 160, 30);

        buttonBruteForce = new JButton("Brute Force");
        buttonBruteForce.setBounds(449, 420, 160, 30);

        //Привязка объектов к главному окну
        add(buttonOpen);
        add(buttonEncoded);
        add(buttonDecoded);
        add(buttonParameters);
        add(buttonSaveFile);
        add(buttonBruteForce);
        add(scrollPaneDecoded);
        add(scrollPaneEncoded);

        //Кнопка открытия файла
        buttonOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Окно открытия файла

                filechoose = new JFileChooser();
                int open = filechoose.showDialog(null, "Open File");
                if (open == JFileChooser.APPROVE_OPTION) {
                    File file = filechoose.getSelectedFile();
                    add(filechoose);
                    filechoose.setVisible(false);

                    //Чтение файла

                    String str = "";
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));

                        int c;

                        while ((c = br.read()) != -1) {
                            str += (char) c;
                        }

                        br.close();
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                    textAreaDecoded.setText(str);
                }
            }
        });

        //Кнопка указания параметров

        buttonParameters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Создание второго окна
                JFrame jFrame = new JFrame();
                jFrame.setSize(320, 100);
                jFrame.setResizable(false);
                jFrame.setTitle("Параметры шифра");
                jFrame.setLocationRelativeTo(null);
                jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                jFrame.getContentPane().setBackground(Color.lightGray);
                jFrame.setVisible(true);

                //Назначение привязки к сетке
                Container container = jFrame.getContentPane();
                container.setLayout(new GridLayout(2, 2, 5, 10));

                shift = new JLabel("Введите ключ шифра:");
                JTextField shift_field = new JTextField("", 1);

                buttonOk = new JButton("OK");
                buttonOk.setBorderPainted(true);

                buttonOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        key = Integer.parseInt(shift_field.getText());
                        jFrame.setVisible(false);
                    }
                });
                container.add(shift);
                container.add(shift_field);
                container.add(buttonOk);
            }
        });

        // Обработка кнопок шифрования и дешифрования
        // Зашифровать
        buttonEncoded.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                WorkWindow.encode();

            }
        });

        // Расшифровать
        buttonDecoded.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                WorkWindow.decode();

            }
        });

        // Кнопка Brute Force
        buttonBruteForce.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                WorkWindow.bruteForce();
            }
        });

        // Кнопка сохранения файла + фильтр файлов
        buttonSaveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                filechoose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                filechoose.setAcceptAllFileFilterUsed(false);
                filechoose.addChoosableFileFilter(filter);

                int returnValue = filechoose.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    System.out.println(filechoose.getSelectedFile().getPath());
                }
                try {
                    textAreaEncoded.write(new BufferedWriter(new FileWriter(filechoose.getSelectedFile()
                            + ".txt")));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        repaint();
        setVisible(true);
    }

    // Алгоритм Brute Force
    private static void bruteForce() {

        String str = "";
        for (int i = -alphabet.size(); i < alphabet.size(); i++) {
            encodeDecodeMethod(textAreaEncoded.getText(), i);
            String variant = result.toString();

            if (isGoodVariant(variant)) {
                str = variant;
            }
        }
        if (str.isEmpty()) {
            textAreaDecoded.setText("Не получилось расшифровать методом Brute force");
        } else {
            textAreaDecoded.setText(str);
        }

    }

    // Условия выполнения Brute force
    private static boolean isGoodVariant(String variant) {

        int wordLength = 0;

        for (int i = 0; i < variant.length(); i++) {
            char ch = variant.charAt(i);

            if (Character.isLetter(ch)) {
                wordLength++;
            } else {
                wordLength = 0;
            }

            // слово должно быть не больше 30 символов
            if (wordLength > 20) {
                if (variant.charAt(i) != ',' && variant.charAt(i + 1) != ' ')
                    return false;

            }

            if (ch == ' ') {
                // не может быть два пробела подряд
                if (variant.charAt(i + 1) == ' ') {
                    return false;
                }

            }

            if (ch == '.' || ch == ',' || ch == ':' || ch == '?' || ch == '!') {
                // не может быть первым символом в строке
                if (i == 0) {
                    return false;
                }
                // предыдущий символ должен быть буквой
                if (!Character.isLetter(variant.charAt(i - 1))) {
                    return false;
                }

                // следующий символ должен быть пробелом если это не конец строки
                if (i != variant.length() - 1 && variant.charAt(i + 1) != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void initAlphabet() {

        //Создание списка алфавита
        alphabet = new ArrayList<>();
        for (char ch = 'А'; ch <= 'Я'; ch++) {
            alphabet.add(ch);
        }

        for (char ch = 'а'; ch <= 'я'; ch++) {
            alphabet.add(ch);
        }

        //Добавление символов, не входящих в алфавит
        alphabet.add(' ');
        alphabet.add('!');
        alphabet.add('"');
        alphabet.add(',');
        alphabet.add('-');
        alphabet.add('.');
        alphabet.add(':');
        alphabet.add('?');
        alphabet.add('Ё');
        alphabet.add('ё');

    }

    private static void decode() {

        encodeDecodeMethod(textAreaEncoded.getText(), -WorkWindow.key);

        textAreaDecoded.setText(result.toString());
    }

    private static void encode() {
        encodeDecodeMethod(textAreaDecoded.getText(), WorkWindow.key);

        textAreaEncoded.setText(result.toString());
    }

    // Алгоритм шифрования
    private static void encodeDecodeMethod(String str, int key) {
        result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {

            char ch = str.charAt(i);
            int oldIndex = alphabet.indexOf(ch);

            int newIndex = (oldIndex + key) % alphabet.size();

            if (newIndex < 0) {
                newIndex = alphabet.size() + newIndex;
            }
            char newCh = alphabet.get(newIndex);

            result.append(newCh);
        }
    }
}