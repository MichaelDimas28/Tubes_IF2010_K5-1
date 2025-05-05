import java.util.List;

public class NPC {
    private String name;
    private Gender gender;
    private int heartPoints;
    private List<String> dialogues;
    private RelationshipStatus relationshipStatus;
    private int freqChat;
    private int freqGift;
    private int freqVisit;
    private List<Items> lovedItems;
    private List<Items> likedItems;
    private List<Items> hatedItems;
    public static final int MAX_HEART_POINTS = 150;

    public NPC(String name, Gender gender, int heartPoints, List<String> dialogues, RelationshipStatus relationshipStatus, int freqChat, int freqGift, int freqVisit, List<Items> lovedItems, List<Items> likedItems, List<Items> hatedItems) {
        this.name = name;
        this.gender = gender;
        this.heartPoints = heartPoints;
        this.dialogues = dialogues;
        this.relationshipStatus = relationshipStatus;
        this.freqChat = freqChat;
        this.freqGift = freqGift;
        this.freqVisit = freqVisit;
        this.lovedItems = lovedItems;
        this.likedItems = likedItems;
        this.hatedItems = hatedItems;
    }

    public String getName() {
        return name;
    }

    public int getHeartPoints() {
        return heartPoints;
    }

    public void setHeartPoints(int heartPoints) {
        if (heartPoints > MAX_HEART_POINTS) {
            heartPoints = MAX_HEART_POINTS;
        }
        this.heartPoints = heartPoints;
    }

    public void receiveGift(Items item) {
        if (lovedItems.contains(item)) {
            setHeartPoints(getHeartPoints() + 10);
        } else if (likedItems.contains(item)) {
            setHeartPoints(getHeartPoints() + 5);
        } else if (hatedItems.contains(item)) {
            setHeartPoints(getHeartPoints() - 10);
        } 
    }

    public void chat() {
        // Implement chat logic here
    }

    public int getFreqChat() {
        return freqChat;
    }

    public int getFreqGift() {
        return freqGift;
    }

    public int getFreqVisit() {
        return freqVisit;
    }

    public void setRelationshipStatus(RelationshipStatus relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }
}

