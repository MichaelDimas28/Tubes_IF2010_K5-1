import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.awt.BasicStroke;
import java.awt.Color;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

    private Farm farm;

    public Map<String, List<String>> npcDialogues = new HashMap<>();
    public boolean emilyMenuActive = false;
    public NPC currentEmily;
    public int emilyMenuSelection = 0;

    public UI (GamePanel gp, Farm farm) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 40);
        this.farm = farm;
        loadDialogues();
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
        
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if (gp.gamePaused) {
            String text = "PAUSED";
            int x = getCenteredX(text);
            int y = gp.screenHeight/2;
            g2.drawString(text, x, y);
        }
        if (messageOn) {
            g2.drawString(message, 100, 100);
            messageCounter++;
            if (messageCounter > 120) {
                messageCounter = 0;
                messageOn = false;
            }
        }

        drawTimeWindow(g2);
        if (emilyMenuActive) {
            g2.setColor(new Color(0,0,0,170));
            g2.fillRect(100, 100, 300, 150);
            g2.setColor(Color.white);
            g2.drawString("Talk", 120, 150);
            g2.drawString("Buy", 120, 200);
        }
    }

    public void drawTimeWindow(Graphics2D g2) {
        int x = gp.screenWidth - 220;
        int y = 20;
        int width = 200;
        int height = 100;

        // Background box
        g2.setColor(new Color(0, 0, 0, 150)); // semi-transparan
        g2.fillRoundRect(x, y, width, height, 15, 15);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 15, 15);
        g2.setFont(arial_40.deriveFont(Font.PLAIN, 18F));

        g2.drawString("Day " + farm.getDay(), x + 10, y + 25);
        g2.drawString("Time: " + farm.getTime().getTime(), x + 10, y + 45);
        g2.drawString("Weather: " + farm.getWeather(), x + 10, y + 65);
        g2.drawString("Season: " + farm.getSeason().name(), x + 10, y + 85);
    }


    private int getCenteredX(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    public void setEmilyInteractionMode(NPC emily) {
        // tampilkan UI pilihan Talk / Buy (misalnya pakai boolean emilyMenuActive, selectedIndex)
        emilyMenuActive = true;
        this.currentEmily = emily;
        emilyMenuSelection = 0;
    }

    public void loadDialogues() {
        npcDialogues.put("Emily", List.of(
            "Hai, aku Emily! Kalau kamu punya bahan segar, aku bisa masak sesuatu yang enak!",
            "Bibit ini bakal jadi bahan segar buat restoranku. Terima kasih!",
            "Wah, pas banget buat menu baru. Kamu tahu selera chef!",
            "Eh, aku chef, bukan tukang las...",
            "Oh, makasih! Mungkin suatu saat berguna.",
            "Setiap resep itu punya cerita. Kadang rasa enaknya datang dari kenangan.",
            "Masakanku enak-enak loh.",
            "Kamu tahu gak? Setiap bunga memiliki artinya masing-masing loh!",
            "Silakan lihat-lihat! Semua bahan segar langsung dari kebunku sendiri. Kamu ingin beli apa?",
            "Barangnya itu saja?",
            "Terima kasih sudah membeli! Sampai jumpa di lain waktu!"
        ));
        npcDialogues.put("Abigail", List.of(
            "Yo! Aku Abigail. Kamu suka eksplorasi? Aku baru nemu gua kecil di balik hutan!",
            "Ini dia bekal energi terbaik buat eksplorasi hari ini!",
            "Wah enak banget, cocok buat makan sambil lihat bintang!",
            "Kayaknya ini cuma akan nambah beban ransel aku...",
            "Hmmm... oke, bisa disimpan di tas, siapa tahu berguna.",
            "Kadang aku pengen tinggal di rumah pohon dan hidup dari buah liar.",
            "Aku nemu batu aneh di pinggir sungai... sepertinya punya aura mistis.",
            "Kalau kamu denger suara aneh tengah malam, kemungkinan itu cuma aku latihan pedang kayu."
        ));
        npcDialogues.put("Mayor Tadi", List.of(
            "Oh, kamu warga baru ya? Saya Mayor Tadi. Tolong jangan bawa barang murahan ke rumah saya.",
            "Akhirnya, seseorang tahu bagaimana memperlakukan seorang wali kota.",
            "Hmm, lumayanlah. Tidak seburuk yang kubayangkan.",
            "Astaga! Barang seperti ini bahkan tidak pantas ada di karpet saya.",
            "Err... kamu yakin ini hadiah? Terlihat biasa banget.",
            "Kapan ya ada festival kemewahan? Rakyat pasti suka parade perhiasan.",
            "Aku sedang mempertimbangkan untuk mengganti lantai balai kota dengan marmer. Demi estetika, tentu saja.",
            "Kalau semua warga punya selera sepertiku, desa ini akan jadi pusat mode!"
        ));
        npcDialogues.put("Caroline", List.of(
            "Hai! Aku Caroline, kalau kamu punya kayu bekas, bawa aja ke sini!",
            "Wow! Ini pas banget buat proyek seni daur ulangku.",
            "Ah, makanan yang netral dan menenangkan. Aku suka!",
            "Aduh! Lidahku terbakar cuma ngeliat ini...",
            "Oh, makasih ya. Mungkin bisa disimpan dulu.",
            "Kadang barang paling sederhana justru punya cerita paling menarik.",
            "Aku pernah bikin lampu dari sendok bekas. Lucu banget, lho!",
            "Kalau kamu punya benda unik, jangan buang dulu. Bisa aku sulap jadi dekorasi."
        ));
        npcDialogues.put("Dasco", List.of(
            "Yo, nama gue Dasco. Kalau mau uji keberuntungan, datang ke kasinoku.",
            "Ini baru makanan elit! Kamu ngerti selera tinggi!",
            "Boleh juga ini. Thanks ya!",
            "Lo beneran ngasih gue beginian? Yang bener aje bro.",
            "Hm, yaudahlah. Niatnya bagus meski item-nya nggak banget.",
            "Gue pernah kalah taruhan sama ayam. Jangan tanya gimana caranya.",
            "Hidup itu soal peluang. Tapi kadang, kamu cuma butuh sedikit gaya.",
            "Kalah itu bukan gagal. Itu cuma... waktu untuk strategi ulang. Gitu sih kata brosurku."
        ));
        npcDialogues.put("Perry", List.of(
            "Hai... aku Perry. Maaf kalau aku terlihat canggung. Aku lebih sering ngobrol dengan karakter novelku.",
            "Wah, manisnya mengingatkanku pada bab romantis novelnya.",
            "Oh, cocok buat menemani waktu baca malamku. Terima kasih.",
            "Eh... maaf, aku kurang suka bau amis... ngusap hidung",
            "Makasih. Mungkin bisa jadi inspirasi karakter baru?",
            "Kau tahu? Kadang aku menulis tokoh berdasarkan orang sungguhan... tapi tenang saja, bukan kamu... mungkin.",
            "Ide cerita sering datang dari tempat-tempat yang paling sepi… atau paling absurd.",
            "Aku menulis 10 halaman tadi malam... lalu hapus semua pagi ini. Proses kreatif tuh emang chaos."
        ));
    }
}