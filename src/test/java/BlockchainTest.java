import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.powermock.api.mockito.PowerMockito.spy;

import Entity.Data;
import domain.Block;
import domain.Blockchain;
import domain.Miner;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(Blockchain.class)
class BlockchainTest {

    @Test
    void addBlockTest() {
        Blockchain blockchain = new Blockchain();

        Block firstBlock = new Block("0", 1, 1, blockchain.createData("massage", 1));
        boolean resultOfBlockValidation = blockchain.isValid(firstBlock);
        firstBlock.setTimeOfCreation(Duration.ofMillis(100));
        blockchain.addBlock(firstBlock);

        Miner miner = new Miner(blockchain);
        Block secondBlock = miner.createBlock();
        blockchain.addBlock(secondBlock);

        Block invalidBlock = new Block("0", 1, 2, blockchain.createData("massage", 1));
        boolean resultOfInvalidBlockValidation = blockchain.isValid(invalidBlock);

        assertTrue(resultOfBlockValidation);
        assertFalse(resultOfInvalidBlockValidation);
        assertEquals(2, blockchain.getBlockchainSize());
    }

    @Test
    void changeDifficultyTest() {
        Blockchain blockchain = new Blockchain();

        String resultOfIncreasing = blockchain.changeDifficulty(200);
        String resultOfDecreasing = blockchain.changeDifficulty(320);
        blockchain.changeDifficulty(320);
        String resultOfConstancy = blockchain.changeDifficulty(320);

        assertTrue(resultOfIncreasing.contains("N was increased to "));
        assertTrue(resultOfDecreasing.contains("N was decreased by "));
        assertTrue(resultOfConstancy.contains("N stays the same"));
    }


    @Test
    public void createDataTest() {
        Blockchain blockchain = new Blockchain();
        String massage = "test";
        int minerId = 1;

        Data result = blockchain.createData(massage, minerId);

        assertEquals(1, result.getIdentifier());
        assertEquals(minerId, result.getMinerID());
        assertEquals(massage, result.getMassages().get(0));
    }

    @Test
    public void isValidTest() {
        Blockchain mock = new Blockchain();

        String massage = "test";
        int minerId = 1;
        int id = 1;
        String hash = "test";
        Data data = new Data(minerId);
        data.setMassages(Collections.singletonList(massage));
        Block block = new Block(hash, minerId, id, data);

        boolean result = mock.isValid(block);
        assertTrue(result);
    }

    @Test
    public void printInformationTest() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);

        Blockchain blockchain = new Blockchain();

        Block firstBlock = new Block("0", 1, 1, blockchain.createData("massage", 1));
        firstBlock.setTimeOfCreation(Duration.ofMillis(100));
        blockchain.addBlock(firstBlock);
        Miner miner = new Miner(blockchain);
        Block secondBlock = miner.createBlock();
        blockchain.addBlock(secondBlock);
        Block thirdBlock = miner.createBlock();
        blockchain.addBlock(thirdBlock);

        blockchain.printInformation("N was increased to 6");

        assertTrue(byteArrayOutputStream.toString()
                .contains("Hash of the previous block: \n" + thirdBlock.getPreviousBlockHash()));
        assertTrue(byteArrayOutputStream.toString()
                .contains("Hash of the block: \n"+ thirdBlock.getHash()));

    }
}