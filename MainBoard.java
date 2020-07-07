import javax.swing.Icon;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class MainBoard extends JFrame implements ActionListener
{
    public static final long serialVersionUID = 1L;
    final int HEIGHT = 6;
    final int WIDTH = 7;
    private boolean flag;
    public BoardPanel pnlBoard;
    public Board gameboard;
    public JButton[] btnSelect;
    public JPanel arrbtn;
    public static boolean checkWin;
    public static boolean gameOver;
    public static int turn;
    public int player;
    public int move;
    public static int gamemode;
    public static int markPlayer;
    public AIEasy easyAI;
    public BoardMed medBoard;
    public AIMedium medAI;
    public AIHard hardAI;
    JMenuBar menuBar;
    JMenu menu;
    JMenu subMenu;
    JMenuItem menuItem;
    JMenuItem subMenuItem;
    
    static {
        MainBoard.checkWin = false;
        MainBoard.gameOver = false;
        MainBoard.turn = 0;
        MainBoard.gamemode = 1;
        MainBoard.markPlayer = 3;
    }
    
    public MainBoard() {
        this.btnSelect = new JButton[7];
        this.arrbtn = new JPanel();
        this.easyAI = new AIEasy(1);
        this.medBoard = new BoardMed();
        this.medAI = new AIMedium(this.medBoard);
        this.hardAI = new AIHard();
        this.setSize(600, 710);
        this.setTitle("Connect Four");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(2);
        this.arrbtn.setLayout(new GridLayout());
        for (int i = 0; i < 7; ++i) {
            (this.btnSelect[i] = new JButton("COL " + (i + 1))).addActionListener(this);
            this.btnSelect[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.arrbtn.add(this.btnSelect[i]);
        }
        this.add(this.arrbtn, "North");
        this.menuBar = new JMenuBar();
        this.menu = new JMenu("Menu");
        this.menuBar.add(this.menu);
        (this.menuItem = new JMenuItem("PvP")).addActionListener(this);
        this.menu.add(this.menuItem);
        (this.menuItem = new JMenuItem("Easy AI")).addActionListener(this);
        this.menu.add(this.menuItem);
        (this.menuItem = new JMenuItem("Med AI")).addActionListener(this);
        this.menu.add(this.menuItem);
        (this.menuItem = new JMenuItem("Hard AI")).addActionListener(this);
        this.menu.add(this.menuItem);
        (this.menuItem = new JMenuItem("Help")).addActionListener(this);
        this.menu.add(this.menuItem);
        (this.menuItem = new JMenuItem("About")).addActionListener(this);
        this.menu.add(this.menuItem);
        this.setJMenuBar(this.menuBar);
        this.add(this.pnlBoard = new BoardPanel(), "Center");
        this.gameboard = new Board();
        this.setVisible(true);
        this.repaint();
        this.revalidate();
    }
    
    public void click(final int col) {
        int gameState = 0;
        if (!MainBoard.gameOver) {
            if (this.gameboard.getLevel(col - 1) < 6) {
                this.pnlBoard.update_col(col, this.gameboard.drop(col - 1), this.gameboard.returnBoard(), this.gameboard.cntMoves());
                final Thread thread = new Thread(this.pnlBoard.updatethread);
                thread.start();
            }
            MainBoard.checkWin = this.gameboard.check(1 + (this.gameboard.cntMoves() - 1) % 2);
            if (MainBoard.checkWin) {
                MainBoard.gameOver = true;
                gameState = 1 + (this.gameboard.cntMoves() - 1) % 2;
            }
            else if (this.gameboard.cntMoves() >= 42) {
                gameState = 3;
                MainBoard.gameOver = true;
            }
            this.pnlBoard.setWinner(gameState, MainBoard.markPlayer);
        }
    }
    
    public void reset() {
        MainBoard.gameOver = false;
        this.gameboard.boardinit();
        this.pnlBoard.setMoves(0);
        this.pnlBoard.setWinner(0, 0);
        this.repaint();
        this.revalidate();
    }
    
    public void medMove(final int col) {
        this.medBoard.drop(col - 1, 2);
        final int temp = this.medAI.makeMove();
        this.click(temp + 1);
        this.medBoard.drop(temp, 1);
    }
    
    public void hardMove(final int col) {
        final AIHard hardAI = this.hardAI;
        hardAI.pos = String.valueOf(hardAI.pos) + (char)(col + 48);
        final int[] height = this.hardAI.height;
        final int n = col - 1;
        ++height[n];
        if (this.flag) {
            this.hardAI.pos = this.hardAI.pos.substring(4, this.hardAI.pos.length());
            this.flag = false;
        }
        final int temp = this.hardAI.makeMove();
        this.click(temp + 1);
        final AIHard hardAI2 = this.hardAI;
        hardAI2.pos = String.valueOf(hardAI2.pos) + (char)(temp + 1 + 48);
        final int[] height2 = this.hardAI.height;
        final int n2 = temp;
        ++height2[n2];
    }
    
    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        final String command = actionEvent.getActionCommand();
        if (command.equals("PvP")) {
            this.reset();
            MainBoard.markPlayer = 3;
            MainBoard.gamemode = 1;
        }
        else if (command.equals("Easy AI")) {
            this.reset();
            MainBoard.gamemode = 2;
            final Object[] options = { "AI", "Player" };
            final int n = JOptionPane.showOptionDialog(null, "Who plays first?", "Turn decision", 0, 3, null, options, options[0]);
            if (n == 0) {
                MainBoard.markPlayer = 1;
                this.click(4);
            }
            else {
                MainBoard.markPlayer = 0;
            }
        }
        else if (command.equals("Med AI")) {
            this.reset();
            MainBoard.gamemode = 3;
            this.medBoard = new BoardMed();
            this.medAI = new AIMedium(this.medBoard);
            final Object[] options = { "AI", "Player" };
            final int n = JOptionPane.showOptionDialog(null, "Who plays first?", "Turn decision", 0, 3, null, options, options[0]);
            if (n == 0) {
                MainBoard.markPlayer = 1;
                this.click(4);
                this.medBoard.drop(3, 1);
            }
            else {
                MainBoard.markPlayer = 0;
            }
        }
        else if (command.equals("Hard AI")) {
            this.reset();
            MainBoard.gamemode = 4;
            this.hardAI = new AIHard();
            this.flag = true;
            final Object[] options = { "AI", "Player" };
            final int n = JOptionPane.showOptionDialog(null, "Who plays first?", "Turn decision", 0, 3, null, options, options[0]);
            if (n == 0) {
                MainBoard.markPlayer = 1;
                this.click(4);
                final AIHard hardAI = this.hardAI;
                hardAI.pos = String.valueOf(hardAI.pos) + '4';
                final int[] height = this.hardAI.height;
                final int n2 = 3;
                ++height[n2];
            }
            else {
                MainBoard.markPlayer = 0;
            }
        }
        else if (command.equals("Help")) {
            JOptionPane.showMessageDialog(null, " - Select a column to place your chip using the buttons above \n - To win, you must be the first player to get four of your colored checkers \n    in a row either horizontally, vertically or diagonally\n - Play against different difficulties of AI or play against a friend", "Rules", -1);
        }
        else if (command.equals("About")) {
            JOptionPane.showMessageDialog(null, "Game Version: 1.0 \n A game by: KMZ \n Latest Update: Jan 2018", "About", -1);
        }
        if (!MainBoard.gameOver) {
            if (command.equals("COL 1") && this.gameboard.playable(0)) {
                this.click(1);
                if (MainBoard.gamemode == 2) {
                    this.click(this.easyAI.makeMove() + 1);
                }
                else if (MainBoard.gamemode == 3) {
                    this.medMove(1);
                }
                else if (MainBoard.gamemode == 4) {
                    this.hardMove(1);
                }
            }
            else if (command.equals("COL 2") && this.gameboard.playable(1)) {
                this.click(2);
                if (MainBoard.gamemode == 2) {
                    this.click(this.easyAI.makeMove() + 1);
                }
                else if (MainBoard.gamemode == 3) {
                    this.medMove(2);
                }
                else if (MainBoard.gamemode == 4) {
                    this.hardMove(2);
                }
            }
            else if (command.equals("COL 3") && this.gameboard.playable(2)) {
                this.click(3);
                if (MainBoard.gamemode == 2) {
                    this.click(this.easyAI.makeMove() + 1);
                }
                else if (MainBoard.gamemode == 3) {
                    this.medMove(3);
                }
                else if (MainBoard.gamemode == 4) {
                    this.hardMove(3);
                }
            }
            else if (command.equals("COL 4") && this.gameboard.playable(3)) {
                this.click(4);
                if (MainBoard.gamemode == 2) {
                    this.click(this.easyAI.makeMove() + 1);
                }
                else if (MainBoard.gamemode == 3) {
                    this.medMove(4);
                }
                else if (MainBoard.gamemode == 4) {
                    this.hardMove(4);
                }
            }
            else if (command.equals("COL 5") && this.gameboard.playable(4)) {
                this.click(5);
                if (MainBoard.gamemode == 2) {
                    this.click(this.easyAI.makeMove() + 1);
                }
                else if (MainBoard.gamemode == 3) {
                    this.medMove(5);
                }
                else if (MainBoard.gamemode == 4) {
                    this.hardMove(5);
                }
            }
            else if (command.equals("COL 6") && this.gameboard.playable(5)) {
                this.click(6);
                if (MainBoard.gamemode == 2) {
                    this.click(this.easyAI.makeMove() + 1);
                }
                else if (MainBoard.gamemode == 3) {
                    this.medMove(6);
                }
                else if (MainBoard.gamemode == 4) {
                    this.hardMove(6);
                }
            }
            else if (command.equals("COL 7") && this.gameboard.playable(6)) {
                this.click(7);
                if (MainBoard.gamemode == 2) {
                    this.click(this.easyAI.makeMove() + 1);
                }
                else if (MainBoard.gamemode == 3) {
                    this.medMove(7);
                }
                else if (MainBoard.gamemode == 4) {
                    this.hardMove(7);
                }
            }
        }
    }
    
    public static void main(final String[] args) {
        new MainBoard();
    }
}
