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
    public boolean fPressed;

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

        if (gp.currentGameState == GamePanel.MAIN_MENU_STATE) {
            handleMainMenuInput(code);
        } else if (gp.currentGameState == GamePanel.HELP_STATE) {
            handleHelpScreenInput(code);
        } else if (gp.currentGameState == GamePanel.GAMEPLAY_STATE) {

            if (code == KeyEvent.VK_P) {
                pPressed = true;
                return;
            }
            if (gp.gamePaused) {
                return;
            }

            if (code == KeyEvent.VK_SPACE &&
                !gp.cookingMenuActive && !gp.inventoryOpen && !gp.binOpen && !gp.dialogueOn &&
                !gp.ui.emilyMenuActive && !gp.ui.emilyStoreActive) {

                if (gp.currentMap == 11 || gp.currentMap == 12) {
                    int tileIDInFront = gp.tileM.getFrontTileID();
                    int playerTileX = gp.player.worldX / gp.tileSize;
                    int playerTileY = gp.player.worldY / gp.tileSize;

                    boolean playerAtCorrectCookingSpot = (playerTileX == 2 && playerTileY == 6);

                    if (playerAtCorrectCookingSpot && gp.player.direction.equals("up") && tileIDInFront == 467) {
                        if (gp.player.isCooking) {
                            gp.ui.showMessage("Kamu sedang memasak: " + gp.player.getRecipeBeingCooked().getItemName());
                        } else {
                            gp.cookingMenuActive = true;

                            gp.inventoryOpen = false;
                            gp.binOpen = false;
                            gp.dialogueOn = false;
                            gp.ui.emilyMenuActive = false;
                            gp.ui.emilyStoreActive = false;

                            gp.ui.availableRecipes.clear();
                            for (Items item : gp.itemManager.getItemMap().values()) {
                                if (item instanceof Food) {
                                    Food food = (Food) item;
                                    if (food.isRecipeAcquired()) {
                                        gp.ui.availableRecipes.add(food);
                                    }
                                }
                            }
                            gp.ui.cookingMenuSelection = 0;
                            gp.ui.showRecipeDetails = false;
                            gp.ui.selectedRecipeForDetail = null;
                        }

                    }
                }
            }

            if (gp.cookingMenuActive) {
                handleCookingMenuInput(code);
                return;
            }
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
                            gp.player.addFishCaught(1);
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
                return;
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
                    if (gp.ui.emilyMenuSelection == 0) {
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
                    } else if (gp.ui.emilyMenuSelection == 1) {

                        gp.ui.emilyStoreActive = true;
                        gp.dialogueOn = false;
                        gp.ui.slotRow = 0;
                        gp.ui.slotCol = 0;
                    }
                    gp.ui.emilyMenuActive = false;
                }

                return;
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

                if (code == KeyEvent.VK_ENTER) {
                    List<Items> currentStoreItems = gp.store.getCurrentSeasonItems();
                    int itemIndexInStore = gp.ui.emilyStoreRow * 13 + gp.ui.emilyStoreCol;

                    if (itemIndexInStore >= 0 && itemIndexInStore < currentStoreItems.size()) {
                        Items selectedItemFromStoreList = currentStoreItems.get(itemIndexInStore);

                        if (selectedItemFromStoreList instanceof Recipe) {
                            Recipe recipeItem = (Recipe) selectedItemFromStoreList;
                            Items foodToCheck = gp.itemManager.getItem(recipeItem.getUnlocksFoodName());
                            if (foodToCheck instanceof Food && ((Food) foodToCheck).isRecipeAcquired()) {
                                gp.ui.showMessage("Kamu sudah memiliki resep ini!");
                                return;
                            }

                            gp.ui.quantityInput = "1";

                            gp.ui.enteringQuantity = false;
                            gp.ui.confirmingPurchase = true;
                            gp.ui.confirmYes = true;
                            return;
                        }

                        gp.ui.enteringQuantity = true;
                        gp.ui.quantityInput = "";
                    }
                } else if (code == KeyEvent.VK_ESCAPE) {
                    gp.ui.emilyStoreActive = false;
                    gp.ui.enteringQuantity = false;
                    gp.ui.confirmingPurchase = false;
                    gp.ui.quantityInput = "";
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

                int totalItems = gp.player.getInventory().totalItems();
                int maxIndex = totalItems - 1;
                int currentIndex = gp.ui.slotRow * 13 + gp.ui.slotCol;
                if (currentIndex > maxIndex) {
                    gp.ui.slotRow = maxIndex / 13;
                    gp.ui.slotCol = maxIndex % 13;
                }

                if (code == KeyEvent.VK_ENTER) {
                    int index = gp.ui.slotRow * 13 + gp.ui.slotCol;
                    if (index < gp.player.getInventory().totalItems()) {
                        InventoryItem selectedItem = gp.player.getInventory().getItems().get(index);

                        if (selectedItem != null && selectedItem.getQuantity() > 0) {

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

            if (gp.tvOn) {
                if (code == KeyEvent.VK_SPACE) {
                    gp.tvOn = false;
                }
                gp.farm.getTime().skipTime(15, null);
                return;
            }

            if (gp.sleepMenuOn) {
                if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT || code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                    gp.ui.confirmSleep = !gp.ui.confirmSleep;
                } else if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.confirmSleep) {
                        gp.ui.processSleep();
                    } else {
                        gp.sleepMenuOn = false;
                    }
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

                int index = gp.ui.slotRow * 13 + gp.ui.slotCol;
                if (index >= gp.player.getInventory().totalItems()) {
                    int lastIndex = gp.player.getInventory().totalItems() - 1;
                    gp.ui.slotRow = lastIndex / 13;
                    gp.ui.slotCol = lastIndex % 13;
                }

                if (code == KeyEvent.VK_ENTER) {
                    int indexInv = gp.ui.slotRow*13 + gp.ui.slotCol;

                    if (gp.player.getItemHeld() != null && gp.player.getItemHeld().equals(gp.player.getInventory().getItems().get(indexInv).getItem())) {
                        gp.player.setItemHeld(null);
                        return;
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
                if (tileName != null && Arrays.asList("468.png", "469.png").contains(tileName)) {
                    gp.tvOn = true;
                }
                if (tileName != null && Arrays.asList("301.png","302.png","303.png","304.png","305.png","306.png").contains(tileName)) {
                    gp.sleepMenuOn = true;
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
            if (code == KeyEvent.VK_F) {
                fPressed = true;
            }
            if (code == KeyEvent.VK_G) {
            gp.player.setGold(gp.player.getGold() + 1000);
            gp.ui.showMessage("+1000g (Cheat Activated)");
        }
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
        if (code == KeyEvent.VK_F) {
            fPressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        
        if (code == KeyEvent.VK_P) pPressed = false;
        if (code == KeyEvent.VK_I) iPressed = false;
        if (code == KeyEvent.VK_SPACE) spacePressed = false; 
    }private void handleCookingMenuInput(int code) {
        if (!gp.ui.showRecipeDetails) {
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
                    gp.ui.cookingMenuSelection = 0;
                }
            } else if (code == KeyEvent.VK_ESCAPE) {
                gp.cookingMenuActive = false;
                resetCookingUIState();
            }
        } else {
             if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.cookingMenuSelection = 0;
            } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.cookingMenuSelection = 1;
            } else if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.cookingMenuSelection == 0) {
                    gp.player.startCooking(gp.ui.selectedRecipeForDetail);

                } else {
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

    private void handleInventoryInput(int code) {
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

            int maxRowsInv = (gp.player.getInventory().totalItems() > 0) ?
                             (gp.player.getInventory().totalItems() - 1) / 13 : 0;
            if (gp.ui.slotRow > maxRowsInv) gp.ui.slotRow = maxRowsInv;
            if (gp.ui.slotRow > 4) gp.ui.slotRow = 4;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            gp.ui.slotCol++;
            if (gp.ui.slotCol > 12) gp.ui.slotCol = 12;
        }

        int totalItemsInv = gp.player.getInventory().totalItems();
        if (totalItemsInv > 0) {
            int index = gp.ui.slotRow * 13 + gp.ui.slotCol;
            if (index >= totalItemsInv) {
                int lastIndex = totalItemsInv - 1;
                gp.ui.slotRow = lastIndex / 13;
                gp.ui.slotCol = lastIndex % 13;
            }
        } else {
            gp.ui.slotRow = 0;
            gp.ui.slotCol = 0;
        }

        if (code == KeyEvent.VK_ENTER) {
            if (totalItemsInv > 0) {
                int indexInv = gp.ui.slotRow*13 + gp.ui.slotCol;

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
    private void handleMainMenuInput(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.mainMenuCommandNum--;
            if (gp.ui.mainMenuCommandNum < 0) {
                gp.ui.mainMenuCommandNum = 2;
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.mainMenuCommandNum++;
            if (gp.ui.mainMenuCommandNum > 2) {
                gp.ui.mainMenuCommandNum = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.mainMenuCommandNum == 0) {
                gp.setupGame();
            } else if (gp.ui.mainMenuCommandNum == 1) {
                gp.currentGameState = GamePanel.HELP_STATE;
            } else if (gp.ui.mainMenuCommandNum == 2) {
                System.exit(0);
            }
        }
    }

    private void handleHelpScreenInput(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            gp.currentGameState = GamePanel.MAIN_MENU_STATE;
        }
    }

}