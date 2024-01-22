// Aaron Ye
// 2024-01-21

package aaron.charts;

public class Note {
    // When the note appears in ms
    private int startTime;
    // When a long note is released in ms, -1 if not a long note
    private int endTime;
    // Which lane the note is in
    private final int lane;
    // Whether the note has been hit
    private boolean isHit;
    // Whether the long note has been released
    private boolean isReleased = false;

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

    public boolean isReleased() {
        return isReleased;
    }

    public void setReleased(boolean released) {
        isReleased = released;
    }

    public String toString() {
        return "Note: " + startTime + " " + endTime + " " + lane + " " + isHit;
    }
}