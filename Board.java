public class Board
{
    public final int WIDTH = 7;
    public final int HEIGHT = 6;
    public int[][] board;
    public int[] level;
    public int moves;
    
    public Board() {
        this.board = new int[7][6];
        this.level = new int[7];
        this.moves = 0;
    }
    
    public boolean playable(final int col) {
        return this.level[col] < 6;
    }
    
    public int cntMoves() {
        return this.moves;
    }
    
    public boolean isWinningMove(final int col) {
        if (col == -1) {
            return false;
        }
        final int cur_plr = 1 + this.moves % 2;
        if (this.level[col] >= 3 && this.board[col][this.level[col] - 1] == cur_plr && this.board[col][this.level[col] - 2] == cur_plr && this.board[col][this.level[col] - 3] == cur_plr) {
            return true;
        }
        for (int i = -1; i <= 1; ++i) {
            int cnt = 0;
            for (int j = -1; j <= 1; j += 2) {
                for (int x = col + j, y = this.level[col] + j * i; x >= 0 && x < 7 && y >= 0 && y < 6 && this.board[x][y] == cur_plr; x += j, y += j * i, ++cnt) {}
            }
            if (cnt >= 3) {
                return true;
            }
        }
        return false;
    }
    
    public boolean check(final int x) {
        if (this.moves > 42) {
            return false;
        }
        for (int i = 5; i >= 0; --i) {
            for (int j = 0; j < 7; ++j) {
                if (i >= 3 && this.board[j][i] == x && this.board[j][i - 1] == x && this.board[j][i - 2] == x && this.board[j][i - 3] == x) {
                    return true;
                }
                if (j < 4 && this.board[j][i] == x && this.board[j + 1][i] == x && this.board[j + 2][i] == x && this.board[j + 3][i] == x) {
                    return true;
                }
                if (i >= 3 && j < 4 && this.board[j][i] == x && this.board[j + 1][i - 1] == x && this.board[j + 2][i - 2] == x && this.board[j + 3][i - 3] == x) {
                    return true;
                }
                if (i >= 3 && j >= 3 && this.board[j][i] == x && this.board[j - 1][i - 1] == x && this.board[j - 2][i - 2] == x && this.board[j - 3][i - 3] == x) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public int drop(final int col) {
        this.board[col][this.level[col]] = 1 + this.moves % 2;
        final int[] level = this.level;
        ++level[col];
        ++this.moves;
        return this.level[col];
    }
    
    public int getLevel(final int col) {
        return this.level[col];
    }
    
    public int[][] returnBoard() {
        return this.board;
    }
    
    public void boardinit() {
        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j < 6; ++j) {
                this.board[i][j] = 0;
            }
            this.level[i] = 0;
        }
        this.moves = 0;
    }
    
    public void display() {
        System.out.println(" |0 1 2 3 4 5 6");
        System.out.println("---------------");
        for (int i = 5; i >= 0; --i) {
            System.out.print(String.valueOf(i) + "|");
            for (int j = 0; j < 7; ++j) {
                System.out.print(String.valueOf(this.board[j][i]) + " ");
            }
            System.out.println();
        }
        System.out.println("---------------");
    }
    
    public void undoMove(final int col) {
        if (this.level[col] > 5) {
            this.level[col] = 5;
        }
        this.board[col][this.level[col]] = 0;
        final int[] level = this.level;
        --level[col];
        --this.moves;
    }
    
    public int result() {
        if (this.moves > 42) {
            return 0;
        }
        if (this.check(1)) {
            return 1;
        }
        if (this.check(2)) {
            return 2;
        }
        return -1;
    }
}
