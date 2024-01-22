// Aaron Ye
// 2024-01-21

package aaron.charts;

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
import java.util.ArrayList;
import java.util.Arrays;

public class Chart {
    // Only 4 key charts are supported
    public enum Mode {
        KEYS_4,
    }

    // The background music
    private Clip audio = null;

    public Clip getAudio() {
        return audio;
    }

    // The time in ms the song starts at when previewing
    private double songPreviewTime = 0;

    public double getSongPreviewTime() {
        return songPreviewTime;
    }

    // The background image
    private BufferedImage background = null;

    public BufferedImage getBackground() {
        return background;
    }

    // Stuff from the chart file
    private String mapId = null;

    public String getMapId() {
        return mapId;
    }

    private String mapSetId = null;

    public String getMapSetId() {
        return mapSetId;
    }

    // Only 4 key is supported
    private Mode mode = null;

    public Mode getMode() {
        return mode;
    }

    // The name of the song
    private String songTitle = null;

    public String getSongTitle() {
        return songTitle;
    }

    // The name of the artist
    public String songArtist = null;

    public String getSongArtist() {
        return songArtist;
    }

    // Stuff from the chart file
    private String source = null;

    public String getSource() {
        return source;
    }

    private String tags = null;

    public String getTags() {
        return tags;
    }

    // Person who made the chart
    private String creator = null;

    public String getCreator() {
        return creator;
    }

    // The name of the difficulty
    private String difficultyName = null;

    public String getDifficultyName() {
        return difficultyName;
    }

    // The description of the chart
    private String description = null;

    public String getDescription() {
        return description;
    }

    // Stuff from the chart file
    private ArrayList<TimingPoint> timingPoints = null;

    public ArrayList<TimingPoint> getTimingPoints() {
        return timingPoints;
    }

    private ArrayList<SliderVelocity> sliderVelocities = null;

    public ArrayList<SliderVelocity> getSliderVelocities() {
        return sliderVelocities;
    }

    // The notes
    private ArrayList<Note>[] notes = null;

    public ArrayList<Note>[] getNotes() {
        return notes;
    }

    // If the chart has an error while parsing
    private boolean hasError = false;

    public boolean getError() {
        return this.hasError;
    }

    /**
     * Creates a chart object
     * @param audioFilePath The path to the audio file
     * @param songPreviewTime The time in ms the song starts at when previewing
     * @param backgroundFilePath The path to the background image
     * @param mapId The id of the map
     * @param mapSetId The id of the map set
     * @param mode The mode of the chart
     * @param songTitle The name of the song
     * @param songArtist The name of the artist
     * @param source The source of the song
     * @param tags The tags of the song
     * @param creator The creator of the chart
     * @param difficultyName The name of the difficulty
     * @param description The description of the chart
     * @param timingPoints The timing points of the chart
     * @param sliderVelocities The slider velocities of the chart
     * @param notes The notes of the chart
     */
    public Chart(
            String audioFilePath,
            double songPreviewTime,
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
            ArrayList<TimingPoint> timingPoints,
            ArrayList<SliderVelocity> sliderVelocities,
            ArrayList<Note>[] notes
    ) {
        // Make sure nothing is null
        if (
                audioFilePath == null ||
                        backgroundFilePath == null ||
                        mapId == null ||
                        mapSetId == null ||
                        mode == null ||
                        songTitle == null ||
                        songArtist == null ||
                        source == null ||
                        tags == null ||
                        creator == null ||
                        difficultyName == null ||
                        description == null ||
                        timingPoints == null ||
                        sliderVelocities == null ||
                        notes == null
        ) {
            this.hasError = true;
            return;
        }

        // Open the audio file
        try {
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

        // Open the background image
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

    /**
     * Parses a chart file
     * @param directoryPath The path to the directory
     * @param fileName The name of the file
     * @return The chart object
     */
    public static Chart parse(String directoryPath, String fileName) {
        // Fix the path
        if (!directoryPath.endsWith("/")) {
            directoryPath += "/";
        }

        // Read the file
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(directoryPath + fileName));
        } catch (IOException e) {
            return null;
        }
        try {
            // Parse
            String audioFilePath = directoryPath + parseStringLine(br.readLine());

            double songPreviewTime = 0;
            try {
                songPreviewTime = parseDoubleLine(br.readLine());
            } catch (NumberFormatException ignored) {
                // TODO: maybe improve this
                // Some songs do not have this
            }
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

            // Skips editor layer line
            br.readLine();
            // Skip custom audio samples line
            br.readLine();
            // Skip sound effect line
            br.readLine();

            // Read timing points
            ArrayList<TimingPoint> timingPoints = new ArrayList<>();
            br.readLine();
            while (true) {
                  String startTime = br.readLine();
                if (startTime.startsWith("SliderVelocities")) {
                    break;
                }
                double startTimeInt = parseDoubleLine(startTime);
                double bpm = Double.parseDouble(parseStringLine(br.readLine()));
                timingPoints.add(new TimingPoint(startTimeInt, bpm));
            }

            // Read slider velocities
            ArrayList<SliderVelocity> sliderVelocities = new ArrayList<>();
            while (true) {
                String startTime = br.readLine();
                if (startTime.startsWith("HitObjects")) {
                    break;
                }
                double startTimeDouble = parseDoubleLine(startTime);
                double multiplier = parseDoubleLine(br.readLine());
                sliderVelocities.add(new SliderVelocity(startTimeDouble, multiplier));
            }

            // Read the notes
            ArrayList<Note>[] notes = (ArrayList<Note>[]) new ArrayList[4];
            for (int i = 0; i < 4; i++) {
                notes[i] = new ArrayList<>();
            }
            String line = br.readLine();
            while (line != null) {
                int startTime = parseIntLine(line);
                int lane = parseIntLine(br.readLine());
                String maybeEndTime = br.readLine().trim();
                if (maybeEndTime.startsWith("EndTime: ")) {
                    int endTime = parseIntLine(maybeEndTime);
                    notes[lane - 1].add(new Note(startTime, endTime, lane, false));
                    // Skip key sounds line
                    br.readLine();
                } else {
                    notes[lane - 1].add(new Note(startTime, -1, lane, false));
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

    /**
     * Parses a string from a line of the chart file
     * @param s The line
     * @return The string
     */
    private static String parseStringLine(String s) {
        return s.substring(s.indexOf(":") + 1).trim();
    }

    /**
     * Parses an integer from a line of the chart file
     * @param s The line
     * @return The integer
     */
    private static int parseIntLine(String s) {
        return Integer.parseInt(s.substring(s.indexOf(":") + 1).trim());
    }

    /**
     * Parses a double from a line of the chart file
     * @param s The line
     * @return The double
     */
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
                ",\n notes=" + Arrays.toString(notes) +
                ",\n hasError=" + hasError +
                '}';
    }
}
