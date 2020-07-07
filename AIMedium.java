import java.util.Scanner;

public class AIMedium
{
    private BoardMed b;
    private Scanner scan;
    private int nextMoveLocation;
    private int maxDepth;
    private int pturn;
    
    public AIMedium(final BoardMed b) {
        this.nextMoveLocation = -1;
        this.maxDepth = 9;
        this.b = b;
        this.scan = new Scanner(System.in);
    }
    
    public void playerMove() {
        System.out.println("Player's turn!");
        System.out.println("Please make a move(0 - 6):");
        int mov;
        for (mov = this.scan.nextInt(); mov < 0 || mov > 6 || !this.b.playable(mov); mov = this.scan.nextInt()) {
            System.out.println("Invalid move! Please make another move: ");
        }
        this.b.drop(mov, 2);
    }
    
    public int gameResult(final BoardMed b) {
        int aiScore = 0;
        int humanScore = 0;
        for (int i = 5; i >= 0; --i) {
            for (int j = 0; j <= 6; ++j) {
                if (b.board[j][i] != 0) {
                    if (j <= 3) {
                        for (int z = 0; z < 4; ++z) {
                            if (b.board[j + z][i] == 1) {
                                ++aiScore;
                            }
                            else {
                                if (b.board[j + z][i] != 2) {
                                    break;
                                }
                                ++humanScore;
                            }
                        }
                        if (aiScore == 4) {
                            return 1;
                        }
                        if (humanScore == 4) {
                            return 2;
                        }
                        aiScore = 0;
                        humanScore = 0;
                    }
                    if (i >= 3) {
                        for (int z = 0; z < 4; ++z) {
                            if (b.board[j][i - z] == 1) {
                                ++aiScore;
                            }
                            else {
                                if (b.board[j][i - z] != 2) {
                                    break;
                                }
                                ++humanScore;
                            }
                        }
                        if (aiScore == 4) {
                            return 1;
                        }
                        if (humanScore == 4) {
                            return 2;
                        }
                        aiScore = 0;
                        humanScore = 0;
                    }
                    if (j <= 3 && i >= 3) {
                        for (int z = 0; z < 4; ++z) {
                            if (b.board[j + z][i - z] == 1) {
                                ++aiScore;
                            }
                            else {
                                if (b.board[j + z][i - z] != 2) {
                                    break;
                                }
                                ++humanScore;
                            }
                        }
                        if (aiScore == 4) {
                            return 1;
                        }
                        if (humanScore == 4) {
                            return 2;
                        }
                        aiScore = 0;
                        humanScore = 0;
                    }
                    if (j >= 3 && i >= 3) {
                        for (int z = 0; z < 4; ++z) {
                            if (b.board[j - z][i - z] == 1) {
                                ++aiScore;
                            }
                            else {
                                if (b.board[j - z][i - z] != 2) {
                                    break;
                                }
                                ++humanScore;
                            }
                        }
                        if (aiScore == 4) {
                            return 1;
                        }
                        if (humanScore == 4) {
                            return 2;
                        }
                        aiScore = 0;
                        humanScore = 0;
                    }
                }
            }
        }
        for (int k = 0; k < 7; ++k) {
            if (b.board[k][0] == 0) {
                return -1;
            }
        }
        return 0;
    }
    
    int calculateScore(final int aiScore, final int moreMoves) {
        final int moveScore = 4 - moreMoves;
        if (aiScore == 0) {
            return 0;
        }
        if (aiScore == 1) {
            return moveScore;
        }
        if (aiScore == 2) {
            return moveScore << 3 + moveScore << 1;
        }
        if (aiScore == 3) {
            return moveScore << 6 + moveScore << 5 + moveScore << 2;
        }
        return 1000;
    }
    
