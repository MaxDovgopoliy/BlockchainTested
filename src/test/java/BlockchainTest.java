import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class BlockchainTest {

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


}