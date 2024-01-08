package charts;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;

public class Chart {
    // Only 4 key charts are supported for now
    public enum Mode {
        KEYS_4,
    }

    private Clip audio = null;

    public Clip getAudio() {
        return audio;
    }

    private final int songPreviewTime;

    public int getSongPreviewTime() {
        return songPreviewTime;
    }

    private BufferedImage background = null;

    public BufferedImage getBackground() {
        return background;
    }

    private final String mapId;

    public String getMapId() {
        return mapId;
    }

    private final String mapSetId;

    public String getMapSetId() {
        return mapSetId;
    }

    private final Mode mode;

    public Mode getMode() {
        return mode;
    }

    private final String songTitle;

    public String getSongTitle() {
        return songTitle;
    }

    public final String songArtist;

    public String getSongArtist() {
        return songArtist;
    }

    private final String source;

    public String getSource() {
        return source;
    }

    private final String tags;

    public String getTags() {
        return tags;
    }

    private final String creator;

    public String getCreator() {
        return creator;
    }

    private final String difficultyName;

    public String getDifficultyName() {
        return difficultyName;
    }

    private final String description;

    public String getDescription() {
        return description;
    }

    private final ArrayDeque<TimingPoint> timingPoints;

    public ArrayDeque<TimingPoint> getTimingPoints() {
        return timingPoints;
    }

    private final ArrayDeque<SliderVelocity> sliderVelocities;

    public ArrayDeque<SliderVelocity> getSliderVelocities() {
        return sliderVelocities;
    }

    private final ArrayDeque<Note> notes;

    public ArrayDeque<Note> getNotes() {
        return notes;
    }

    private boolean hasError = false;

    public boolean getError() {
        return this.hasError;
    }

    public Chart(
            String audioFilePath,
            int songPreviewTime,
            String backgroundFilePath,
            String mapId,
            String mapSetId,
            Mode mode,
            String songTitle,
            String songArtist,
            String source,
            String tags,
            String creator,
            String difficultyName,
            String description,
            ArrayDeque<TimingPoint> timingPoints,
            ArrayDeque<SliderVelocity> sliderVelocities,
            ArrayDeque<Note> notes
    ) {
        try {
            // TODO: must convert mp3 to wav, maybe implement auto conversion
            if (audioFilePath.endsWith(".mp3")) {
                audioFilePath = audioFilePath.substring(0, audioFilePath.length() - 4) + ".wav";
            }
            this.audio = AudioSystem.getClip();
            this.audio.open(AudioSystem.getAudioInputStream(new File(audioFilePath)));
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            this.hasError = true;
            System.out.println("Error loading audio file");
        }

        this.songPreviewTime = songPreviewTime;

        try {
            this.background = ImageIO.read(new File(backgroundFilePath));
        } catch (IOException | IllegalArgumentException e) {
            this.hasError = true;
            System.out.println("Error loading background image");
        }

        this.mapId = mapId;
        this.mapSetId = mapSetId;
        this.mode = mode;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.source = source;
        this.tags = tags;
        this.creator = creator;
        this.difficultyName = difficultyName;
        this.description = description;
        this.timingPoints = timingPoints;
        this.sliderVelocities = sliderVelocities;
        this.notes = notes;
    }

    public static Chart parse(String directoryPath, String fileName) {
        if (!directoryPath.endsWith("/")) {
            directoryPath += "/";
        }

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(directoryPath + fileName));
        } catch (IOException e) {
            return null;
        }
        try {
            String audioFilePath = directoryPath + parseStringLine( br.readLine());
            int songPreviewTime = parseIntLine(br.readLine());
            String backgroundFilePath = directoryPath + parseStringLine(br.readLine());
            String mapId = parseStringLine(br.readLine());
            String mapSetId = parseStringLine(br.readLine());
            Mode mode;
            if (parseStringLine(br.readLine()).equals("Keys4")) {
                mode = Mode.KEYS_4;
            } else {
                return null;
            }
            String songTitle = parseStringLine(br.readLine());
            String songArtist = parseStringLine(br.readLine());
            String source = parseStringLine(br.readLine());
            String tags = parseStringLine(br.readLine());
            String creator = parseStringLine(br.readLine());
            String difficultyName = parseStringLine(br.readLine());
            String description = parseStringLine(br.readLine());

            // TODO: make it work if the array spans multiple lines
            // Skips editor layer line
            br.readLine();
            // Skip custom audio samples line
            br.readLine();
            // Skip sound effect line
            br.readLine();

            ArrayDeque<TimingPoint> timingPoints = new ArrayDeque<>();
            br.readLine();
            while (true) {
                String startTime = br.readLine();
                if (startTime.startsWith("SliderVelocities")) {
                    break;
                }
                int startTimeInt = parseIntLine(startTime);
                int bpm = Integer.parseInt(parseStringLine(br.readLine()));
                timingPoints.add(new TimingPoint(startTimeInt, bpm));
            }

            ArrayDeque<SliderVelocity> sliderVelocities = new ArrayDeque<>();
            while (true) {
                String startTime = br.readLine();
                if (startTime.startsWith("HitObjects")) {
                    break;
                }
                double startTimeDouble = parseDoubleLine(startTime);
                double multiplier = parseDoubleLine(br.readLine());
                sliderVelocities.add(new SliderVelocity(startTimeDouble, multiplier));
            }

            ArrayDeque<Note> notes = new ArrayDeque<>();
            String line = br.readLine();
            while (line != null) {
                int startTime = parseIntLine(line);
                int lane = parseIntLine(br.readLine());
                String maybeEndTime = br.readLine().trim();
                if (maybeEndTime.startsWith("EndTime: ")) {
                    int endTime = parseIntLine(maybeEndTime);
                    notes.add(new Note(startTime, endTime, lane));
                    // Skip key sounds line
                    br.readLine();
                } else {
                    notes.add(new Note(startTime, -1, lane));
                }
                line = br.readLine();
            }

            return new Chart(
                    audioFilePath,
                    songPreviewTime,
                    backgroundFilePath,
                    mapId,
                    mapSetId,
                    mode,
                    songTitle,
                    songArtist,
                    source,
                    tags,
                    creator,
                    difficultyName,
                    description,
                    timingPoints,
                    sliderVelocities,
                    notes
            );
        } catch (IOException e) {
            return null;
        }
    }

    private static String parseStringLine(String s) {
        return s.substring(s.indexOf(":") + 1).trim();
    }

    private static int parseIntLine(String s) {
        return Integer.parseInt(s.substring(s.indexOf(":") + 1).trim());
    }

    private static double parseDoubleLine(String s) {
        return Double.parseDouble(s.substring(s.indexOf(":") + 1).trim());
    }

    @Override
    public String toString() {
        return "Chart{" +
                "audio=" + audio +
                ",\n songPreviewTime=" + songPreviewTime +
                ",\n background=" + background +
                ",\n mapId='" + mapId + '\'' +
                ",\n mapSetId='" + mapSetId + '\'' +
                ",\n mode=" + mode +
                ",\n songTitle='" + songTitle + '\'' +
                ",\n songArtist='" + songArtist + '\'' +
                ",\n source='" + source + '\'' +
                ",\n tags='" + tags + '\'' +
                ",\n creator='" + creator + '\'' +
                ",\n difficultyName='" + difficultyName + '\'' +
                ",\n description='" + description + '\'' +
                ",\n timingPoints=" + timingPoints +
                ",\n sliderVelocities=" + sliderVelocities +
                ",\n notes=" + notes +
                ",\n hasError=" + hasError +
                '}';
    }
}
