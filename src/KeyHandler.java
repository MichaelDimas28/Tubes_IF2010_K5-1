import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        // 1. Handle PAUSE (Prioritas tertinggi)
        if (code == KeyEvent.VK_P) {
            pPressed = true; // Akan di-toggle di GamePanel.update()
            return; 
        }
        if (gp.gamePaused) {
            return; // Abaikan input lain jika game di-pause
        }

        // --- AWAL PENAMBAHAN LOGIKA UNTUK COOKING ---
        // 2. Handle PEMBUKAAN MENU MASAK via interaksi Tombol SPACE (bukan 'C' lagi)
        // Hanya jika tidak ada menu UI lain yang lebih prioritas sedang aktif
        if (code == KeyEvent.VK_SPACE && 
            !gp.cookingMenuActive && !gp.inventoryOpen && !gp.binOpen && !gp.dialogueOn &&
            !gp.ui.emilyMenuActive && !gp.ui.emilyStoreActive) {

            // Cek interaksi dengan Kompor (Stove)
            // Map rumah pemain adalah 11 (Player House utama) atau 12 (Player House dari daftar NPC)
            if (gp.currentMap == 11 || gp.currentMap == 12) {  //
                int tileIDInFront = gp.tileM.getFrontTileID(); // Asumsi metode ini ada dan mengembalikan ID integer
                int playerTileX = gp.player.worldX / gp.tileSize;
                int playerTileY = gp.player.worldY / gp.tileSize;

                // Kompor (tile ID 467) di player_house.txt baris 6, kolom 3 (index 0-based: y=5, x=2).
                // Pemain harus di baris 7, kolom 3 (index 0-based: y=6, x=2) dan menghadap "up".
                boolean playerAtCorrectCookingSpot = (playerTileX == 2 && playerTileY == 6);
                
                if (playerAtCorrectCookingSpot && gp.player.direction.equals("up") && tileIDInFront == 467) {
                    if (gp.player.isCooking) { // Jika sudah ada proses masak pasif
                        gp.ui.showMessage("Kamu sedang memasak: " + gp.player.getRecipeBeingCooked().getItemName());
                    } else {
                        gp.cookingMenuActive = true; // Aktifkan state menu masak
                        // Nonaktifkan state UI lain yang mungkin tidak sengaja aktif
                        gp.inventoryOpen = false;
                        gp.binOpen = false;
                        gp.dialogueOn = false;
                        gp.ui.emilyMenuActive = false;
                        gp.ui.emilyStoreActive = false;

                        gp.ui.availableRecipes.clear();
                        for (Items item : gp.itemManager.getItemMap().values()) {
                            if (item instanceof Food) {
                                Food food = (Food) item;
                                if (food.isRecipeAcquired()) { // Hanya resep yang sudah didapat
                                    gp.ui.availableRecipes.add(food);
                                }
                            }
                        }
                        gp.ui.cookingMenuSelection = 0;
                        gp.ui.showRecipeDetails = false;
                        gp.ui.selectedRecipeForDetail = null;
                    }
                    // Setelah interaksi kompor (baik membuka menu atau menampilkan pesan),
                    // kita anggap input SPACE sudah tertangani untuk frame ini.
                    // Tidak langsung 'return' karena SPACE mungkin masih perlu di-flag untuk Player.update().
                    // Namun, karena kita sudah masuk menu masak, idealnya input gameplay lain tidak berjalan.
                    // Jadi, setelah ini akan dicek `if (gp.cookingMenuActive)`
                }
            }
        }


        // 3. Jika MENU MASAK AKTIF, proses input untuk menu tersebut
        if (gp.cookingMenuActive) {
            handleCookingMenuInput(code); // Panggil metode terpisah untuk menangani input menu masak
            return; // Input sudah ditangani oleh menu masak
        }
        // --- AKHIR PENAMBAHAN LOGIKA UNTUK COOKING ---


        // --- KODE ASLI ANDA UNTUK MENU LAIN DAN GAMEPLAY ---
        // (Dengan sedikit penyesuaian agar tidak konflik dengan menu masak)

        if (gp.ui.emilyMenuActive) { // Emily Menu (jika tidak ada menu masak aktif)
            // ... (kode Emily Menu Anda yang sudah ada) ...
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.emilyMenuSelection--;
                if (gp.ui.emilyMenuSelection < 0) gp.ui.emilyMenuSelection = 1;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.emilyMenuSelection++;
                if (gp.ui.emilyMenuSelection > 1) gp.ui.emilyMenuSelection = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                // Saat masuk ke Emily (Talk/Buy), pastikan menu lain nonaktif
                gp.inventoryOpen = false; gp.binOpen = false; gp.dialogueOn = false; // gp.cookingMenuActive sudah false

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
                    gp.ui.emilyStoreActive = true;
                    // gp.dialogueOn = false; // Sudah di atas
                    gp.ui.slotRow = 0;
                    gp.ui.slotCol = 0;
                }
                gp.ui.emilyMenuActive = false; // tutup menu Emily setelah aksi
            }
             if (code == KeyEvent.VK_ESCAPE) { // Tambahan: ESC untuk menutup menu Emily
                gp.ui.emilyMenuActive = false;
            }
            return; 
        }

        if (gp.ui.emilyStoreActive) { // Emily Store (jika tidak ada menu masak/emily aktif)
            // ... (kode Emily Store Anda yang sudah ada, pastikan validasi dan batasan kursor sudah benar) ...
            // (Pastikan ada 'return;' di akhir blok ini)
            if (gp.ui.enteringQuantity) {
                if (Character.isDigit(e.getKeyChar())) {
                    gp.ui.quantityInput += e.getKeyChar();
                } else if (code == KeyEvent.VK_BACK_SPACE && gp.ui.quantityInput.length() > 0) {
                    gp.ui.quantityInput = gp.ui.quantityInput.substring(0, gp.ui.quantityInput.length() - 1);
                } else if (code == KeyEvent.VK_ENTER) {
                    try {
                        if (gp.ui.quantityInput.isEmpty()){ 
                            gp.ui.showMessage("Masukkan jumlah!");
                            return; 
                        }
                        int qty = Integer.parseInt(gp.ui.quantityInput);
                        if (qty <= 0) { 
                             gp.ui.showMessage("Jumlah harus lebih dari 0!");
                             gp.ui.quantityInput = ""; 
                             return; 
                        }
                        gp.ui.buyQuantity = qty;
                        gp.ui.enteringQuantity = false;
                        gp.ui.confirmingPurchase = true;
                        gp.ui.confirmYes = true;
                    } catch (NumberFormatException ex) {
                        gp.ui.quantityInput = "";
                        gp.ui.showMessage("Jumlah tidak valid!");
                    }
                }
                return;
            } else if (gp.ui.confirmingPurchase) {
                if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT || code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                    gp.ui.confirmYes = !gp.ui.confirmYes;
                } else if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.confirmYes) { 
                         if (gp.ui.quantityInput.isEmpty() || Integer.parseInt(gp.ui.quantityInput) <= 0) {
                             gp.ui.showMessage("Terjadi kesalahan kuantitas.");
                             gp.ui.confirmingPurchase = false;
                             gp.ui.enteringQuantity = true; 
                         } else {
                            gp.ui.processPurchase(); 
                         }
                    } else { 
                        gp.ui.confirmingPurchase = false;
                        gp.ui.enteringQuantity = false; 
                        gp.ui.quantityInput = "";
                    }
                }
                 return;
            }

            // Navigasi di toko Emily
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gp.ui.emilyStoreRow--;
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gp.ui.emilyStoreRow++;
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) gp.ui.emilyStoreCol--;
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) gp.ui.emilyStoreCol++;
    
            List<Items> currentStoreItems = gp.store.getCurrentSeasonItems();
            int storeItemCount = currentStoreItems.size();
            int itemsPerRow = 13; 
    
            if (storeItemCount > 0) {
                int maxRows = (storeItemCount - 1) / itemsPerRow;
                if (gp.ui.emilyStoreRow < 0) gp.ui.emilyStoreRow = 0;
                if (gp.ui.emilyStoreRow > maxRows) gp.ui.emilyStoreRow = maxRows;
                
                int maxColsThisRow = (gp.ui.emilyStoreRow == maxRows) ? (storeItemCount - 1) % itemsPerRow : itemsPerRow - 1;
    
                if (gp.ui.emilyStoreCol < 0) gp.ui.emilyStoreCol = 0;
                if (gp.ui.emilyStoreCol > maxColsThisRow) gp.ui.emilyStoreCol = maxColsThisRow;
            } else { 
                gp.ui.emilyStoreRow = 0;
                gp.ui.emilyStoreCol = 0;
            }
    
            if (code == KeyEvent.VK_ENTER) {
                if(storeItemCount > 0 && gp.ui.emilyStoreRow * itemsPerRow + gp.ui.emilyStoreCol < storeItemCount){ 
                    gp.ui.enteringQuantity = true;
                    gp.ui.quantityInput = "";
                }
            } else if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_SPACE) { // Sesuai kode lama Anda
                gp.ui.emilyStoreActive = false;
                gp.ui.enteringQuantity = false;
                gp.ui.confirmingPurchase = false;
                gp.ui.quantityInput = "";
            }
            return;
        }

        if (gp.binOpen) { // Bin (jika tidak ada menu masak/emily/store aktif)
            // ... (kode Bin Anda yang sudah ada, pastikan validasi dan batasan kursor sudah benar) ...
            // (Pastikan ada 'return;' di akhir blok ini)
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gp.ui.slotRow--;
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gp.ui.slotRow++;
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) gp.ui.slotCol--;
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) gp.ui.slotCol++;
    
            int totalPlayerItems = gp.player.getInventory().totalItems();
            int itemsPerRowInv = 13; 
    
            if (totalPlayerItems > 0) {
                int maxRowsInv = Math.max(0, (totalPlayerItems - 1) / itemsPerRowInv); 
                
                if (gp.ui.slotRow < 0) gp.ui.slotRow = 0;
                if (gp.ui.slotRow > maxRowsInv) gp.ui.slotRow = maxRowsInv;
                
                int maxColsInvThisRow = (gp.ui.slotRow == maxRowsInv) ? 
                                        Math.max(0, (totalPlayerItems - 1) % itemsPerRowInv) : 
                                        itemsPerRowInv - 1;
    
                if (gp.ui.slotCol < 0) gp.ui.slotCol = 0;
                if (gp.ui.slotCol > maxColsInvThisRow) gp.ui.slotCol = maxColsInvThisRow;
            } else {
                gp.ui.slotRow = 0;
                gp.ui.slotCol = 0;
            }
    
            if (code == KeyEvent.VK_ENTER) {
                if (totalPlayerItems > 0) {
                    int index = gp.ui.slotRow * itemsPerRowInv + gp.ui.slotCol;
                    if (index < totalPlayerItems) { 
                        InventoryItem selectedItem = gp.player.getInventory().getItems().get(index);
                        if (selectedItem != null && selectedItem.getQuantity() > 0) {
                            if (selectedItem.getItem().getSellPrice() <= 0) { 
                                gp.ui.showMessage(selectedItem.getItem().getItemName()+" ini tidak bisa dijual!");
                            } else {
                                InventoryItem itemToShip = new InventoryItem(selectedItem.getItem(), 1); 
                                boolean added = gp.farm.getShippingBin().addItem(itemToShip);
                                if (added) {
                                    selectedItem.setQuantity(selectedItem.getQuantity()-1);
                                    if (selectedItem.getQuantity() == 0) {
                                        gp.player.getInventory().removeItem(selectedItem);
                                        // Kursor mungkin perlu dikoreksi lagi
                                    }
                                } else {
                                    gp.ui.showMessage("Shipping Bin penuh atau item tidak bisa dijual!"); 
                                }
                            }
                        }
                    }
                }
            }
            if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_SPACE) { 
                gp.binOpen = false;
            }
            return;
        }
        
        // Handle Inventory (dibuka dengan 'I' atau jika sudah terbuka)
        // (Jika belum ada menu lain yang lebih prioritas aktif)
        if (code == KeyEvent.VK_I && !gp.dialogueOn) { // Cek !gp.dialogueOn agar tidak buka inv saat dialog
            iPressed = true; // GamePanel.update akan toggle gp.inventoryOpen
                             // dan harus menutup menu lain jika inventory dibuka
            return; 
        }
        if (gp.inventoryOpen) {
            // Tombol 'ESC' untuk menutup inventory jika sudah terbuka (selain 'I')
            if (code == KeyEvent.VK_ESCAPE){ 
                 gp.inventoryOpen = false;
            } else {
                handleInventoryInput(code); 
            }
            return; 
        }
        
        // Handle Dialogue (jika aktif dan tidak ada menu UI lain yang lebih prioritas)
        if (gp.dialogueOn) {
            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
                gp.dialogueOn = false;
                gp.ui.currentDialogue = "";
                // spacePressed = false; // Reset jika hanya untuk dialog, atau biarkan untuk Player.update
            }
            return; 
        }

        // --- AKSI GAMEPLAY STANDAR (jika tidak ada UI yang aktif) ---
        // Pergerakan dan interaksi utama
        // (Blok ini hanya akan tercapai jika semua kondisi menu UI di atas adalah false)

        if (code == KeyEvent.VK_SPACE) {
            // Interaksi NPC dan Bin (kompor sudah ditangani di awal)
            int npcIndex = gp.collisionChecker.checkNPC(gp.player, gp.npcManager.getCurrentMapNPCs());
            if (npcIndex != -1) {
                NPC interactedNPC = gp.npcManager.getCurrentMapNPCs().get(npcIndex);
                if (interactedNPC.getName().equals("Emily")) {
                    gp.ui.emilyMenuActive = true;
                    gp.ui.currentEmily = interactedNPC;
                    gp.ui.emilyMenuSelection = 0;
                } else {
                    gp.player.interactWithNPC(interactedNPC);
                }
            } else { // Hanya cek Bin jika tidak ada NPC
                int tileIDInFrontForBin = gp.tileM.getFrontTileID();
                List<Integer> shippingBinTileIDs = Arrays.asList(200, 211, 222, 233, 244, 255); //
                if (shippingBinTileIDs.contains(tileIDInFrontForBin)) {
                    gp.binOpen = true;
                    gp.ui.slotRow = 0; gp.ui.slotCol = 0;
                }
            }
            spacePressed = true; // Set flag jika masih dipakai di Player.update()
        } else if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = true;
        } else if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;
        } else if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        } else if (code == KeyEvent.VK_ENTER) {
            enterPressed = true; // Untuk aksi alat di Player.update()
        }
        // iPressed sudah ditangani di atas untuk inventory
    } // Akhir dari keyPressed

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

    private void handleCookingMenuInput(int code) {
        if (!gp.ui.showRecipeDetails) { // Sedang di layar DAFTAR RESEP
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.cookingMenuSelection--;
                if (gp.ui.cookingMenuSelection < 0) {
                    gp.ui.cookingMenuSelection = gp.ui.availableRecipes.isEmpty() ? 0 : gp.ui.availableRecipes.size() - 1;
                }
            } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.cookingMenuSelection++;
                if (gp.ui.cookingMenuSelection >= (gp.ui.availableRecipes.isEmpty() ? 1 : gp.ui.availableRecipes.size())) {
                    gp.ui.cookingMenuSelection = 0;
                }
            } else if (code == KeyEvent.VK_ENTER) {
                if (!gp.ui.availableRecipes.isEmpty() && gp.ui.cookingMenuSelection < gp.ui.availableRecipes.size()) {
                    gp.ui.selectedRecipeForDetail = gp.ui.availableRecipes.get(gp.ui.cookingMenuSelection);
                    gp.ui.showRecipeDetails = true;
                    gp.ui.cookingMenuSelection = 0; // Reset untuk opsi "Masak" / "Kembali"
                }
            } else if (code == KeyEvent.VK_ESCAPE) {
                gp.cookingMenuActive = false; // Tutup menu masak
                resetCookingUIState(); 
            }
        } else { // Sedang di layar DETAIL RESEP
             if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) { 
                gp.ui.cookingMenuSelection = 0; // Pilih "Masak"
            } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) { 
                gp.ui.cookingMenuSelection = 1; // Pilih "Kembali"
            } else if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.cookingMenuSelection == 0) { // Aksi "Masak"
                    gp.player.startCooking(gp.ui.selectedRecipeForDetail);
                    // startCooking akan menangani penutupan menu jika berhasil/gagal
                } else { // Aksi "Kembali" (cookingMenuSelection == 1)
                    gp.ui.showRecipeDetails = false;
                    int previousRecipeIndex = gp.ui.availableRecipes.indexOf(gp.ui.selectedRecipeForDetail);
                    gp.ui.cookingMenuSelection = (previousRecipeIndex != -1) ? previousRecipeIndex : 0; 
                }
            } else if (code == KeyEvent.VK_ESCAPE) {
                gp.ui.showRecipeDetails = false; 
                int previousRecipeIndex = gp.ui.availableRecipes.indexOf(gp.ui.selectedRecipeForDetail);
                gp.ui.cookingMenuSelection = (previousRecipeIndex != -1) ? previousRecipeIndex : 0;
            }
        }
    }

    private void resetCookingUIState() {
        gp.ui.cookingMenuSelection = 0;
        gp.ui.showRecipeDetails = false;
        gp.ui.selectedRecipeForDetail = null;
        if (gp.ui.availableRecipes != null) { 
             gp.ui.availableRecipes.clear();
        } else {
            gp.ui.availableRecipes = new ArrayList<>(); 
        }
    }
    
    private void handleInventoryInput(int code) { // Versi yang sudah ada di file Anda
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
            // Koreksi: Batas bawah harus mempertimbangkan jumlah item
            int maxRowsInv = (gp.player.getInventory().totalItems() > 0) ? 
                             (gp.player.getInventory().totalItems() - 1) / 13 : 0;
            if (gp.ui.slotRow > maxRowsInv) gp.ui.slotRow = maxRowsInv;
            if (gp.ui.slotRow > 4) gp.ui.slotRow = 4; // Batas visual jika tetap 5 baris
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            gp.ui.slotCol++;
            if (gp.ui.slotCol > 12) gp.ui.slotCol = 12;
        }

        // Penyesuaian kursor jika keluar dari batas item yang ada
        int totalItemsInv = gp.player.getInventory().totalItems();
        if (totalItemsInv > 0) {
            int index = gp.ui.slotRow * 13 + gp.ui.slotCol;
            if (index >= totalItemsInv) {
                int lastIndex = totalItemsInv - 1;
                gp.ui.slotRow = lastIndex / 13;
                gp.ui.slotCol = lastIndex % 13;
            }
        } else { // Inventory kosong
            gp.ui.slotRow = 0;
            gp.ui.slotCol = 0;
        }


        if (code == KeyEvent.VK_ENTER) {
            if (totalItemsInv > 0) {
                int indexInv = gp.ui.slotRow*13 + gp.ui.slotCol;
                 // Pastikan index valid lagi
                if (indexInv < totalItemsInv) {
                    Items currentHeldItem = gp.player.getItemHeld();
                    Items itemInSlot = gp.player.getInventory().getItems().get(indexInv).getItem();
    
                    if (currentHeldItem != null && currentHeldItem.equals(itemInSlot)) {
                        gp.player.setItemHeld(null);
                    } else {
                        gp.player.setItemHeld(itemInSlot);
                    }
                }
            }
        }
    }

}