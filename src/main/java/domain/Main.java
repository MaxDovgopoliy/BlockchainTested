package domain;

import java.util.ArrayList;
import java.util.List;


public class Main {
    static Blockchain blockchain = new Blockchain();
    static List<Miner> miners = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
        main.startMining();

    }

    public void startMining() {
        for (int i = 0; i < 9; i++) {
            miners.add(new Miner(blockchain));
        }

        for (Miner miner : miners) {
            miner.start();
        }
    }
}
