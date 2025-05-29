import java.util.List;

import javax.imageio.ImageIO;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NPC {
    GamePanel gp;
    public int worldX, worldY;
    public int speed;
    public BufferedImage idle;
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int mapIndex;

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
    // private static List<NPC> listOfNPC = new ArrayList<>();

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
        // listOfNPC.add(this);
    }

    public void getNPCImage() {
        idle = setup("abigail32.png", gp.tileSize, gp.tileSize);
    }

    public BufferedImage setup(String imageName, int width, int height) {
        UtilityTool uTool = new UtilityTool();

        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/npc/"+imageName));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
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
            setHeartPoints(getHeartPoints() + 20);
        } else if (likedItems.contains(item)) {
            setHeartPoints(getHeartPoints() + 20);
        } else if (hatedItems.contains(item)) {
            setHeartPoints(getHeartPoints() - 25);
        } 
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }

    public void chat() {
        // Implement chat logic here
    }

    public List<Items> getLovedItems() {
        return lovedItems;
    }
    public List<Items> getLikedItems() {
        return likedItems;
    }
    public List<Items> getHatedItems() {
        return hatedItems;
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
    
    public void setFreqChat(int freq) {
        freqChat = freq;
    }
    
    public void setFreqGift(int freq) {
        freqGift = freq;
    }
    
    public void setFreqVisit(int freq) {
        freqVisit = freq;
    }
    public void setRelationshipStatus(RelationshipStatus relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public void setDefaultSolidArea() {
        solidArea = new Rectangle(0, gp.tileSize, gp.tileSize, gp.tileSize); 
    }
    // public static List<NPC> getListOfNPC() {
    //     return listOfNPC;
    // }

    public RelationshipStatus getRelationshipStatus() {
        return relationshipStatus;
    }

}

