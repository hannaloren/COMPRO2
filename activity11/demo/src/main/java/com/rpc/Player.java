package activity11.demo.rpc;

package activity11;

public class Player {
    private String username;
    private String password;
    private int wins;
    private int losses;
    private Move move;

    public Player(String username, String password, int wins, int losses) {
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.losses = losses;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public void win() {
        wins++;
    }

    public void lose() {
        losses++;
    }
}
