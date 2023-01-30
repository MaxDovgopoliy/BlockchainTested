import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import Entity.Data;
import domain.Block;
import domain.Blockchain;
import domain.Miner;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class MinerTest {

    @Test
    void addBlock() {
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
    void changeDifficulty() {
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
    public void runTest() throws InterruptedException {
        Blockchain blockchain = new Blockchain();
        Miner miner = new Miner(blockchain);
        miner.start();

        miner.join();
        assertEquals(15, blockchain.getBlockchainSize());
    }

    @Test
    public void createBlockTest() {
        Blockchain blockchain = Mockito.mock(Blockchain.class);
        Miner miner = new Miner(blockchain);
        Data data = new Data(1);
        data.setMassages(Collections.singletonList("massage"));
        Mockito.when(blockchain.createData(anyString(), anyInt())).thenReturn(data);
        Mockito.when(blockchain.isValid(any(Block.class))).thenReturn(true);

        Block block = miner.createBlock();

        assertEquals(block.getMinerId(), 1);
        assertEquals(block.getMassage(), "[massage]");
        assertEquals(block.getPreviousBlockHash(), "0");
    }
}