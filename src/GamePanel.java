import java.awt.Color;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    public int maxWorldCol;
    public int maxWorldRow;
    public final int maxMap = 13;
    public int currentMap = 0;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    Timer gameClockTimer;
    Farm farm = new Farm(this, Weather.Sunny, 1, Season.Spring, new Time(6, 0), new ShippingBin());
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    UI ui = new UI(this, this.farm);
    Thread gameThread;
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public ItemManager itemManager = new ItemManager();
    public NPCManager npcManager = new NPCManager(this);
    public FishingManager fishingManager = new FishingManager(this);
    public Player player = new Player("Test", Gender.Male, this, keyH);
    public Store store = new Store(this);

    public boolean gamePaused = false;
    public boolean dialogueOn = false;
    public boolean inventoryOpen = false;
    public boolean binOpen = false;
    public boolean tvOn = false;
    public boolean sleepMenuOn = false;

    public boolean cookingMenuActive = false;

    public static final int MAIN_MENU_STATE = 0;
    public static final int GAMEPLAY_STATE = 1;
    public static final int HELP_STATE = 2;

    public int currentGameState = MAIN_MENU_STATE;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        gameClockTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentGameState == GAMEPLAY_STATE && !gamePaused && !inventoryOpen && !dialogueOn && !cookingMenuActive) {
                    farm.getTime().skipTime(5, farm);
                    if (farm != null && player != null) {
                        farm.checkPassiveActions(player);
                    }
                }
            }
        });
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setupGame() {
        currentGameState = GAMEPLAY_STATE;
        if (!gameClockTimer.isRunning()) {
            gameClockTimer.start();
        }
        gamePaused = false;
        dialogueOn = false;
        inventoryOpen = false;
        binOpen = false;
        cookingMenuActive = false;
        if (ui != null) {
            ui.emilyMenuActive = false;
            ui.emilyStoreActive = false;
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread!=null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        if (currentGameState == MAIN_MENU_STATE) {
        } else if (currentGameState == HELP_STATE) {
        } else if (currentGameState == GAMEPLAY_STATE) {
            if (keyH.pPressed) {
                gamePaused = !gamePaused;
                keyH.pPressed = false;
                if (gamePaused) {
                    inventoryOpen = false;
                    binOpen = false;
                    cookingMenuActive = false;
                    dialogueOn = false;
                    if (ui != null) {
                        ui.emilyMenuActive = false;
                        ui.emilyStoreActive = false;
                    }
                }
            }

            if (keyH.iPressed && !gamePaused && !cookingMenuActive && !dialogueOn && !ui.emilyMenuActive && !ui.emilyStoreActive && !binOpen) {
                inventoryOpen = !inventoryOpen;
                keyH.iPressed = false;
                if (inventoryOpen) {
                }
            }

            if (!gamePaused && !inventoryOpen && !binOpen && !cookingMenuActive && !dialogueOn && !ui.emilyMenuActive && !ui.emilyStoreActive) {
                 if (player != null) player.update();
            }
            if (ui != null && ui.messageOn) {
                ui.messageCounter++;
                if (ui.messageCounter > 120) {
                    ui.messageCounter = 0;
                    ui.messageOn = false;
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        if (currentGameState == MAIN_MENU_STATE) {
            ui.drawMainMenu(g2);
        } else if (currentGameState == HELP_STATE) {
            ui.drawHelpScreen(g2);
        } else if (currentGameState == GAMEPLAY_STATE) {
            if (gamePaused) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.drawString("PAUSED", screenWidth / 2 - 100, screenHeight / 2);
            } else if (cookingMenuActive) {
                g2.setColor(new Color(10, 10, 30, 245));
                g2.fillRect(0, 0, screenWidth, screenHeight);
                ui.drawCookingMenu(g2);
            } else if (inventoryOpen) {
                ui.drawInventory();
                ui.drawHeldItemsInventory();
                tileM.draw(g2);
                farm.drawFarm(g2);
                player.draw(g2);
                npcManager.draw(g2);
                ui.draw(g2);
            } else{
                tileM.draw(g2);
                farm.drawFarm(g2);
                player.draw(g2);
                npcManager.draw(g2);
                ui.draw(g2);
            }
            g2.dispose();
        }
    }
}