public class Time {
    private int hour;
    private int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public synchronized void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public synchronized String getTime() {
        return String.format("%02d:%02d", hour, minute);
    }

    public synchronized int getHour() {
        return hour;
    }

    public synchronized int getMinute() {
        return minute;
    }

    public synchronized void skipTime(int minutesToAdd, Farm farmInstance) { // Tambahkan farmInstance
    this.minute += minutesToAdd;
    while (this.minute >= 60) {
        this.minute -= 60;
        this.hour++;
        if (this.hour >= 24) {
            this.hour -= 24;
            if (farmInstance != null) {
                farmInstance.processNewDay(); // Langsung panggil metode di Farm saat hari berganti
            }
        }
    }
}

}
