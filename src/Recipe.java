import java.util.List; // Hanya jika diperlukan oleh konstruktor superclass Items Anda

public class Recipe extends Items {
    private String unlocksFoodName; // Nama dari Food item yang akan di-unlock oleh resep ini

    public Recipe(String itemName, int buyPrice, String imageName, String unlocksFoodName) {
        // Harga jual resep biasanya 0 karena hanya dibeli sekali
        super(itemName, buyPrice, 0, imageName); 
        this.unlocksFoodName = unlocksFoodName;
    }

    public String getUnlocksFoodName() {
        return unlocksFoodName;
    }

    // Anda tidak perlu menambahkan item resep ini ke inventory pemain.
    // Jadi, tidak ada logika khusus lain yang biasanya dibutuhkan di sini selain data.
}
