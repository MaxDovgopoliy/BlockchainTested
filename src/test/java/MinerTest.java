import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import com.ginsberg.junit.exit.ExpectSystemExit;
import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class MinerTest {

    @Test
    void makeTransactionTest() {
        Blockchain blockchain = new Blockchain();
        Miner miner1 = new Miner(blockchain);
        Miner miner2 = new Miner(blockchain);
        Main.miners.add(miner1);
        Main.miners.add(miner2);


        String negativeResult = miner1.makeTransaction(150, miner2.getMinerId());
        String resultWithNonExistingMiner = miner1.makeTransaction(50, 200);
        String correctResult = miner1.makeTransaction(50, miner2.getMinerId());

        assertEquals("error", negativeResult);
        assertEquals("no miner with this id", resultWithNonExistingMiner);
        assertEquals("miner" + miner1.getMinerId() + " sent 50 VC to miner"
                        + miner2.getMinerId(), correctResult);
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