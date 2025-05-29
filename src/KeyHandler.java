import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
    GamePanel gp;
    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;
    public boolean pPressed;
    public boolean enterPressed;
    public boolean spacePressed;
    public boolean iPressed;

    public KeyHandler (GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        if (gp.ui.emilyMenuActive) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.emilyMenuSelection--;
                if (gp.ui.emilyMenuSelection < 0) gp.ui.emilyMenuSelection = 1;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.emilyMenuSelection++;
                if (gp.ui.emilyMenuSelection > 1) gp.ui.emilyMenuSelection = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.emilyMenuSelection == 0) { // Talk
                    gp.dialogueOn = true;
                    gp.ui.currentDialogue = gp.player.getNPCDialog(gp.ui.currentEmily, null);
                } else if (gp.ui.emilyMenuSelection == 1) { // Buy
                    gp.dialogueOn = true;
                    gp.ui.currentDialogue = gp.ui.npcDialogues.get("Emily").get(8); // dialog index 8
                }
                gp.ui.emilyMenuActive = false; // tutup menu
            }

            return; // abaikan input lain saat menu Emily aktif
        }

        if (gp.inventoryOpen) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.slotRow--;
                if (gp.ui.slotRow < 0) gp.ui.slotRow = 0;
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                gp.ui.slotCol--;
                if (gp.ui.slotCol < 0) gp.ui.slotCol = 0;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.slotRow++;
                if (gp.ui.slotRow > 4) gp.ui.slotRow = 4;
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                gp.ui.slotCol++;
                if (gp.ui.slotCol > 12) gp.ui.slotCol = 12;
            }

            // >>> CEK dan PERBAIKI posisi jika melebihi jumlah item <<<
            int index = gp.ui.slotRow * 13 + gp.ui.slotCol;
            if (index >= gp.player.getInventory().totalItems()) {
                int lastIndex = gp.player.getInventory().totalItems() - 1;
                gp.ui.slotRow = lastIndex / 13;
                gp.ui.slotCol = lastIndex % 13;
            }

            if (code == KeyEvent.VK_ENTER) {
                int indexInv = gp.ui.slotRow*13 + gp.ui.slotCol;
                // Jika sudah pegang item, Enter akan melepaskan itemHeld
                if (gp.player.getItemHeld() != null && gp.player.getItemHeld().equals(gp.player.getInventory().getItems().get(indexInv).getItem())) {
                    gp.player.setItemHeld(null);
                    return; // Keluar agar tidak lanjut ambil item baru
                }
                if (indexInv < gp.player.getInventory().totalItems()) {
                    gp.player.setItemHeld(gp.player.getInventory().getItems().get(indexInv).getItem());
                }
            }
        }

        if (gp.dialogueOn && spacePressed) {
            gp.dialogueOn = false;
            gp.ui.currentDialogue = "";
            spacePressed = false;
        }
        
        if (code == KeyEvent.VK_SPACE) {
            int npcIndex = gp.collisionChecker.checkNPC(gp.player, gp.npcManager.getCurrentMapNPCs());
            if (npcIndex != -1) {
                gp.player.interactWithNPC(gp.npcManager.getCurrentMapNPCs().get(npcIndex));
            }
            spacePressed = true;
        }

        if (code == KeyEvent.VK_P) {
            pPressed = true;
        }

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_I) {
            iPressed = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
    }
}