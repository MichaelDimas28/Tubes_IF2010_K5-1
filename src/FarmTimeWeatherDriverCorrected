import java.util.Scanner;
import java.io.IOException; // Untuk menangani IOException dari FarmMap

public class FarmTimeWeatherDriverCorrected {

    private static Farm myFarm;
    private static TimeRunnable farmTimeRunnable;
    private static Thread timeThread;
    private static volatile boolean keepRunningDriver = true; // Untuk menghentikan loop observasi

    public static void main(String[] args) {
        System.out.println("--- Driver Tes Time, Season, dan Weather pada Farm (Menggunakan TimeRunnable) ---");

        initializeFarmAndDependencies();

        if (myFarm == null) {
            System.out.println("Gagal menginisialisasi Farm. Driver berhenti.");
            return;
        }

        System.out.println("Farm berhasil dibuat: " + myFarm.getFarmName());
        printFarmStatus("Status Awal:");

        // Buat dan jalankan TimeRunnable menggunakan objek Time dari Farm
        farmTimeRunnable = new TimeRunnable(myFarm.getTime(), myFarm); // Menggunakan TimeRunnable yang sudah Anda buat
        timeThread = new Thread(farmTimeRunnable);
        timeThread.start();
        System.out.println("Thread TimeRunnable dimulai. Waktu akan berjalan otomatis.");

        runObservationLoop();

        // Hentikan thread waktu saat driver selesai
        System.out.println("Menghentikan TimeRunnable...");
        if (farmTimeRunnable != null) {
            farmTimeRunnable.stop();
        }
        if (timeThread != null && timeThread.isAlive()) {
            try {
                timeThread.join(1500); // Tunggu thread waktu selesai (maks 1.5 detik)
                if (timeThread.isAlive()) {
                    System.out.println("TimeRunnable tidak berhenti, melakukan interrupt...");
                    timeThread.interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Driver diinterupsi saat menunggu TimeRunnable.");
            }
        }
        System.out.println("Driver selesai.");
    }

    private static void initializeFarmAndDependencies() {
        Player dummyPlayer = null;
        FarmMap dummyFarmMap = null;
        // Anda bisa mencoba memuat peta asli jika pathnya benar dan dibutuhkan untuk tes Farm.
        // try {
        // dummyFarmMap = new FarmMap("data/farmMap/farm_map1.txt");
        // } catch (IOException e) {
        // System.err.println("Peringatan: Gagal memuat dummy FarmMap: " + e.getMessage());
        // }
        ShippingBin dummyShippingBin = new ShippingBin();
        Time initialTimeForFarm = new Time(6, 0); // Mulai jam 06:00

        // Enum Weather sekarang ada di file Weather.java, bukan inner enum Farm
        myFarm = new Farm("Peternakan Driver",
                          Weather.Sunny, // Cuaca awal
                          1,             // Hari awal
                          Season.Spring, // Musim awal
                          dummyPlayer,
                          dummyFarmMap,
                          initialTimeForFarm,
                          dummyShippingBin);
    }

    private static void runObservationLoop() {
        Scanner scanner = new Scanner(System.in);
        long lastCheckTime = System.currentTimeMillis();
        final long CHECK_INTERVAL_MS = 1000; // Cek status farm setiap 200ms

        System.out.println("\nMemulai observasi. Ketik 'exit' untuk berhenti.");

        while (keepRunningDriver) {
            // Periksa input pengguna secara non-blocking (atau dengan timeout singkat)
            // Ini hanya contoh sederhana, bisa dibuat lebih canggih.
            if (System.currentTimeMillis() - lastCheckTime > CHECK_INTERVAL_MS) {
                // Panggil metode di Farm untuk memeriksa apakah hari telah berganti
                // dan untuk memproses logika hari baru jika ya.
                boolean dayChanged = myFarm.checkForNewDay(); // Metode baru di Farm.java

                if (dayChanged) {
                    printFarmStatus("HARI BARU DIPROSES:");
                } else {
                    // Cetak status waktu secara berkala untuk melihat tick dari TimeRunnable
                     System.out.println("Tick Waktu Farm: " + myFarm.getTime().getTime() +
                                       " (Hari: " + myFarm.getDay() + " Musim: " + myFarm.getSeason() +
                                       " Cuaca: " + myFarm.getWeather() + ")");
                }
                lastCheckTime = System.currentTimeMillis();
                
            }

            try {
                if (System.in.available() > 0) { // Cek apakah ada input di konsol
                    String input = scanner.nextLine().trim().toLowerCase();
                    if ("exit".equals(input)) {
                        keepRunningDriver = false;
                    } else if ("status".equals(input)) {
                        printFarmStatus("Status Saat Ini:");
                    } else {
                        try {
                            int minutesToSkip = Integer.parseInt(input);
                            if (minutesToSkip > 0) {
                                System.out.println("Memajukan waktu sebanyak " + minutesToSkip + " menit...");
                                // Panggil skipTime pada objek Time yang dimiliki Farm
                                // dan biarkan Farm yang menghandle logika pergantian hari jika terjadi
                                for (int i = 0; i < minutesToSkip; i++) {
                                    myFarm.getTime().skipTime(1,myFarm); // Skip per menit agar checkForNewDay bisa lebih presisi
                                    // Jika skipTime di Time.java sudah menghandle logic advanceDay dengan benar,
                                    // maka checkForNewDay akan terpicu olehnya.
                                    // Jika tidak, kita panggil checkForNewDay setelah loop skipTime.
                                }
                                // Setelah semua menit di-skip, cek sekali lagi untuk memastikan hari baru diproses
                                // boolean dayChangedAfterSkip = myFarm.checkForNewDay();
                                // if (dayChangedAfterSkip) {
                                //     printFarmStatus("Status Farm Setelah Skip Waktu (Hari Baru):");
                                // } else {
                                //     printFarmStatus("Status Farm Setelah Skip Waktu:");
                                // }
                                printFarmStatus("Status Farm Setelah Skip " + minutesToSkip + " Menit:");
                            } else {
                                System.out.println("Masukkan angka positif untuk jumlah menit.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Perintah tidak dikenal atau angka tidak valid. Ketik 'status', 'exit', atau jumlah menit.");
                        }
                }
            }
        }
                catch (IOException e) {
                // Abaikan untuk driver sederhana ini
            }

            // Beri sedikit jeda agar tidak membebani CPU
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                keepRunningDriver = false; // Hentikan jika diinterupsi
                Thread.currentThread().interrupt();
            }
        }
        scanner.close();
    }

    private static void printFarmStatus(String prefix) {
        System.out.println(prefix + " Waktu: " + myFarm.getTime().getTime() +
                           " | Hari Global: " + myFarm.getDay() +
                           " | Hari di Musim: " + myFarm.calculateDayInSeason() + // Menggunakan metode kalkulasi
                           " | Musim: " + myFarm.getSeason() +
                           " | Cuaca Hari Ini: " + myFarm.getWeather() +
                           " | Prakiraan Besok: " + myFarm.getWeatherForTomorrow());
    }
}