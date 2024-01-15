package aaron.charts;

public class Note {
    private int startTime;
    private int endTime;
    private final int lane;
    private boolean isHit;

    public Note(int startTime, int endTime, int lane, boolean isHit) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.lane = lane;
        this.isHit = isHit;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getLane() {
        return lane;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public String toString() {
        return "Note: " + startTime + " " + endTime + " " + lane + " " + isHit;
    }
}