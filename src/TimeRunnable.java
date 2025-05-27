public class TimeRunnable implements Runnable {
    private final Time time;
    private boolean running = true;
    private final Farm farmInstance; // Tambahkan ini
    public TimeRunnable(Time time, Farm farmInstance) { // Modifikasi konstruktor
    this.time = time;
    this.farmInstance = farmInstance;
}


    public void stop() {
        running = false;
    }

    

    @Override
    public void run() {
        while (running) {
                try {
                    Thread.sleep(1000); // 1 detik nyata = 5 menit game
                    // Panggil skipTime di objek Time dengan referensi Farm
                    time.skipTime(5, farmInstance); //
                    // Output di driver akan mengambil status terbaru dari myFarm
                    // System.out.println("Current Game Time: " + time.getTime()); // Output ini mungkin tidak lagi relevan di sini
                } catch (InterruptedException e) {
                    // ...
                }
            }
        }
    }
