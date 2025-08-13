package utils;

import models.Song;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to load song data from a CSV file.
 */
public class CSVLoader {

    /**
     * Loads songs from a CSV file.
     * Assumes the CSV has headers and specific column names.
     * Columns used: "SongName", "ArtistName", "Duration_ms", "Popularity"
     * Other columns like Valence, Tempo, Danceability, Energy, Loudness,
     * Speechiness, Acousticness, Instrumentalness, Liveness will be ignored
     * as they are not used by the Song model or the current parsing logic.
     *
     * @param filePath The path to the CSV file (e.g., "data/SpotifySongs.csv").
     * @return A list of Song objects.
     * Time Complexity: O(R * C) where R is number of rows, C is number of columns (for parsing each line).
     * Space Complexity: O(R) for storing all Song objects.
     */
    public static List<Song> loadSongs(String filePath) {
        List<Song> songs = new ArrayList<>();
        String line;
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("CSV file is empty: " + filePath);
                return songs;
            }
            String[] headers = headerLine.split(cvsSplitBy);
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim(), i);
            }

            // Get indices only for the columns we actually care about for the Song object
            Integer songNameIdx = headerMap.get("SongName");
            Integer artistNameIdx = headerMap.get("ArtistName");
            Integer durationMsIdx = headerMap.get("Duration_ms");
            Integer popularityIdx = headerMap.get("Popularity");

            // Check if all required columns for the Song object exist
            if (songNameIdx == null || artistNameIdx == null || durationMsIdx == null || popularityIdx == null) {
                System.err.println("CSV file missing one or more required columns for Song object: SongName, ArtistName, Duration_ms, Popularity.");
                return songs;
            }

            long idCounter = 1;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy, -1); // -1 to handle trailing empty strings (empty columns)

                // Determine the maximum index we need to access for the song properties
                int maxRequiredIdx = Math.max(songNameIdx, Math.max(artistNameIdx, Math.max(durationMsIdx, popularityIdx)));

                if (data.length <= maxRequiredIdx) {
                    System.err.println("Skipping incomplete row (not enough columns for essential data): " + line);
                    continue;
                }

                try {
                    String title = data[songNameIdx].trim();
                    String artist = data[artistNameIdx].trim();
                    long durationMs = Long.parseLong(data[durationMsIdx].trim());
                    int popularity = Integer.parseInt(data[popularityIdx].trim());

                    // Simple mapping of popularity to 1-5 star rating
                    int rating = 1;
                    if (popularity >= 80) rating = 5;
                    else if (popularity >= 60) rating = 4;
                    else if (popularity >= 40) rating = 3;
                    else if (popularity >= 20) rating = 2;

                    String songId = title.replaceAll("\\s+", "") + artist.replaceAll("\\s+", "") + (idCounter++);
                    songs.add(new Song(songId, title, artist, durationMs, rating));

                } catch (NumberFormatException e) {
                    System.err.println("Skipping row due to number format error (check Duration_ms or Popularity column): " + line + " - " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    // This catch might still occur if split(csvSplitBy, -1) doesn't perfectly align with expectation
                    System.err.println("Skipping row due to unexpected column count (ArrayIndexOutOfBounds): " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file at " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
        return songs;
    }
}