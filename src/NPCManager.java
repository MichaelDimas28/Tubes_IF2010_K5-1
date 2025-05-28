import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class NPCManager {
    GamePanel gp;
    public NPC[] npcs;
    private Map<String, List<String>> npcDialogues = new HashMap<>();
    ItemManager items;

    public NPCManager(GamePanel gp) {
        this.gp = gp;
        npcs = new NPC[6]; // jumlah NPC statis
        setNPCs();
        loadNPCDialogues();
    }

public void loadNPCDialogues() {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(
        getClass().getResourceAsStream("/npc/dialog_npc.txt")))) {

        String line;
        String currentName = null;
        List<String> currentDialogues = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            if (!line.startsWith("\"")) {
                // Simpan dialog sebelumnya
                if (currentName != null && !currentDialogues.isEmpty()) {
                    npcDialogues.put(currentName.trim(), new ArrayList<>(currentDialogues));
                }
                currentName = line.trim();
                currentDialogues.clear();
            } else {
                currentDialogues.add(line.replace("\"", "").trim());
            }
        }

        // Simpan dialog terakhir
        if (currentName != null && !currentDialogues.isEmpty()) {
            npcDialogues.put(currentName.trim(), new ArrayList<>(currentDialogues));
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}


    public void setNPCs() {
        ItemManager itemManager = gp.itemManager; // Pastikan sudah ada

        // Mayor Tadi
        List<Items> mayorLoved = List.of(itemManager.getItem("Legend"));
        List<Items> mayorLiked = List.of(
            itemManager.getItem("Angler"),
            itemManager.getItem("Crimsonfish"),
            itemManager.getItem("Glacierfish")
        );

        List<Items> mayorHated = new ArrayList<>();
        for (Items item : itemManager.getItemMap().values()) {
            if (!mayorLoved.contains(item) && !mayorLiked.contains(item)) {
                mayorHated.add(item);
            }
        }

        NPC mayorTadi = new NPC("Mayor Tadi", Gender.Male, 0,
            npcDialogues.get("Mayor Tadi"), RelationshipStatus.Single, 0, 0, 0,
            mayorLoved, mayorLiked, mayorHated);
        mayorTadi.gp = gp;
        mayorTadi.idle = mayorTadi.setup("mayortadi32.png", gp.tileSize, gp.tileSize);
        mayorTadi.worldX = gp.tileSize * 15;
        mayorTadi.worldY = gp.tileSize * 15;
        npcs[0] = mayorTadi;
        mayorTadi.setDefaultSolidArea();

        // Caroline
        List<Items> caroLoved = List.of(
            itemManager.getItem("Coal"),
            itemManager.getItem("Firewood")
        );
        List<Items> caroLiked = List.of(
            itemManager.getItem("Potato"),
            itemManager.getItem("Wheat")
        );
        List<Items> caroHated = List.of(itemManager.getItem("Hot Pepper"));

        NPC caroline = new NPC("Caroline", Gender.Female, 0,
            npcDialogues.get("Caroline"), RelationshipStatus.Single, 0, 0, 0,
            caroLoved, caroLiked, caroHated);
        caroline.gp = gp;
        caroline.idle = caroline.setup("caroline32.png", gp.tileSize, gp.tileSize);
        caroline.worldX = gp.tileSize * 16;
        caroline.worldY = gp.tileSize * 15;
        npcs[1] = caroline;
        caroline.setDefaultSolidArea();
        
        // Perry
        List<Items> perryLoved = List.of(
            itemManager.getItem("Cranberry"),
            itemManager.getItem("Blueberry")
        );
        List<Items> perryLiked = List.of(
            itemManager.getItem("Wine")
        );
        List<Items> perryHated = new ArrayList<>();
        for (Items item : itemManager.getItemMap().values()) {
            if (item instanceof Fish) {
                perryHated.add(item);
            }
        }

        NPC perry = new NPC("Perry", Gender.Male, 0,
            npcDialogues.get("Perry"), RelationshipStatus.Single, 0, 0, 0,
            perryLoved, perryLiked, perryHated);
        perry.gp = gp;
        perry.idle = perry.setup("perry32.png", gp.tileSize, gp.tileSize);
        perry.worldX = gp.tileSize * 17;
        perry.worldY = gp.tileSize * 15;
        npcs[2] = perry;
        perry.setDefaultSolidArea();

        // Dasco
        List<Items> dascoLoved = List.of(
            itemManager.getItem("The Legends of Spakbor"),
            itemManager.getItem("Cooked Pig's Head"),
            itemManager.getItem("Wine"),
            itemManager.getItem("Fugu"),
            itemManager.getItem("Spakbor Salad")
        );
        List<Items> dascoLiked = List.of(
            itemManager.getItem("Fish Sandwich"),
            itemManager.getItem("Fish Stew"),
            itemManager.getItem("Baguette"),
            itemManager.getItem("Fish n' Chips")
        );
        List<Items> dascoHated = List.of(
            itemManager.getItem("Legend"),
            itemManager.getItem("Grape"),
            itemManager.getItem("Cauliflower"),
            itemManager.getItem("Wheat"),
            itemManager.getItem("Pufferfish"),
            itemManager.getItem("Salmon")
        );

        NPC dasco = new NPC("Dasco", Gender.Male, 0,
            npcDialogues.get("Dasco"), RelationshipStatus.Single, 0, 0, 0,
            dascoLoved, dascoLiked, dascoHated);
        dasco.gp = gp;
        dasco.idle = dasco.setup("dasco32.png", gp.tileSize, gp.tileSize);
        dasco.worldX = gp.tileSize * 18;
        dasco.worldY = gp.tileSize * 15;
        npcs[3] = dasco;
        dasco.setDefaultSolidArea();

        // Emily
        List<Items> emilyLoved = new ArrayList<>();
        for (Items item: itemManager.getItemMap().values()) {
            if (item instanceof Seeds) {
                emilyLoved.add(item);
            }
        }
        List<Items> emilyLiked = List.of(
            itemManager.getItem("Catfish"),
            itemManager.getItem("Salmon"),
            itemManager.getItem("Sardine")
        );
        List<Items> emilyHated = List.of(
            itemManager.getItem("Coal"),
            itemManager.getItem("Firewood")
        );
    
        NPC emily = new NPC("Emily", Gender.Male, 0,
            npcDialogues.get("Emily"), RelationshipStatus.Single, 0, 0, 0,
            emilyLoved, emilyLiked, emilyHated);
        emily.gp = gp;
        emily.idle = emily.setup("emily32.png", gp.tileSize, gp.tileSize);
        emily.worldX = gp.tileSize * 19;
        emily.worldY = gp.tileSize * 15;
        npcs[4] = emily;
        emily.setDefaultSolidArea();

        // Abigail
        List<Items> abigailLoved = List.of(
            itemManager.getItem("Blueberry"),
            itemManager.getItem("Melon"),
            itemManager.getItem("Pumpkin"),
            itemManager.getItem("Grape"),
            itemManager.getItem("Cranberry")
        );
        List<Items> abigailLiked = List.of(
            itemManager.getItem("Baguette"),
            itemManager.getItem("Pumpkin Pie"),
            itemManager.getItem("Wine")
        );
        List<Items> abigailHated = List.of(
            itemManager.getItem("Hot Pepper"),
            itemManager.getItem("Cauliflower"),
            itemManager.getItem("Parsnip"),
            itemManager.getItem("Wheat")
        );
    
        NPC abigail = new NPC("Abigail", Gender.Male, 0,
            npcDialogues.get("Abigail"), RelationshipStatus.Single, 0, 0, 0,
            abigailLoved, abigailLiked, abigailHated);
        abigail.gp = gp;
        abigail.idle = abigail.setup("abigail32.png", gp.tileSize, gp.tileSize);
        abigail.worldX = gp.tileSize * 20;
        abigail.worldY = gp.tileSize * 15;
        npcs[5] = abigail;
        abigail.setDefaultSolidArea();
    }
    
    
    public void draw(Graphics2D g2) {
        for (NPC npc : npcs) {
            if (npc != null) {
                int screenX = npc.worldX - gp.player.worldX + gp.player.screenX;
                int screenY = npc.worldY - gp.player.worldY + gp.player.screenY;

                g2.drawImage(npc.idle, screenX, screenY+10, gp.tileSize, gp.tileSize*175/100, null);
            }
        }
    }

    // public Collection<Items> getAllItems() {
    //     return gp.itemManager.getItemMap().values();
    // }

}
