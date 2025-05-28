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

    public synchronized void skipTime(int minutesToAdd) {
        int newMinute = minute + minutesToAdd;
        hour = (hour + newMinute / 60) % 24;
        minute = newMinute % 60;
    }

    // Agar bisa menjadi nilai 0000-2400 (dalam format HHMM)
    public synchronized int getTimeAsInt() {
        return hour * 100 + minute;
    }

    public synchronized boolean isWithin(int startTime, int endTime) {
        int current = getTimeAsInt();
        if (startTime <= endTime) {
            return current >= startTime && current <= endTime;
        } else {
        // untuk range lintas tengah malam, misal 20.00 - 02.00
            return current >= startTime || current <= endTime;
        }
    }
}
