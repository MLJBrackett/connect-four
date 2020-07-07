import java.net.URLConnection;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AIHard extends Board
{
    public String pos;
    public int[] height;
    
    public AIHard() {
        this.height = new int[7];
    }
    
    public int makeMove() {
        int index = 0;
        try {
            final String web = "http://connect4.gamesolver.org/solve?pos=" + this.pos;
            final URL url = new URL(web);
            final URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if (urlConnection instanceof HttpURLConnection) {
                connection = (HttpURLConnection)urlConnection;
            }
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String urlString = "";
            String current;
            while ((current = in.readLine()) != null) {
                urlString = String.valueOf(urlString) + current;
            }
            final String[] tokens = urlString.substring(urlString.indexOf("[") + 1, urlString.indexOf("]")).split(",");
            final int[] score = new int[tokens.length];
            int max = -10000;
            final int[] spot = new int[tokens.length];
            boolean flag = false;
            int cnt = 0;
            for (int i = 0; i < tokens.length; ++i) {
                int x = 1;
                if (tokens[i].charAt(0) == '-') {
                    x = -1;
                    tokens[i] = tokens[i].substring(1, tokens[i].length());
                }
                for (int j = 0; j < tokens[i].length(); ++j) {
                    final int[] array = score;
                    final int n = i;
                    array[n] *= 10;
                    final int[] array2 = score;
                    final int n2 = i;
                    array2[n2] += tokens[i].charAt(j) - '0';
                }
                final int[] array3 = score;
                final int n3 = i;
                array3[n3] *= x;
                if (score[i] > max && this.height[i] <= 5) {
                    max = score[i];
                    index = i;
                    if (flag) {
                        for (int j = 0; j < tokens.length; ++j) {
                            spot[j] = 0;
                        }
                        cnt = 0;
                        flag = false;
                    }
                    spot[cnt] = i;
                }
                else if (score[i] == max && this.height[i] <= 5) {
                    flag = true;
                    ++cnt;
                    spot[cnt] = i;
                }
            }
            if (flag) {
                index = spot[(int)(Math.random() * cnt)];
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return index;
    }
}
