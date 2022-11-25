import java.io.Serializable;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Date;
import java.util.Random;

public class Block implements Serializable {

    private int id;
    private int minerId;
    private long timeStamp;
    private String previousBlockHash;
    private String hash;
    Data data;
    private int magicNumber;
    private Duration timeOfCreation;

    public Block(String previousBlockHash, int minerId, int id, Data data) {
        this.timeStamp = new Date().getTime();
        this.magicNumber = new Random().nextInt();
        this.hash = generateSHA256(timeStamp + magicNumber);
        this.id = id;
        if (id == 1) {
            this.previousBlockHash = "0";
        } else {
            this.previousBlockHash = previousBlockHash;
        }
        this.minerId = minerId;
        this.data = data;
    }

    private static String generateSHA256(Object input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.toString().getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem : hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getHash() {
        return hash;
    }

    public int getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public Duration getTimeOfCreation() {
        return timeOfCreation;
    }


    public int getMinerId() {
        return minerId;
    }

    public String getMassage() {
        return data.getMassages().toString();
    }

    public void setTimeOfCreation(Duration timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }
}
