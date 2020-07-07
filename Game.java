import java.util.Scanner;

public class Game
{
    private static Scanner scan;
    
    static {
        Game.scan = new Scanner(System.in);
    }
    
    public static void main(final String[] arg) {
        int player = 0;
        System.out.println("Please choose a game mode: ");
        System.out.println("(PvP - 1 / Easy AI - 2 / Medium AI - 3 / Hard AI - 4)");
        int opt;
        for (opt = Game.scan.nextInt(); opt < 1 || opt > 4; opt = Game.scan.nextInt()) {
            System.out.println("Invalid request! Please re-enter your choice: ");
        }
        if (opt == 1) {
            final Board b = new Board();
            b.display();
            do {
                player = 1 + b.moves % 2;
                System.out.println("Player " + player + "'s turn!");
                System.out.println("Please make a move(0 - 6):");
                int mov;
                for (mov = Game.scan.nextInt(); !b.playable(mov); mov = Game.scan.nextInt()) {
                    System.out.println("Invalid move! Please make another move: ");
                }
                b.drop(mov);
                b.display();
            } while (!b.check(player));
            if (b.cntMoves() == 42) {
                System.out.println("Draw!");
            }
            else {
                System.out.println("Player " + player + " wins!");
            }
        }
        else if (opt == 2) {
            System.out.println("Do you want to go first or second? (1 - first / 2 - second)");
            int turn;
            for (turn = Game.scan.nextInt(); turn != 1 && turn != 2; turn = Game.scan.nextInt()) {
                System.out.println("Invalid request! Please re-enter your choice: ");
            }
            final AIEasy b2 = new AIEasy(turn);
            b2.display();
            if (turn == 2) {
                b2.drop(3);
            }
            do {
                player = 1 + b2.moves % 2;
                if (player == turn) {
                    System.out.println("Player's turn!");
                    System.out.println("Please make a move(0 - 6):");
                    int mov2;
                    for (mov2 = Game.scan.nextInt(); !b2.playable(mov2); mov2 = Game.scan.nextInt()) {
                        System.out.println("Invalid move! Please make another move: ");
                    }
                    b2.drop(mov2);
                    b2.display();
                }
                else {
                    int mov2;
                    do {
                        mov2 = b2.makeMove();
                    } while (!b2.playable(mov2));
                    b2.drop(mov2);
                    System.out.println("AI chooses column " + mov2 + "!");
                    b2.display();
                }
            } while (!b2.check(player));
            if (b2.cntMoves() == 42) {
                System.out.println("Draw!");
            }
            else if (player == turn) {
                System.out.println("Player wins!");
            }
            else {
                System.out.println("AI wins!");
            }
        }
        else if (opt == 4) {
            System.out.println("Do you want to go first or second? (1 - first / 2 - second)");
            int turn;
            for (turn = Game.scan.nextInt(); turn != 1 && turn != 2; turn = Game.scan.nextInt()) {
                System.out.println("Invalid request! Please re-enter your choice: ");
            }
            final AIHard b3 = new AIHard();
            boolean flag = true;
            if (turn == 2) {
                b3.drop(3);
                final AIHard aiHard = b3;
                aiHard.pos = String.valueOf(aiHard.pos) + '4';
            }
            b3.display();
            do {
                player = 1 + b3.moves % 2;
                if (player == turn) {
                    System.out.println("Player's turn!");
                    System.out.println("Please make a move(0 - 6):");
                    int mov2;
                    for (mov2 = Game.scan.nextInt(); !b3.playable(mov2); mov2 = Game.scan.nextInt()) {
                        System.out.println("Invalid move! Please make another move: ");
                    }
                    b3.drop(mov2);
                    final AIHard aiHard2 = b3;
                    aiHard2.pos = String.valueOf(aiHard2.pos) + (char)(mov2 + 1 + 48);
                    b3.display();
                }
                else {
                    int mov2;
                    do {
                        mov2 = b3.makeMove();
                    } while (!b3.playable(mov2));
                    b3.drop(mov2);
                    final AIHard aiHard3 = b3;
                    aiHard3.pos = String.valueOf(aiHard3.pos) + (char)(mov2 + 1 + 48);
                    System.out.println("AI chooses column " + mov2 + "!");
                    b3.display();
                }
                if (flag) {
                    b3.pos = b3.pos.substring(4, b3.pos.length());
                    flag = false;
                }
            } while (!b3.check(player));
            if (b3.cntMoves() == 42) {
                System.out.println("Draw!");
            }
            else if (player == turn) {
                System.out.println("Player wins!");
            }
            else {
                System.out.println("AI wins!");
            }
        }
    }
}
