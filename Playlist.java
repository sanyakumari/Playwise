package playlist;

import models.Song;
import java.util.*;

public class Playlist {
    private DoublyLinkedList<Song> songs;
    private Map<Integer, Song> pinnedPositions; // position -> Song

    public Playlist() {
        this.songs = new DoublyLinkedList<>();
        this.pinnedPositions = new HashMap<>();
    }

    public void addSong(Song song) {
        songs.addLast(song);
    }

    public void deleteSong(int index) {
        if (index >= 0 && index < songs.getSize()) {
            Song removed = songs.get(index);
            pinnedPositions.values().remove(removed); // Remove from pinned if present
            songs.delete(index);
        } else {
            System.out.println("Invalid index for deletion.");
        }
    }

    public void moveSong(int fromIndex, int toIndex) {
        if (fromIndex >= 0 && fromIndex < songs.getSize() &&
            toIndex >= 0 && toIndex < songs.getSize()) {
            songs.move(fromIndex, toIndex);
        } else {
            System.out.println("Invalid indices for move operation.");
        }
    }

    public void reversePlaylist() {
        songs.reverse();
    }

    public void displayPlaylist() {
        if (songs.isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }
        System.out.println("\n--- Current Playlist ---");
        for (int i = 0; i < songs.getSize(); i++) {
            Song song = songs.get(i);
            String pinInfo = pinnedPositions.containsKey(i) ? " (Pinned)" : "";
            System.out.println((i + 1) + ". " + song.getTitle() + " by " + song.getArtist() + pinInfo);
        }
        System.out.println("------------------------");
    }

    public Song getSongAtIndex(int index) {
        if (index >= 0 && index < songs.getSize()) {
            return songs.get(index);
        }
        return null;
    }

    public int getSize() {
        return songs.getSize();
    }

    public List<Song> getAllSongsAsList() {
        return songs.toList();
    }

    public void clear() {
        songs.clear();
        pinnedPositions.clear();
    }

    // ✅ Feature 1: Shuffle with Pinned Positions
    public void pinSongAtPosition(int index) {
        if (index >= 0 && index < songs.getSize()) {
            pinnedPositions.put(index, songs.get(index));
        } else {
            System.out.println("Invalid index to pin.");
        }
    }

    public void shuffleWithPinned() {
        List<Song> current = songs.toList();
        List<Song> unpinned = new ArrayList<>();

        for (int i = 0; i < current.size(); i++) {
            if (!pinnedPositions.containsKey(i)) {
                unpinned.add(current.get(i));
            }
        }

        Collections.shuffle(unpinned);

        songs.clear();
        int upIndex = 0;
        for (int i = 0; i < current.size(); i++) {
            if (pinnedPositions.containsKey(i)) {
                songs.addLast(pinnedPositions.get(i));
            } else {
                songs.addLast(unpinned.get(upIndex++));
            }
        }
    }

    // ✅ Feature 2: Playlist Summary Generator
    public void generateSummary() {
        List<Song> list = songs.toList();
        int totalDuration = 0;
        Set<String> artists = new HashSet<>();

        for (Song s : list) {
            totalDuration += s.getDurationMs();
            artists.add(s.getArtist());
        }

        System.out.println("\n--- Playlist Summary ---");
        System.out.println("Total Songs: " + list.size());
        System.out.printf("Total Playtime: %.2f minutes\n", totalDuration / 60000.0);
        System.out.println("Unique Artists: " + artists.size());
        System.out.println("Genre information not available.\n--------------------------");
    }
}
