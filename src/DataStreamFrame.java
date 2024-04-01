import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamFrame extends JFrame
{
    JButton quitButton;
    JButton chooserButton;
    JButton searchButton;

    JTextArea realFile;
    JTextArea filteredFile;

    JPanel mainPanel;
    JPanel titlePanel;
    JPanel displayPanel;
    JPanel buttonPanel;
    JPanel stringPanel;
    JPanel filePanel;
    JPanel filterPanel;
    JPanel otherPanel;
    JLabel titleLabel;
    JLabel stringLabel;
    JLabel fileLabel;


    JTextField stringTextField;
    JTextField fileTextField;
    JScrollPane scroller;
    JFileChooser chooser = new JFileChooser();

    File fileChoice;
    String fileName;

    List<String> fileInfo;

    public DataStreamFrame()
    {
        setTitle("DATA STREAM");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        add(mainPanel);

        otherPanel = new JPanel();
        otherPanel.setLayout(new GridLayout(2, 1));

        displayPanel = new JPanel();
        displayPanel.setLayout(new GridLayout(1, 2));

        mainPanel.add(otherPanel, BorderLayout.NORTH);
        mainPanel.add(displayPanel, BorderLayout.CENTER);


        createDisplayPanel();
        createFilterPanel();
        createTitlePanel();
        createStringChoicePanel();
        createButtonPanel();
        setVisible(true);
    }

    private void createDisplayPanel()
    {
        filePanel = new JPanel();
        realFile = new JTextArea(25, 35);
        realFile.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        realFile.setEditable(false);
        scroller = new JScrollPane(realFile);
        filePanel.add(scroller);
        displayPanel.add(filePanel, new GridLayout(1, 1));
    }


    private void createTitlePanel()
    {
        titlePanel = new JPanel();
        titleLabel = new JLabel("DATA STREAM", JLabel.CENTER);
        titleLabel.setVerticalTextPosition(JLabel.BOTTOM);
        titleLabel.setHorizontalTextPosition(JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        titlePanel.add(titleLabel);
        otherPanel.add(titlePanel, new GridLayout(1, 1));
    }

    private void createFilterPanel()
    {
        filterPanel = new JPanel();
        filteredFile = new JTextArea(25, 35);
        filteredFile.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        filteredFile.setEditable(false);
        scroller = new JScrollPane(filteredFile);
        filterPanel.add(scroller);
        displayPanel.add(filterPanel, new GridLayout(1, 2));
    }


    private void createStringChoicePanel()
    {
        stringPanel = new JPanel();
        fileLabel = new JLabel("FILE: ");
        fileTextField = new JTextField("                        ");
        fileLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));


        fileTextField.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        fileTextField.setEditable(false);


        stringLabel = new JLabel("SEARCHING FOR: ");
        stringTextField = new JTextField("                        ");
        stringLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));


        stringTextField.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        stringTextField.setEditable(false);


        stringPanel.add(fileLabel);
        stringPanel.add(fileTextField);
        stringPanel.add(stringLabel);
        stringPanel.add(stringTextField);
        otherPanel.add(stringPanel, new GridLayout(2, 1));
    }

    private void createButtonPanel()
    {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        searchButton = new JButton("ChOOSE A FILE:");
        searchButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        searchButton.addActionListener(new ActionListener()

        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fileRead();
            }
        });

        chooserButton = new JButton("SEARCH");
        chooserButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        chooserButton.addActionListener(new ActionListener()

        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fileFilter();
            }
        });

        quitButton = new JButton("QUIT");
        quitButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        quitButton.addActionListener(new ActionListener()
        {
            JOptionPane pane = new JOptionPane();
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int result = JOptionPane.showConfirmDialog(pane, "Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
                else
                {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
        });

        buttonPanel.add(searchButton);
        buttonPanel.add(chooserButton);
        buttonPanel.add(quitButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }


    private void fileRead()
    {
        Path target = new File(System.getProperty("user.dir")).toPath();
        target = target.resolve("src");
        chooser.setCurrentDirectory((target.toFile()));
        try
        {
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                target = chooser.getSelectedFile().toPath();

                Scanner inFile = new Scanner(target);

                while (inFile.hasNextLine())
                {
                    fileChoice = chooser.getSelectedFile();
                    Path file = fileChoice.toPath();
                    fileName = fileChoice.getName();
                    fileTextField.setText(fileName);
                    fileInfo = new ArrayList<>();

                    try
                            (Stream<String> stream = Files.lines(Paths.get(String.valueOf(file))))
                    {
                        fileInfo = stream
                                .map(String::toUpperCase)
                                .collect(Collectors.toList());
                    }
                    for
                    (Object line : fileInfo)
                    {
                        realFile.append(line +"\n");
                    }
                    realFile.append("\n\n");
                    break;
                }
                inFile.close();
            }
            else
            {
                realFile.setText("Try again, Please choose a file");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void fileFilter()
    {
        String words = JOptionPane.showInputDialog("Enter the word you want to search: ");
        stringTextField.setText(words);
        List<String> results = fileInfo.stream().filter(str -> str.toLowerCase().replaceAll("[^A-Za-z]", " ").contains(words)).collect(Collectors.toList());
        for
        (String lines : results)
        {
            filteredFile.append(lines + "\n");
        }
        filteredFile.append("\n\n");
    }
}