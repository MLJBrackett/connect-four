public class BoardMed
{
    private final int WIDTH = 7;
    private final int HEIGHT = 6;
    private final int MIN_SCORE = -18;
    private final int MAX_SCORE = 18;
    private long current_position;
    private long mask;
    private long bottom_mask;
    private long board_mask;
    public int[][] board;
    private int moves;
    
    public BoardMed() {
        this.bottom_mask = this.bottom(7, 6);
        this.board_mask = this.bottom_mask * 63L;
        this.board = new int[7][6];
        this.moves = 0;
    }
    
    public boolean playable(final int col) {
        return this.board[col][0] == 0;
    }
    
    public int cntMoves() {
        return this.moves;
    }
    
    public boolean check(final int x) {
        for (int i = 0; i <= 6; --i) {
            for (int j = 0; j < 7; ++j) {
                if (i <= 3 && this.board[j][i] == x && this.board[j][i + 1] == x && this.board[j][i + 2] == x && this.board[j][i + 3] == x) {
                    return true;
                }
                if (j < 4 && this.board[j][i] == x && this.board[j + 1][i] == x && this.board[j + 2][i] == x && this.board[j + 3][i] == x) {
                    return true;
                }
                if (i <= 3 && j < 4 && this.board[j][i] == x && this.board[j + 1][i + 1] == x && this.board[j + 2][i + 2] == x && this.board[j + 3][i + 3] == x) {
                    return true;
                }
                if (i <= 3 && j >= 3 && this.board[j][i] == x && this.board[j - 1][i + 1] == x && this.board[j - 2][i - 2] == x && this.board[j - 3][i + 3] == x) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void display() {
        System.out.println(" |0 1 2 3 4 5 6");
        System.out.println("---------------");
        for (int i = 0; i < 6; ++i) {
            System.out.print(String.valueOf(i) + "|");
            for (int j = 0; j < 7; ++j) {
                System.out.print(String.valueOf(this.board[j][i]) + " ");
            }
            System.out.println();
        }
        System.out.println("---------------");
    }
    
    public boolean drop(final int col, final int plr) {
        if (!this.playable(col)) {
            System.out.println("Illegal move!");
            return false;
        }
        for (int i = 5; i >= 0; --i) {
            if (this.board[col][i] == 0) {
                this.board[col][i] = plr;
                ++this.moves;
                return true;
            }
        }
        return false;
    }
    
    public void undoMove(final int col) {
        for (int i = 0; i <= 5; ++i) {
            if (this.board[col][i] != 0) {
                this.board[col][i] = 0;
                break;
            }
        }
    }
    
    public long bottom(final int width, final int height) {
        return (width == 0) ? 0L : (this.bottom(width - 1, height) | (long)(1 << (width - 1) * (height + 1)));
    }
    
    public void play(final long move) {
        this.current_position ^= this.mask;
        this.mask |= move;
        ++this.moves;
    }
    
    public boolean canWinNext() {
        return this.winning_position() != 0L & this.possible() != 0L;
    }
    
    public long key() {
        return this.current_position + this.mask;
    }
    
    public long possibleNonLosingMoves() {
        long possible_mask = this.possible();
        final long opponent_win = this.opponent_winning_position();
        final long forced_moves = possible_mask & opponent_win;
        if (forced_moves != 0L) {
            if (forced_moves != 0L & forced_moves - 1L != 0L) {
                return 0L;
            }
            possible_mask = forced_moves;
        }
        return possible_mask & ~(opponent_win >> 1);
    }
    
    public long moveScore(final long move) {
        return this.popcount(this.compute_winning_position(this.current_position | move, this.mask));
    }
    
    public boolean canPlay(final int col) {
        return this.mask != 0L & this.top_mask_col(col) != 0L;
    }
    
    public void playCol(final int col) {
        this.play(this.mask + this.bottom_mask_col(col) & this.column_mask(col));
    }
    
    public boolean isWinningMove(final int col) {
        return (this.winning_position() & this.possible() & this.column_mask(col)) != 0x0L;
    }
    
    public long winning_position() {
        return this.compute_winning_position(this.current_position, this.mask);
    }
    
    public long opponent_winning_position() {
        return this.compute_winning_position(this.current_position ^ this.mask, this.mask);
    }
    
    public long possible() {
        return this.mask + this.bottom_mask & this.board_mask;
    }
    
    public long popcount(long m) {
        long c;
        for (c = 0L, c = 0L; m != 0L; m &= m - 1L, ++c) {}
        return c;
    }
    
    public long compute_winning_position(final long position, final long mask) {
        long r = position << 1 & position << 2 & position << 3;
        long p = position << 7 & position << 14;
        r |= (p & position << 21);
        r |= (p & position >> 7);
        p = (position >> 7 & position >> 14);
        r |= (p & position << 7);
        r |= (p & position >> 21);
        p = (position << 6 & position << 12);
        r |= (p & position << 18);
        r |= (p & position >> 6);
        p = (position >> 6 & position >> 12);
        r |= (p & position << 6);
        r |= (p & position >> 18);
        p = (position << 8 & position << 16);
        r |= (p & position << 24);
        r |= (p & position >> 8);
        p = (position >> 8 & position >> 16);
        r |= (p & position << 8);
        r |= (p & position >> 24);
        return r & (this.board_mask ^ mask);
    }
    
    public long top_mask_col(final int col) {
        return 1 << 5 + col * 7;
    }
    
    public long bottom_mask_col(final int col) {
        return 1 << col * 7;
    }
    
    public long column_mask(final int col) {
        return 63 << col * 7;
    }
}
