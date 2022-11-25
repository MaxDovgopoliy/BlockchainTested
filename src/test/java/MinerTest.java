import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MinerTest {


    @Test
    void makeTransaction() {
        Blockchain blockchain = new Blockchain();
        Miner miner1 = new Miner(blockchain);
        Miner miner2 = new Miner(blockchain);
        Main.miners.add(miner1);
        Main.miners.add(miner2);


        String negativeResult = miner1.makeTransaction(150, 2);
        String resultWithNonExistingMiner = miner1.makeTransaction(50, 200);
        String correctResult = miner1.makeTransaction(50, 2);

        assertEquals("error",negativeResult);
        assertEquals("no miner with this id",resultWithNonExistingMiner);
        assertEquals("miner1 sent 50 VC to miner2",correctResult);
    }
}