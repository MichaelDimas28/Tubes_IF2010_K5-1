public class TimeRunnable implements Runnable {
    private final Time time;
    private boolean running = true;

    public TimeRunnable(Time time) {
        this.time = time;
    }

    public void stop() {
        running = false;
    }

    public void skipTime(int minutes) {
        time.skipTime(minutes);
        System.out.println("[Sleep] Time skipped to: " + time.getTime());
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000); // 1 detik nyata = 5 menit game
                time.skipTime(5);
                System.out.println("Current Game Time: " + time.getTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
