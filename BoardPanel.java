import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPanel;

public class BoardPanel extends JPanel
{
    public static final long serialVersionUID = 1L;
    final int HEIGHT = 6;
    final int ROW = 7;
    final int COL_1_X = 25;
    final int COL_2_X = 105;
    final int COL_3_X = 185;
    final int COL_4_X = 265;
    final int COL_5_X = 345;
    final int COL_6_X = 425;
    final int COL_7_X = 505;
    final int COL_Y = 180;
    final int COL_Y_MIN = 580;
    public static int ylow;
    public int[][] mark;
    public int moves;
    public int COL_NUM;
    public int pCount;
    public int gameState;
    public static int markPlayer;
    UpdateThread updatethread;
    public Color c1;
    public Color c2;
    Font f;
    Color nColor1;
    Color nColor2;
    
    static {
        BoardPanel.ylow = 580;
    }
    
    public BoardPanel() {
        this.mark = new int[7][6];
        this.gameState = 0;
        this.updatethread = new UpdateThread(this);
        this.f = new Font("Dialog", 0, 20);
        this.nColor1 = new Color(255, 102, 102);
        this.nColor2 = new Color(255, 255, 153);
        this.c1 = new Color(255, 0, 0);
        this.c2 = new Color(255, 204, 0);
    }
    
    public void update_col(final int col, final int ymin, final int[][] board, final int move) {
        this.COL_NUM = col;
        final int COL_Y_STOP = 580 - 80 * (ymin - 1);
        this.mark = board;
        this.moves = move;
        switch (this.COL_NUM) {
            case 1: {
                this.updatethread.update_init(25, 180, COL_Y_STOP);
                break;
            }
            case 2: {
                this.updatethread.update_init(105, 180, COL_Y_STOP);
                break;
            }
            case 3: {
                this.updatethread.update_init(185, 180, COL_Y_STOP);
                break;
            }
            case 4: {
                this.updatethread.update_init(265, 180, COL_Y_STOP);
                break;
            }
            case 5: {
                this.updatethread.update_init(345, 180, COL_Y_STOP);
                break;
            }
            case 6: {
                this.updatethread.update_init(425, 180, COL_Y_STOP);
                break;
            }
            case 7: {
                this.updatethread.update_init(505, 180, COL_Y_STOP);
                break;
            }
        }
    }
    
    public void setMoves(final int move) {
        this.moves = move;
    }
    
    public void setWinner(final int w, final int mark) {
        this.gameState = w;
        BoardPanel.markPlayer = mark;
    }
    
    public void paintComponent(final Graphics g) {
        int x = 25;
        int y = 180;
        if (this.moves % 2 == 0) {
            this.pCount = 1;
        }
        else {
            this.pCount = 2;
        }
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), 150);
        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j < 6; ++j) {
                g.fillOval(x, y, 50, 50);
                y += 80;
            }
            y = 180;
            x += 80;
        }
        if (this.pCount == 1) {
            g.setColor(this.c1);
            g.fillOval(105, 60, 50, 50);
            g.setColor(Color.WHITE);
            g.fillOval(425, 60, 50, 50);
        }
        else {
            g.setColor(Color.WHITE);
            g.fillOval(105, 60, 50, 50);
            g.setColor(this.c2);
            g.fillOval(425, 60, 50, 50);
        }
        g.setFont(this.f);
        g.setColor(Color.BLACK);
        if (g instanceof Graphics2D) {
            final Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (this.gameState == 0) {
                g2.drawString("Player " + this.pCount + "'s turn", 230, 90);
            }
            else if (this.gameState == 1 && BoardPanel.markPlayer == 1) {
                g2.drawString("AI wins!", 230, 90);
                g.setColor(this.c1);
                g.fillOval(105, 60, 50, 50);
                g.setColor(Color.WHITE);
                g.fillOval(420, 55, 70, 70);
            }
            else if (this.gameState == 2 && BoardPanel.markPlayer == 1) {
                g2.drawString("Player Wins!", 230, 90);
                g.setColor(this.c2);
                g.fillOval(425, 60, 50, 50);
                g.setColor(Color.WHITE);
                g.fillOval(95, 55, 70, 70);
            }
            else if (this.gameState == 1 && BoardPanel.markPlayer == 0) {
                g2.drawString("Player wins!", 230, 90);
                g.setColor(this.c1);
                g.fillOval(105, 60, 50, 50);
                g.setColor(Color.WHITE);
                g.fillOval(420, 55, 70, 70);
            }
            else if (this.gameState == 2 && BoardPanel.markPlayer == 0) {
                g2.drawString("AI wins!", 230, 90);
                g.setColor(this.c2);
                g.fillOval(425, 60, 50, 50);
                g.setColor(Color.WHITE);
                g.fillOval(95, 55, 70, 70);
            }
            else if (this.gameState == 1 && BoardPanel.markPlayer == 3) {
                g2.drawString("Player 1 wins!", 230, 90);
                g.setColor(this.c1);
                g.fillOval(105, 60, 50, 50);
                g.setColor(Color.WHITE);
                g.fillOval(420, 55, 70, 70);
            }
            else if (this.gameState == 2 && BoardPanel.markPlayer == 3) {
                g2.drawString("Player 2 wins!", 230, 90);
                g.setColor(this.c2);
                g.fillOval(425, 60, 50, 50);
                g.setColor(Color.WHITE);
                g.fillOval(95, 55, 70, 70);
            }
            else {
                g2.drawString("It's a draw!", 230, 90);
                g.setColor(this.nColor1);
                g.fillOval(105, 60, 50, 50);
                g.setColor(this.nColor2);
                g.fillOval(425, 60, 50, 50);
            }
        }
        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j < 6; ++j) {
                if (this.mark[i][j] == 1) {
                    g.setColor(this.c1);
                    if (this.updatethread.x != 25 + 80 * i || BoardPanel.ylow != 580 - 80 * j) {
                        g.fillOval(25 + 80 * i, 580 - 80 * j, 50, 50);
                    }
                }
                else if (this.mark[i][j] == 2) {
                    g.setColor(this.c2);
                    if (this.updatethread.x != 25 + 80 * i || BoardPanel.ylow != 580 - 80 * j) {
                        g.fillOval(25 + 80 * i, 580 - 80 * j, 50, 50);
                    }
                }
            }
        }
        if (this.moves % 2 == 0) {
            g.setColor(this.c2);
        }
        else {
            g.setColor(this.c1);
        }
        if (this.moves == 0) {
            g.setColor(Color.WHITE);
        }
        g.fillOval(this.updatethread.x, this.updatethread.y, 50, 50);
    }
    
    public static class UpdateThread implements Runnable
    {
        private int x;
        private int y;
        private int dy;
        private volatile boolean run_enable;
        public BoardPanel pnl;
        
        public UpdateThread(final BoardPanel f) {
            this.x = 25;
            this.y = 180;
            this.dy = 2;
            this.run_enable = false;
            this.pnl = f;
        }
        
        public void update_init(final int x0, final int y0, final int ymin) {
            this.run_enable = true;
            this.x = x0;
            this.y = y0;
            BoardPanel.ylow = ymin;
        }
        
        @Override
        public void run() {
            while (this.run_enable) {
                this.y += this.dy;
                if (this.y >= BoardPanel.ylow) {
                    this.run_enable = false;
                }
                this.pnl.repaint();
                try {
                    Thread.sleep(10L);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
