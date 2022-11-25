import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Blockchain implements Serializable {
    private List<Block> blocks = new LinkedList<>();
    private static int difficulty = 0;
    private boolean running = true;


    public synchronized int addBlock(Block block) {
        if (!running | !isValid(block)) {
            return 0;
        }
        blocks.add(block);
        String difficulty = changeDifficulty(block.getTimeOfCreation().toMillis());
        printInformation(difficulty);
        if (blocks.size() >= 15) {
            running = false;
            return -1;
        }
        return 1;
    }

    public String changeDifficulty(long seconds) {
        String result;
        if (seconds < 300) {
            difficulty++;
            result = "N was increased to " + difficulty;
        } else if (seconds > 300 & difficulty >= 0) {
            difficulty--;
            result = "N was decreased by " + 1;
        } else {
            result = "N stays the same";
        }
        return result;
    }

    public Data createData(String massage, int minerID) {
        Data data = new Data(minerID);
        if (getBlockchainSize() == 0) {
            data.setIdentifier(1);
        } else {
            data.setIdentifier(getBlockchainSize() + 1);
        }
        data.setMassages(new ArrayList<>(List.of(massage)));
        return data;
    }

    public void saveInFile() {
        try {

            FileOutputStream fileOut = new FileOutputStream("text.txt");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public boolean isValid(Block block) {
        if (block.getId() != 1 && !block.getPreviousBlockHash().equals(blocks.get(blocks.size() - 1).getHash())) {
            return false;
        }
        if (block.getId() != 1 && block.getId() != blocks.get(blocks.size() - 1).getId() + 1) {
            return false;
        }
        if (block.getId() == 1 && !blocks.isEmpty()) {
            return false;
        }
        for (int i = 0; i < difficulty; i++) {
            if (block.getHash().charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }

    public void printInformation(String difficulty) {
        Block block = blocks.get(blocks.size() - 1);
        System.out.println("\nBlock:\n" +
                "Created by miner" + block.getMinerId() + "\n" +
                "miner" + block.getMinerId() + " gets 100 VC\n" +
                "Id: " + block.getId() + "\n" +
                "Timestamp: " + block.getTimeStamp() + "\n" +
                "Magic number:" + block.getMagicNumber() + "\n" +
                "Hash of the previous block: \n" +
                "" + block.getPreviousBlockHash() + "\n" +
                "Hash of the block: \n" +
                "" + block.getHash() + "\n" +
                "Block data:\n" +
                block.getMassage() + "\n" +
                "Block was generating for " + block.getTimeOfCreation().getSeconds() + "\n" +
                difficulty);

    }

    public int getBlockchainSize() {
        return this.blocks.size();
    }

    public String getLastBlockHash() {
        return this.blocks.get(blocks.size() - 1).getHash();
    }

    public int getLastBlockId() {
        return this.blocks.get(blocks.size() - 1).getId();
    }

    public boolean isRunning() {
        return running;
    }
}
