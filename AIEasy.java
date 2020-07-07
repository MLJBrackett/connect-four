import java.util.Random;

public class AIEasy extends Board
{
    private static Random rand;
    
    static {
        AIEasy.rand = new Random();
    }
    
    public AIEasy(final int player) {
    }
    
    public int makeMove() {
        final int mov = AIEasy.rand.nextInt(7);
        return mov;
    }
}
