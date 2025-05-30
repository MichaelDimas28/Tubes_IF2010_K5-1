import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public String currentDialogue = "";
    public int slotCol = 0;
    public int slotRow = 0;

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
        
        drawTimeWindow(g2);
        if (gp.gamePaused) {
            String text = "PAUSED";
            int x = getCenteredX(text);
            int y = gp.screenHeight/2;
            g2.drawString(text, x, y);
        }
        
        if (gp.inventoryOpen) {
            drawInventory();
            drawHeldItemsInventory();
            // return;
        }

        if (gp.binOpen) {
            drawInventory();
            drawShippingBin();
            // return;
        }

        if (messageOn) {
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            int x = 30;
            int y = 200;

            float alpha = 1.0f;
            if (messageCounter > 90) { // mulai menghilang setelah 90 frame (~1.5 detik)
                alpha = 1.0f - ((messageCounter - 90) / 30f); // dari 1.0 ke 0.0 selama 30 frame
                if (alpha < 0) alpha = 0;
            }

            // Simpan komposit lama
            Composite originalComposite = g2.getComposite();
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2.setComposite(ac);

            // Gambar stroke hitam
            g2.setColor(Color.BLACK);
            g2.drawString(message, x + 2, y);
            g2.drawString(message, x - 2, y);
            g2.drawString(message, x, y + 2);
            g2.drawString(message, x, y - 2);

            // Gambar teks putih di atasnya
            g2.setColor(Color.WHITE);
            g2.drawString(message, x, y);

            // Kembalikan transparansi semula
            g2.setComposite(originalComposite);

            messageCounter++;
            if (messageCounter > 120) {
                messageCounter = 0;
                messageOn = false;
            }
        }

        if (gp.dialogueOn) {
            drawDialogueScreen();
            return;
        }
        if (emilyMenuActive) {
            int frameX = 100;
            int frameY = 100;
            int width = 300;
            int height = 150;

            drawSubWindow(frameX, frameY, width, height);
            g2.setFont(arial_40.deriveFont(Font.PLAIN, 24F));
            g2.setColor(Color.white);

            String[] options = {"Talk", "Buy"};
            for (int i = 0; i < options.length; i++) {
                if (i == emilyMenuSelection) {
                    g2.setColor(Color.YELLOW); // highlight pilihan
                } else {
                    g2.setColor(Color.white);
                }
                g2.drawString(options[i], frameX + 20, frameY + 50 + i * 40);
            }


        }
    }

    public void drawInventory() {
        // FRAME
        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int width = gp.screenWidth - (gp.tileSize*2);
        int height = gp.tileSize*6;
        drawSubWindow(frameX, frameY, width, height);

        //SLOT
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;

        //CURSOR
        int cursorX = slotXstart + (gp.tileSize * slotCol);
        int cursorY = slotYstart + (gp.tileSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // DRAW CURSOR
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // DRAW PLAYER'S ITEMS
        for (int i = 0; i<gp.player.getInventory().totalItems(); i++) {
            InventoryItem invItem = gp.player.getInventory().getItems().get(i);
            BufferedImage itemImage = invItem.getItem().getImage();
            if (itemImage != null) {
                g2.drawImage(itemImage, slotX, slotY, null);
            }
            String quantityText = String.valueOf(invItem.getQuantity());
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.setColor(Color.white);
            g2.drawString(quantityText, slotX + 30, slotY + 40); // atur offset sesuai selera
            slotX += gp.tileSize;
            if (slotX >= slotXstart + (gp.tileSize*13)) {
                slotX = slotXstart;
                slotY += gp.tileSize;
            }
        }
        // DRAW DESKRIPSI NAMA ITEM
        int index = slotRow * 13 + slotCol;
        InventoryItem selectedItem = gp.player.getInventory().getItems().get(index);

        int subX = frameX;
        int subY = frameY + height + 10; // 10px di bawah inventory
        int subWidth = 250;
        int subHeight = 50;

        drawSubWindow(subX, subY, subWidth, subHeight);
        if (selectedItem != null && selectedItem.getItem() != null) {
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(20f));
            g2.drawString(selectedItem.getItem().getItemName(), subX + 10, subY + 30);
        }
        // drawHeldItemsInventory();
    }

    public void drawHeldItemsInventory() {
        // DRAW HELD ITEMS
        int heldX = gp.tileSize;
        // int heldY = subY + height + gp.tileSize;
        int heldY = gp.tileSize*7 + 70;
        int heldWidth = gp.tileSize * 5;
        int heldHeight = gp.tileSize + 10;

        drawSubWindow(heldX, heldY, heldWidth, heldHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        g2.drawString("Held Item:", heldX + 20, heldY + 40);

        if (gp.player.getItemHeld() != null) {
            g2.drawImage(gp.player.getItemHeld().getImage(), heldX + gp.tileSize * 35/10, heldY + 4, gp.tileSize, gp.tileSize, null);
        }
    }

    public void drawShippingBin() {
        // FRAME
        int frameX = gp.screenWidth - gp.tileSize *6;
        int frameY = gp.tileSize*7 + 10;
        int width = gp.tileSize * 5; // 4 tile + margin
        int height = gp.tileSize * 5-30; // 4 slot + padding
        drawSubWindow(frameX, frameY, width, height);

        // SLOT
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 15;
        int slotX = slotXstart;
        int slotY = slotYstart;

        // DRAW SHIPPING BIN ITEMS
        for (int i = 0; i < gp.farm.getShippingBin().totalItems(); i++) {
            InventoryItem item = gp.farm.getShippingBin().getShippedItems().get(i);
            BufferedImage image = item.getItem().getImage();
            if (image != null) {
                g2.drawImage(image, slotX, slotY, null);
            }

            String quantityText = String.valueOf(item.getQuantity());
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.setColor(Color.white);
            g2.drawString(quantityText, slotX + 30, slotY + 40); // offset kanan bawah

            slotX += gp.tileSize;
            if (slotX >= slotXstart + (gp.tileSize * 4)) {
                slotX = slotXstart;
                slotY += gp.tileSize;
            }
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

    public void drawDialogueScreen() {
        int frameX = gp.tileSize;
        int frameY = gp.screenHeight - gp.tileSize*4;
        int width = gp.screenWidth - (gp.tileSize*2);
        int height = gp.tileSize*3;
        drawSubWindow(frameX, frameY, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,24F));
        int textX = frameX + gp.tileSize / 2;
        int textY = frameY + gp.tileSize / 2;

        int maxWidth = width - gp.tileSize;
        drawWrappedText(currentDialogue, textX, textY, maxWidth);
        // for (String line: currentDialogue.split("\n")) {
        //     g2.drawString(line, frameX, frameY);
        //     frameY +=40 ;
        // }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color bgColor = new Color(0, 0, 0, 180);
        Color borderColor = Color.white;

        g2.setColor(bgColor);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setStroke(new BasicStroke(3));
        g2.setColor(borderColor);
        g2.drawRoundRect(x, y, width, height, 35, 35);
    }



    private void drawWrappedText(String text, int x, int y, int maxWidth) {
        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = fm.getHeight();

        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String testLine = line + words[i] + " ";
            int testWidth = fm.stringWidth(testLine);
            if (testWidth > maxWidth) {
                g2.drawString(line.toString(), x, y);
                y += lineHeight;
                line = new StringBuilder(words[i] + " ");
            } else {
                line.append(words[i]).append(" ");
            }
        }

        // Draw sisa baris terakhir
        g2.drawString(line.toString(), x, y);
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
            "Aku... nggak nyangka kamu serius banget. Tapi aku bahagia! Ya, aku terima!",
            "Maaf... aku belum siap meninggalkan dapurku untuk komitmen sebesar itu.",
            "Besok? Minggu depan? Kapan pun kamu siap nikah, aku juga siap!",
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
            "Wuhuu! Petualangan baru nih! Aku terima! Mari kita bikin hidup makin seru!",
            "Huh? Serius? Aku belum siap untuk ikatan... aku masih mau menjelajah lebih banyak.",
            "Ayo! Kita rayakan pernikahan di puncak gunung! Atau... ya, rumah juga boleh.",
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
            "Hm... kau punya keberanian dan... gaya yang cukup. Baiklah, aku terima lamaranmu.",
            "Aku tidak bisa menerima lamaran yang belum memenuhi standar prestise dan keagungan.",
            "Mari kita rayakan pesta pernikahan yang paling bergengsi sepanjang sejarah desa.",
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
            "Serius? Wah... aku nggak nyangka, tapi... aku mau! Aku senang banget!",
            "Aku menghargai perasaanmu, tapi... aku belum siap untuk langkah sebesar itu.",
            "Wah, aku deg-degan... tapi juga nggak sabar! Yuk, kita wujudkan impian menikah ini bersama.",
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
            "Hah! Gila kamu... tapi aku suka gaya kamu. Aku terima!",
            "Sayang banget... kamu belum cukup berani untuk all-in. Coba lagi nanti.",
            "Pesta besar, taruhan tinggi, hidup baru... aku siap, ayo kita go big! Let's get married!",
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
            "Aku... aku gak pandai bicara, tapi... iya. Aku ingin menulis kisah kita bareng.",
            "Maaf... hatiku belum setenang yang kamu pikir. Mungkin lain waktu.",
            "Mari kita jadikan hari pernikahan itu menjadi halaman baru dalam cerita kita.",
            "Kau tahu? Kadang aku menulis tokoh berdasarkan orang sungguhan... tapi tenang saja, bukan kamu... mungkin.",
            "Ide cerita sering datang dari tempat-tempat yang paling sepi… atau paling absurd.",
            "Aku menulis 10 halaman tadi malam... lalu hapus semua pagi ini. Proses kreatif tuh emang chaos."
        ));
        npcDialogues.put("Ar", List.of(
            "Halo, saya Ar.",
            "Wow ini kelihatannya enak.",
            "Wow, aku bisa goreng ini.",
            "Plis jangan kasih aku ini lagi.",
            "OK.",
            "OK!",
            "Maaf, saya menolak.",
            "OK! Ayo menikah!",
            "Jika kamu jatuhkan sabun ke lantai, apakah sabunnya kotor atau lantainya bersih?",
            "Kamu tahu gak bahwa keripik kentang dibuat karena kokinya kesal sama permintaan pelanggan yang mau kentang gorengnya tipis-tipis?",
            "Fish n' Chips sangat enak."
        ));
        npcDialogues.put("Flo", List.of(
            "Hai! Aku Flo, salam kenal yaa semoga kita bisa jadi teman baik",
            "Ihh thank you so muchh!",
            "Makasiii, sehat-sehat!",
            "Woi... apasii...",
            "Oke thanks!",
            "YES! AKU MAU!",
            "Sorry, kayaknya kita ngga meant to be...",
            "Wahhh, Oke aku terima!",
            "Emang ada ya orang deket berbulan-bulan tapi ngga jadian?",
            "Kalau laper makan mie, kalau kangen just call me",
            "Are you some kind kambing? Because you make my heart terombang-ambing."
        ));
        npcDialogues.put("Mas", List.of(
            "Panggil saya mas Mas",
            "Makasi banget lo bro.",
            "Makasi lo bro",
            "Pinter lu bro",
            "Baik lu bro",
            "Daripada kelamaan, yauda de saya terima",
            "Maaf, Anda terlalu baik",
            "Yuk nikah!",
            "Tau nggak apa yang lebi jelek dari sapi? muka Anda",
            "Tau nggak apa yang lebi jelek dari muka Anda? ga ada",
            "Saya laki mas."
        ));
        npcDialogues.put("Rei", List.of(
            "Rei (nunduk dikit)",
            "Makasi (senyum)",
            "Makasi.",
            "(lari)",
            "(dipegang) (diliatin bentar) (ngangguk)",
            "(ngangguk)",
            "Ga (geleng-geleng kepala)",
            "Yuk.",
            "(duduk di lantai) (liat atap)... (nguap)",
            "(ngadu tatap muka)",
            "Itu, anu... ga jadi deh."
        ));
        npcDialogues.put("Fav", List.of(
            "Salken! Aku Fav, aku lagi bikin proyek IoT di desa ini!",
            "Bjir? Ga nyangka banget bakal ada orang yang ngasih aku ini, MAKASIHH!",
            "Aku sering tau beli ini kalo ke mall! Kapan-kapan beli bareng yuk!",
            "Jujur ga pernah punya ini sih, but I guess it's worth trying...",
            "Eh ini apaan? Kaya pernah liat tapi ga pernah megang, makasii.",
            "Eh demi apa? Ga nyangka, ak jg mw realll!",
            "Ogah gweh sama luwh.",
            "Mau hari ini banget??? Bentar-bentar aku siap-siap dulu! //lari",
            "Kenapa kita satu rumah ber5? Sumpah ini tuh demi penghematan uang di masa ekonomi sulit cuy!",
            "Di sini susah sinyal banget dah, rasanya pengen pindah desa aja.",
            "Di sini ada event jejepangan gak ya? Udah lama gak nge-event, kapan-kapan adain yuk!"
        ));
    }
}