    public int evaluateBoardMed(final BoardMed b) {
        int aiScore = 1;
        int score = 0;
        int blanks = 0;
        int z = 0;
        int moreMoves = 0;
        for (int i = 5; i >= 0; --i) {
            for (int j = 0; j <= 6; ++j) {
                if (b.board[j][i] != 0) {
                    if (b.board[j][i] != 2) {
                        if (j <= 3) {
                            for (z = 1; z < 4; ++z) {
                                if (b.board[j + z][i] == 1) {
                                    ++aiScore;
                                }
                                else {
                                    if (b.board[j + z][i] == 2) {
                                        aiScore = 0;
                                        blanks = 0;
                                        break;
                                    }
                                    ++blanks;
                                }
                            }
                            moreMoves = 0;
                            if (blanks > 0) {
                                for (int c = 1; c < 4; ++c) {
                                    for (int col = j + c, m = i; m <= 5 && b.board[col][m] == 0; ++m) {
                                        ++moreMoves;
                                    }
                                }
                            }
                            if (moreMoves != 0) {
                                score += this.calculateScore(aiScore, moreMoves);
                            }
                            aiScore = 1;
                            blanks = 0;
                        }
                        if (i >= 3) {
                            for (z = 1; z < 4; ++z) {
                                if (b.board[j][i - z] == 1) {
                                    ++aiScore;
                                }
                                else if (b.board[j][i - z] == 2) {
                                    aiScore = 0;
                                    break;
                                }
                            }
                            moreMoves = 0;
                            if (aiScore > 0) {
                                for (int col2 = j, k = i - z + 1; k <= i - 1 && b.board[col2][k] == 0; ++k) {
                                    ++moreMoves;
                                }
                            }
                            if (moreMoves != 0) {
                                score += this.calculateScore(aiScore, moreMoves);
                            }
                            aiScore = 1;
                            blanks = 0;
                        }
                        if (j >= 3) {
                            for (z = 1; z < 4; ++z) {
                                if (b.board[j - z][i] == 1) {
                                    ++aiScore;
                                }
                                else {
                                    if (b.board[j - z][i] == 2) {
                                        aiScore = 0;
                                        blanks = 0;
                                        break;
                                    }
                                    ++blanks;
                                }
                            }
                            moreMoves = 0;
                            if (blanks > 0) {
                                for (int c = 1; c < 4; ++c) {
                                    for (int col = j - c, m = i; m <= 5 && b.board[col][m] == 0; ++m) {
                                        ++moreMoves;
                                    }
                                }
                            }
                            if (moreMoves != 0) {
                                score += this.calculateScore(aiScore, moreMoves);
                            }
                            aiScore = 1;
                            blanks = 0;
                        }
                        if (j <= 3 && i >= 3) {
                            for (z = 1; z < 4; ++z) {
                                if (b.board[j + z][i - z] == 1) {
                                    ++aiScore;
                                }
                                else {
                                    if (b.board[j + z][i - z] == 2) {
                                        aiScore = 0;
                                        blanks = 0;
                                        break;
                                    }
                                    ++blanks;
                                }
                            }
                            moreMoves = 0;
                            if (blanks > 0) {
                                for (int c = 1; c < 4; ++c) {
                                    final int col = j + c;
                                    int l;
                                    for (int row = l = i - c; l <= 5; ++l) {
                                        if (b.board[col][l] == 0) {
                                            ++moreMoves;
                                        }
                                        else if (b.board[col][l] != 1) {
                                            break;
                                        }
                                    }
                                }
                                if (moreMoves != 0) {
                                    score += this.calculateScore(aiScore, moreMoves);
                                }
                                aiScore = 1;
                                blanks = 0;
                            }
                        }
                        if (j >= 3 && i >= 3) {
                            for (z = 1; z < 4; ++z) {
                                if (b.board[j - z][i - z] == 1) {
                                    ++aiScore;
                                }
                                else {
                                    if (b.board[j - z][i - z] == 2) {
                                        aiScore = 0;
                                        blanks = 0;
                                        break;
                                    }
                                    ++blanks;
                                }
                            }
                            moreMoves = 0;
                            if (blanks > 0) {
                                for (int c = 1; c < 4; ++c) {
                                    final int col = j - c;
                                    int l;
                                    for (int row = l = i - c; l <= 5; ++l) {
                                        if (b.board[col][l] == 0) {
                                            ++moreMoves;
                                        }
                                        else if (b.board[col][l] != 1) {
                                            break;
                                        }
                                    }
                                }
                                if (moreMoves != 0) {
                                    score += this.calculateScore(aiScore, moreMoves);
                                }
                                aiScore = 1;
                                blanks = 0;
                            }
                        }
                    }
                }
            }
        }
        return score;
    }
    
