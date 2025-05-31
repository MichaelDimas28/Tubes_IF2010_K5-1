import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

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
        if (gp.ui.fishingActive) {
            if (Character.isDigit(e.getKeyChar())) {
                gp.ui.fishingInput += e.getKeyChar();
            }
        }
        
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        if (gp.ui.fishingActive) {
            if (code == KeyEvent.VK_BACK_SPACE && gp.ui.fishingInput.length() > 0) {
                gp.ui.fishingInput = gp.ui.fishingInput.substring(0, gp.ui.fishingInput.length() - 1);
            } else if (code == KeyEvent.VK_ENTER) {
                if (!gp.ui.fishingInput.isEmpty()) {
                    int guess = Integer.parseInt(gp.ui.fishingInput);
                    gp.ui.fishingTriesLeft--;
    
                    if (guess == gp.ui.fishingTarget) {
                        gp.ui.showMessage("Kamu menangkap: " + gp.ui.currentFishingFish.getItemName());
                        gp.player.getInventory().addItem(new InventoryItem(gp.itemManager.getItem(gp.ui.currentFishingFish.getItemName()), 1));
                        gp.ui.fishingActive = false;
                        gp.gamePaused = false;
                    } else if (gp.ui.fishingTriesLeft <= 0) {
                        gp.ui.showMessage("Ikan berhasil lolos...");
                        gp.ui.fishingActive = false;
                        gp.gamePaused = false;
                    } else {
                        gp.ui.showMessage("Tebakan salah! Coba lagi.");
                    }
                    gp.ui.fishingInput = "";
                }
            } else if (code == KeyEvent.VK_ESCAPE) {
                gp.ui.fishingActive = false;
                gp.gamePaused = false;
                gp.ui.showMessage("Memancing dibatalkan.");
            }
            return; // blokir input lain
        }
        
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
                    if (gp.ui.currentEmily.getHeartPoints() == 0) {
                        gp.ui.currentDialogue = gp.ui.npcDialogues.get("Emily").get(0);
                    } else {
                        gp.ui.currentDialogue = gp.player.getNPCDialog(gp.ui.currentEmily, null);
                    }
                    if (!gp.ui.currentEmily.getHasTalked()) {
                        gp.ui.currentEmily.setHasTalked(true);
                        gp.ui.currentEmily.setHeartPoints(gp.ui.currentEmily.getHeartPoints()+10);
                        gp.ui.currentEmily.setFreqChat(gp.ui.currentEmily.getFreqChat()+1);
                    }
                } else if (gp.ui.emilyMenuSelection == 1) { // Buy
                    // gp.dialogueOn = true;
                    // gp.ui.currentDialogue = gp.ui.npcDialogues.get("Emily").get(11); // dialog index 8
                    gp.ui.emilyStoreActive = true;
                    gp.dialogueOn = false;
                    gp.ui.slotRow = 0;
                    gp.ui.slotCol = 0;
                }
                gp.ui.emilyMenuActive = false; // tutup menu
            }

            return; // abaikan input lain saat menu Emily aktif
        }

        if (gp.ui.emilyStoreActive) {
            if (gp.ui.enteringQuantity) {
                if (Character.isDigit(e.getKeyChar())) {
                    gp.ui.quantityInput += e.getKeyChar();
                } else if (code == KeyEvent.VK_BACK_SPACE && gp.ui.quantityInput.length() > 0) {
                    gp.ui.quantityInput = gp.ui.quantityInput.substring(0, gp.ui.quantityInput.length() - 1);
                } else if (code == KeyEvent.VK_ENTER) {
                    try {
                        int qty = Integer.parseInt(gp.ui.quantityInput);
                        gp.ui.buyQuantity = Math.max(1, qty);
                        gp.ui.enteringQuantity = false;
                        gp.ui.confirmingPurchase = true;
                        gp.ui.confirmYes = true;
                    } catch (NumberFormatException ex) {
                        gp.ui.quantityInput = "";
                    }
                }
                return;
            } else if (gp.ui.confirmingPurchase) {
                if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT || code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                    gp.ui.confirmYes = !gp.ui.confirmYes;
                } else if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.confirmYes && Integer.parseInt(gp.ui.quantityInput)>0) {
                        gp.ui.processPurchase();
                    } else {
                        gp.ui.confirmingPurchase = false;
                        gp.ui.enteringQuantity = false;
                    }
                }
                return;
            }




            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.emilyStoreRow--;
                if (gp.ui.emilyStoreRow < 0) gp.ui.emilyStoreRow = 0;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.emilyStoreRow++;
                if (gp.ui.emilyStoreRow > 4) gp.ui.emilyStoreRow = 4;
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                gp.ui.emilyStoreCol--;
                if (gp.ui.emilyStoreCol < 0) gp.ui.emilyStoreCol = 0;
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                gp.ui.emilyStoreCol++;
                if (gp.ui.emilyStoreCol > 12) gp.ui.emilyStoreCol = 12;
            }

            int storeSize = gp.store.getCurrentSeasonItems().size();
            int index = gp.ui.emilyStoreRow * 13 + gp.ui.emilyStoreCol;
            if (index >= storeSize) {
                gp.ui.emilyStoreRow = (storeSize - 1) / 13;
                gp.ui.emilyStoreCol = (storeSize - 1) % 13;
            }

            if (code == KeyEvent.VK_ENTER) {
                gp.ui.enteringQuantity = true;
                gp.ui.quantityInput = "";
            } else if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_SPACE) {
                gp.ui.emilyStoreActive = false;
            }
            return;
        }


        if (gp.binOpen) {
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

            // CEGAH KARAKTER BERGERAK SAAT SHIPPING BIN TERBUKA

            int totalItems = gp.player.getInventory().totalItems();
            int maxIndex = totalItems - 1;
            int currentIndex = gp.ui.slotRow * 13 + gp.ui.slotCol;
            if (currentIndex > maxIndex) {
                gp.ui.slotRow = maxIndex / 13;
                gp.ui.slotCol = maxIndex % 13;
            }

            // PENGIRIMAN ITEM KE SHIPPING BIN
            if (code == KeyEvent.VK_ENTER) {
                int index = gp.ui.slotRow * 13 + gp.ui.slotCol;
                if (index < gp.player.getInventory().totalItems()) {
                    InventoryItem selectedItem = gp.player.getInventory().getItems().get(index);
                    System.out.println("Selected item: " + selectedItem.getItem().getItemName() +
                   ", Quantity: " + selectedItem.getQuantity() +
                   ", SellPrice: " + selectedItem.getItem().getSellPrice());

                    if (selectedItem != null && selectedItem.getQuantity() > 0) {

                        // CEK HARGA JUAL ITEM (TIDAK BISA DIKIRIM JIKA 0)
                        if (selectedItem.getItem().getSellPrice() <= 0) {
                            gp.ui.showMessage(selectedItem.getItem().getItemName()+" ini tidak bisa dijual!");
                        }

                        InventoryItem itemToShip = new InventoryItem(selectedItem.getItem(), 1);
                        boolean added = gp.farm.getShippingBin().addItem(itemToShip);
                        if (added) {
                            selectedItem.setQuantity(selectedItem.getQuantity()-1);
                            if (selectedItem.getQuantity() == 0) {
                                gp.player.getInventory().removeItem(selectedItem);
                                totalItems = gp.player.getInventory().totalItems();
                                maxIndex = totalItems - 1;
                                if (maxIndex < 0) {
                                    gp.ui.slotRow = 0;
                                    gp.ui.slotCol = 0;
                                } else {
                                    gp.ui.slotRow = maxIndex / 13;
                                    gp.ui.slotCol = maxIndex % 13;
                                }
                            }
                        } else if (selectedItem.getItem().getSellPrice() <= 0) {
                            gp.ui.showMessage(selectedItem.getItem().getItemName()+" tidak bisa dijual!");
                        } else {
                            gp.ui.showMessage("Shipping Bin penuh!");
                        }
                    }
                }
            }
            if (code == KeyEvent.VK_SPACE) {
                gp.binOpen = false;
            }
            return;
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
            String tileName = gp.tileM.getFrontTile();
            if (npcIndex != -1) {
                gp.player.interactWithNPC(gp.npcManager.getCurrentMapNPCs().get(npcIndex));
            }
            if (tileName != null && Arrays.asList("222.png","211.png","200.png","233.png","244.png","255.png").contains(tileName)) {
                gp.binOpen = true;
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