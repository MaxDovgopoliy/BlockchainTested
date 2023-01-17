import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Miner extends Thread {
    private int coins = 100;
    private static AtomicInteger minersCount = new AtomicInteger(0);
    private int id;

    Blockchain blockchain;

    public boolean running = true;

    public Miner(Blockchain blockchain) {
        this.blockchain = blockchain;
        this.id = minersCount.incrementAndGet();
    }

    @Override
    public void run() {
        while (running) {
            Block block = createBlock();
            try {
                blockchain.addBlock(block);
                coins += 100;
                if (!blockchain.isRunning()) {
                    running = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public Block createBlock() {
        long startTime = System.currentTimeMillis();
        Data data = blockchain.createData("Created by miner " + id, id);
        while (true) {
            Block block;
            if (blockchain.getBlockchainSize() == 0) {
                block = new Block("0", id, 1, data);
            } else {
                block = new Block(blockchain.getLastBlockHash(), id, blockchain.getLastBlockId() + 1, data);
            }
            if (blockchain.isValid(block)) {
                block.setTimeOfCreation(Duration.ofMillis(System.currentTimeMillis() - startTime));
                return block;
            }
        }

    }

    public String makeTransaction(int countOfCoins, int minerId) {
        if (countOfCoins > this.coins) {
            return "error";
        } else {
            Miner miner = Main.miners.stream().filter(x -> x.id == minerId).findAny().orElse(null);
            if (miner != null) {
                coins = -countOfCoins;
                miner.coins += countOfCoins;
                return "miner" + id + " sent " + countOfCoins + " VC to miner" + minerId;
            } else {
                return "no miner with this id";
            }
        }
    }

    public int getMinerId() {
        return id;
    }
}
