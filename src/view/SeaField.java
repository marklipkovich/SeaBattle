package view;

import static javax.swing.JOptionPane.showMessageDialog;

import io.StoreObject;
import presenter.SeaBattlePresenter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class SeaField {
    private Color colorStart = Color.white;
    private Color colorMiss = Color.lightGray;
    private Color colorHit = Color.yellow;
    private Color colorKill = Color.red;
    private SeaBattlePresenter presenter;
    private byte boardSize;
    private JFrame frame;
    //private final JPanel panel = new JPanel(new BorderLayout(3, 3));
    private JPanel panel = new JPanel();
    private JButton[][] buttonGrid;
    private JLabel numberShipsLabel = new JLabel("Number of Ships");
    private JLabel maxShipSizeLabel = new JLabel("Max Ship Size");
    private JTextField outText = new JTextField("START");
    private JComboBox<Byte> maxShipSizeCombo;
    private JComboBox<Byte> numberShipsCombo;

    public void initializeGui() {
        presenter.onInitGui();
        JPanel board;
        byte buttonImageSize = 64;
        Font font = new Font("Sans", Font.BOLD, 18);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        board = new JPanel(new GridLayout(0, boardSize));
        JPanel control = new JPanel(new GridLayout(5, 0));
        panel.add(board);
        panel.add(control);
        numberShipsCombo.addActionListener(a -> presenter.selectNumberShips((Byte) numberShipsCombo.getSelectedItem()));
        maxShipSizeCombo.addActionListener(a -> presenter.selectMaxShipSize((Byte) maxShipSizeCombo.getSelectedItem()));
        outText.setFont(font);
        outText.setEditable(false);
        numberShipsLabel.setFont(font);
        maxShipSizeLabel.setFont(font);
        numberShipsCombo.setFont(font);
        maxShipSizeCombo.setFont(font);
        control.add(numberShipsLabel);
        control.add(numberShipsCombo);
        control.add(maxShipSizeLabel);
        control.add(maxShipSizeCombo);
        control.add(outText);
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        buttonGrid = new JButton[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JButton button = new JButton();
                button.addActionListener(e -> presenter.shot(e.getActionCommand()));
                button.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(new BufferedImage(buttonImageSize, buttonImageSize, BufferedImage.TYPE_INT_ARGB));
                button.setIcon(icon);
                button.setBackground(Color.white);
                button.setActionCommand(String.valueOf(getCellNumber(i, j)));
                buttonGrid[i][j] = button;
                board.add(button);
            }
        }
        frame = new JFrame("Sea Battle");
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        fileMenu.setFont(font);
        newMenuItem.setFont(font);
        loadMenuItem.setFont(font);
        saveMenuItem.setFont(font);
        newMenuItem.addActionListener(e -> presenter.newGame());
        saveMenuItem.addActionListener(new SaveMenuListener());
        loadMenuItem.addActionListener(new LoadMenuListener());
        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setLocationByPlatform(true);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);
    }

    public class SaveMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(frame);
            StoreObject storeObj = new StoreObject(buttonGrid);
            presenter.saveFile(fileSave.getSelectedFile(), storeObj);
        }
    }

    public class LoadMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            JFileChooser fileLoad = new JFileChooser();
            fileLoad.showSaveDialog(frame);
            StoreObject loadObj = presenter.loadFile(fileLoad.getSelectedFile());
            JButton[][] buttonGridLoad = loadObj.getButtonGrid();
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    buttonGrid[i][j].setEnabled(buttonGridLoad[i][j].isEnabled());
                    buttonGrid[i][j].setBackground(buttonGridLoad[i][j].getBackground());
                }
            }
        }
    }

    private int getCellNumber(int cellRow, int cellCol) {
        return (cellCol + 1 + 10 * (cellRow + 1));
    }

    public void setPresenter(SeaBattlePresenter pres) {
        presenter = pres;
    }

    public void setInitGui(Byte board, Byte[] maxShipsArray, Byte maxShipSize, Byte[] numShipsArray, Byte numberShips) {
        boardSize = board;
        maxShipSizeCombo = new JComboBox<>(maxShipsArray);
        maxShipSizeCombo.setSelectedItem(maxShipSize);
        numberShipsCombo = new JComboBox<>(numShipsArray);
        numberShipsCombo.setSelectedItem(numberShips);
    }

    public void newGame() {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                buttonGrid[x][y].setEnabled(true);
                buttonGrid[x][y].setBackground(colorStart);
            }
        }
        outText.setText("START");
    }

    public void gameOver() {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                buttonGrid[x][y].setEnabled(false);
            }
        }
        outText.setText("GAME OVER!");
    }

    public void shipKilled(List<Integer> area, int shipNumber) {
        for (int cell : area) {
            buttonGrid[cell / 10 - 1][cell % 10 - 1].setBackground(colorKill);
        }
        outText.setText("SHIP " + shipNumber + " KILLED");
    }

    public void hit(int cell) {
        buttonGrid[cell / 10 - 1][cell % 10 - 1].setBackground(colorHit);
        outText.setText("HIT");
    }

    public void miss(int cell) {
        buttonGrid[cell / 10 - 1][cell % 10 - 1].setEnabled(false);
        buttonGrid[cell / 10 - 1][cell % 10 - 1].setBackground(colorMiss);
        outText.setText("MISS");
    }

    public void loadGame() {
        outText.setText("START");
    }

    public void saveError(){
        showMessageDialog(frame, "OШИБКА СОХРАНЕНИЯ!");
    }

    public void loadError() {
        showMessageDialog(frame, "OШИБКА ЗАГРУЗКИ ФАЙЛА!");
    }
}
