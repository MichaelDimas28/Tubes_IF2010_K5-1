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
}