    public int minimax(final int depth, final int turn, int alpha, int beta) {
        if (beta <= alpha) {
            if (turn == 1) {
                return Integer.MAX_VALUE;
            }
            return Integer.MIN_VALUE;
        }
        else {
            final int gameResult = this.gameResult(this.b);
            if (gameResult == 1) {
                return 1073741823;
            }
            if (gameResult == 2) {
                return -1073741824;
            }
            if (gameResult == 0) {
                return 0;
            }
            if (depth == this.maxDepth) {
                return this.evaluateBoardMed(this.b);
            }
            int maxScore = Integer.MIN_VALUE;
            int minScore = Integer.MAX_VALUE;
            for (int j = 0; j <= 6; ++j) {
                int currentScore = 0;
                if (this.b.playable(j)) {
                    if (turn == 1) {
                        this.b.drop(j, 1);
                        currentScore = this.minimax(depth + 1, 2, alpha, beta);
                        if (depth == 0) {
                            if (currentScore > maxScore) {
                                this.nextMoveLocation = j;
                            }
                            if (currentScore == 1073741823) {
                                this.b.undoMove(j);
                                break;
                            }
                        }
                        maxScore = Math.max(currentScore, maxScore);
                        alpha = Math.max(currentScore, alpha);
                    }
                    else if (turn == 2) {
                        this.b.drop(j, 2);
                        currentScore = this.minimax(depth + 1, 1, alpha, beta);
                        minScore = Math.min(currentScore, minScore);
                        beta = Math.min(currentScore, beta);
                    }
                    this.b.undoMove(j);
                    if (currentScore == Integer.MAX_VALUE) {
                        break;
                    }
                    if (currentScore == Integer.MIN_VALUE) {
                        break;
                    }
                }
            }
            return (turn == 1) ? maxScore : minScore;
        }
    }
    
    public int makeMove() {
        this.nextMoveLocation = -1;
        this.minimax(0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return this.nextMoveLocation;
    }
    
    public void startGame() {
        System.out.println("Do you want to go first or second? (1 - first / 2 - second)");
        this.pturn = this.scan.nextInt();
        while (this.pturn != 1 && this.pturn != 2) {
            System.out.println("Invalid request! Please re-enter your choice: ");
            this.pturn = this.scan.nextInt();
        }
        if (this.pturn == 1) {
            this.playerMove();
        }
        this.b.display();
        this.b.drop(3, 1);
        this.b.display();
        int gameResult;
        do {
            this.playerMove();
            this.b.display();
            gameResult = this.gameResult(this.b);
            if (gameResult == 1) {
                System.out.println("AI Wins!");
            }
            else if (gameResult == 2) {
                System.out.println("Player Wins!");
            }
            else if (gameResult == 0) {
                System.out.println("Draw!");
            }
            else {
                this.b.drop(this.makeMove(), 1);
                this.b.display();
                gameResult = this.gameResult(this.b);
                if (gameResult == 1) {
                    System.out.println("AI Wins!");
                }
                else {
                    if (gameResult != 2) {
                        continue;
                    }
                    System.out.println("Player Wins!");
                }
            }
            return;
        } while (gameResult != 0);
        System.out.println("Draw!");
    }
}
