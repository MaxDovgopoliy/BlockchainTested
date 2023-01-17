import java.util.List;

public class Data {
    private int identifier;
    private int publicKey;
    private int signature;
    private List<String> massages;
    private int minerID;

    public Data(int minerID) {
        this.minerID = minerID;
    }

    public int getPublicKey() {
        return publicKey;
    }

    public int getSignature() {
        return signature;
    }

    public List<String> getMassages() {
        return massages;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public void setMassages(List<String> massages) {
        this.massages = massages;
    }

    public int getIdentifier() {
        return identifier;
    }

    public int getMinerID() {
        return minerID;
    }

    public void createSignature(int identifier) {
        this.signature = identifier + minerID;
        this.publicKey = identifier * 11;
    }
}
