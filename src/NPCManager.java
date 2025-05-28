import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class NPCManager {
    GamePanel gp;
    public NPC[] npcs;

    public NPCManager(GamePanel gp) {
        this.gp = gp;
        npcs = new NPC[6]; // jumlah NPC statis
        setNPCs();
    }

    public void setNPCs() {
        // Contoh NPC 1
        List<String> dialogue1 = new ArrayList<>();
        // dialogue1.add("Halo, aku Abigail!");
        List<Items> loved = new ArrayList<>();
        List<Items> liked = new ArrayList<>();
        List<Items> hated = new ArrayList<>();

        NPC npc1 = new NPC("Abigail", Gender.Female, 0, dialogue1, RelationshipStatus.Stranger, 0, 0, 0, empty, empty, empty);
        npc1.gp = gp; // penting agar getNPCImage bisa pakai gp
        npc1.getNPCImage();
        npc1.worldX = gp.tileSize * 10;
        npc1.worldY = gp.tileSize * 10;
        npcs[0] = npc1;

        // Tambahkan NPC lainnya dengan cara yang sama...
    }

    public void draw(Graphics2D g2) {
        for (NPC npc : npcs) {
            if (npc != null) {
                int screenX = npc.worldX - gp.player.worldX + gp.player.screenX;
                int screenY = npc.worldY - gp.player.worldY + gp.player.screenY;

                g2.drawImage(npc.idle, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    }
}
