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
    public List<NPC>[] npcMapList;
    private Map<String, List<String>> npcDialogues = new HashMap<>();
    ItemManager items;

    @SuppressWarnings("unchecked")
    public NPCManager(GamePanel gp) {
        this.gp = gp;
        npcs = new NPC[11]; // jumlah NPC
        npcMapList = new ArrayList[gp.maxMap];
        for (int i = 0; i < gp.maxMap ; i++) {
            npcMapList[i] = new ArrayList<>();
        }
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
        mayorTadi.worldX = gp.tileSize * 5;
        mayorTadi.worldY = gp.tileSize * 4;
        npcs[0] = mayorTadi;
        mayorTadi.setMapIndex(1);
        npcMapList[1].add(mayorTadi);
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
        caroline.worldX = gp.tileSize * 4;
        caroline.worldY = gp.tileSize * 3;
        npcs[1] = caroline;
        caroline.setMapIndex(2);
        npcMapList[2].add(caroline);
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
        perry.worldX = gp.tileSize *11;
        perry.worldY = gp.tileSize * 2;
        npcs[2] = perry;
        perry.setMapIndex(3);
        npcMapList[3].add(perry);
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
        dasco.worldX = gp.tileSize * 5;
        dasco.worldY = gp.tileSize * 1;
        npcs[3] = dasco;
        dasco.setDefaultSolidArea();
        dasco.setMapIndex(4);
        npcMapList[4].add(dasco);

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
    
        NPC emily = new NPC("Emily", Gender.Female, 0,
            npcDialogues.get("Emily"), RelationshipStatus.Single, 0, 0, 0,
            emilyLoved, emilyLiked, emilyHated);
        emily.gp = gp;
        emily.idle = emily.setup("emily32.png", gp.tileSize, gp.tileSize);
        emily.worldX = gp.tileSize * 6;
        emily.worldY = gp.tileSize * 6;
        npcs[4] = emily;
        emily.setMapIndex(6);
        npcMapList[6].add(emily);
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
    
        NPC abigail = new NPC("Abigail", Gender.Female, 0,
            npcDialogues.get("Abigail"), RelationshipStatus.Single, 0, 0, 0,
            abigailLoved, abigailLiked, abigailHated);
        abigail.gp = gp;
        abigail.idle = abigail.setup("abigail32.png", gp.tileSize, gp.tileSize);
        abigail.worldX = gp.tileSize * 3;
        abigail.worldY = gp.tileSize * 3;
        npcs[5] = abigail;
        abigail.setMapIndex(5);
        npcMapList[5].add(abigail);
        abigail.setDefaultSolidArea();

        // Ar
        List<Items> arLoved = List.of(
            itemManager.getItem("Fish n' Chips")
        );
        List<Items> arLiked = List.of(
            itemManager.getItem("Potato")
        );
        List<Items> arHated = List.of(
            itemManager.getItem("The Legends of Spakbor")
        );
    
        NPC ar = new NPC("Ar", Gender.Male, 0,
            npcDialogues.get("Ar"), RelationshipStatus.Single, 0, 0, 0,
            arLoved, arLiked, arHated);
        ar.gp = gp;
        ar.idle = ar.setup("ar.png", gp.tileSize, gp.tileSize);
        ar.worldX = gp.tileSize * 8;
        ar.worldY = gp.tileSize * 5;
        npcs[6] = ar;
        ar.setMapIndex(12);
        npcMapList[12].add(ar);
        ar.setDefaultSolidArea();

        // Flo
        List<Items> floLoved = List.of(
            itemManager.getItem("Baguette"),
            itemManager.getItem("Fish n' Chips"),
            itemManager.getItem("Cranberry")
        );
        List<Items> floLiked = List.of(
            itemManager.getItem("Wheat"),
            itemManager.getItem("Fish Sandwich"),
            itemManager.getItem("Sashimi")
        );
        List<Items> floHated = List.of(
            itemManager.getItem("Veggie Soup"),
            itemManager.getItem("Cauliflower"),
            itemManager.getItem("Spakbor Salad")
        );
    
        NPC flo = new NPC("Flo", Gender.Female, 0,
            npcDialogues.get("Flo"), RelationshipStatus.Single, 0, 0, 0,
            floLoved, floLiked, floHated);
        flo.gp = gp;
        flo.idle = flo.setup("flo.png", gp.tileSize, gp.tileSize);
        flo.worldX = gp.tileSize * 7;
        flo.worldY = gp.tileSize * 11;
        npcs[7] = flo;
        flo.setMapIndex(12);
        npcMapList[12].add(flo);
        flo.setDefaultSolidArea();
        
        // Mas
        List<Items> masLoved = List.of(
            itemManager.getItem("Tomato"),
            itemManager.getItem("Bullhead"),
            itemManager.getItem("Salmon")
        );
        List<Items> masLiked = List.of(
            itemManager.getItem("Glacierfish"),
            itemManager.getItem("Melon"),
            itemManager.getItem("Baguette")
        );
        List<Items> masHated = List.of(
            itemManager.getItem("Cooked Pig's Head"),
            itemManager.getItem("Pumpkin"),
            itemManager.getItem("Catfish")
        );
    
        NPC mas = new NPC("Mas", Gender.Male, 0,
            npcDialogues.get("Mas"), RelationshipStatus.Single, 0, 0, 0,
            masLoved, masLiked, masHated);
        mas.gp = gp;
        mas.idle = mas.setup("mas.png", gp.tileSize, gp.tileSize);
        mas.worldX = gp.tileSize * 16;
        mas.worldY = gp.tileSize * 10;
        npcs[8] = mas;
        mas.setMapIndex(12);
        npcMapList[12].add(mas);
        mas.setDefaultSolidArea();
        
        // Rei
        List<Items> reiLoved = List.of(
            itemManager.getItem("Salmon")
        );
        List<Items> reiLiked = List.of(
            itemManager.getItem("Catfish"),
            itemManager.getItem("Pufferfish")
        );
        List<Items> reiHated = List.of(
            itemManager.getItem("Parsnip"),
            itemManager.getItem("Cauliflower"),
            itemManager.getItem("Hot Pepper")
        );
    
        NPC rei = new NPC("Rei", Gender.Male, 0,
            npcDialogues.get("Rei"), RelationshipStatus.Single, 0, 0, 0,
            reiLoved, reiLiked, reiHated);
        rei.gp = gp;
        rei.idle = rei.setup("rei.png", gp.tileSize, gp.tileSize);
        rei.worldX = gp.tileSize * 19;
        rei.worldY = gp.tileSize * 9;
        npcs[9] = rei;
        rei.setMapIndex(12);
        npcMapList[12].add(rei);
        rei.setDefaultSolidArea();
       
        // Fav
        List<Items> favLoved = List.of(
            itemManager.getItem("Salmon"),
            itemManager.getItem("Catfish"),
            itemManager.getItem("Fish n' Chips"),
            itemManager.getItem("Sashimi"),
            itemManager.getItem("Blueberry")
        );
        List<Items> favLiked = List.of(
            itemManager.getItem("Potato"),
            itemManager.getItem("Sardine"),
            itemManager.getItem("Cranberry"),
            itemManager.getItem("Grape")
        );
        List<Items> favHated = List.of(
            itemManager.getItem("Parsnip"),
            itemManager.getItem("Wine"),
            itemManager.getItem("Cooked Pig's Head")
        );
    
        NPC fav = new NPC("Fav", Gender.Male, 0,
            npcDialogues.get("Fav"), RelationshipStatus.Single, 0, 0, 0,
            favLoved, favLiked, favHated);
        fav.gp = gp;
        fav.idle = fav.setup("fav.png", gp.tileSize, gp.tileSize);
        fav.worldX = gp.tileSize * 18;
        fav.worldY = gp.tileSize * 18;
        npcs[10] = fav;
        fav.setMapIndex(12);
        npcMapList[12].add(fav);
        fav.setDefaultSolidArea();
    }
    
    
    public void draw(Graphics2D g2) {
        List<NPC> currentMapNPCs = npcMapList[gp.currentMap];
        for (NPC npc : currentMapNPCs) {
            if (npc != null) {
                int screenX = npc.worldX - gp.player.worldX + gp.player.screenX;
                int screenY = npc.worldY - gp.player.worldY + gp.player.screenY;
                
                g2.drawImage(npc.idle, screenX, screenY+10, gp.tileSize, gp.tileSize*175/100, null);
            }
        }
    }
    
    public List<NPC> getCurrentMapNPCs() {
        List<NPC> currentMapNPCs = npcMapList[gp.currentMap];
        return currentMapNPCs;
    }

    // public Collection<Items> getAllItems() {
    //     return gp.itemManager.getItemMap().values();
    // }

}
