package com.hanna.rpc;

public class Result extends Game {

    public Result() {
    }

    
    public String determineWinner(Player p1, Player p2) {
        int result = p1.getMove().compare(p2.getMove());
        if (result == 0)
            return "Draw!";
        if (result == 1)
            return p1.getUsername();

        return p2.getUsername();
    }

}
