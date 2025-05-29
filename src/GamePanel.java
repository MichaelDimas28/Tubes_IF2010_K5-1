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
    final int originalTileSize = 16; // 16px untuk setiap pixel
    final int scale = 3; // Agar ukuran pixel menjadi 48px
    
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixel
    final int screenHeight = tileSize * maxScreenRow; // 576 pixel
    // final int FPS = 60;
    
    // World Settings
    public int maxWorldCol;
    public int maxWorldRow;
    public final int maxMap = 12; // Total jumlah map yang ada
    public int currentMap = 0;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
    Timer gameClockTimer;
    Farm farm = new Farm(Weather.Sunny, 1, Season.Spring, new Time(6, 0));
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    UI ui = new UI(this, this.farm);
    Thread gameThread;
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Player player = new Player("Test", Gender.Male, 100, 0, this, keyH);
    public ItemManager itemManager = new ItemManager();
    public NPCManager npcManager = new NPCManager(this);

    // public int gameState;
    // public final int playState = 1;
    // public final int pauseState = 2;
    // public final int dialogueState = 3;
    public boolean gamePaused = false;
    public boolean dialogueOn = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        gameClockTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gamePaused) { // Tambahkan kondisi jika game bisa pause
                farm.getTime().skipTime(1, farm); // Tambah 1 menit per 1 detik
            }
            }
        });
        gameClockTimer.start();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
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
        if (keyH.pPressed) {
            gamePaused = !gamePaused;
            keyH.pPressed = false;
        }

        if (!gamePaused) {
            player.update();
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        if (gamePaused) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("PAUSED", screenWidth / 2 - 100, screenHeight / 2);
        }


        tileM.draw(g2);

        
        player.draw(g2);
        npcManager.draw(g2);
        ui.draw(g2);
        g2.dispose();
    }
}