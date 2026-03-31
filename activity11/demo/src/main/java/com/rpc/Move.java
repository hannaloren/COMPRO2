package activity11.demo.rpc;

public enum Move {
     ROCK, PAPER, SCISSORS;

    public static Move fromString(String input) {
        return Move.valueOf(input.toUpperCase());
    }
}
}
