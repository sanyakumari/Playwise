import models.Song;

import playlist.Playlist;
import playback.PlaybackStack;
import ratings.SongRatingTree;
import search.SongHashMap;
import sorting.MergeSort;
import utils.CSVLoader;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static Playlist currentPlaylist = new Playlist();
    private static PlaybackStack playbackHistory = new PlaybackStack();
    private static SongRatingTree songRatingTree = new SongRatingTree();
    private static SongHashMap songLookup = new SongHashMap();

    public static void main(String[] args) {
        System.out.println("Welcome to PlayWise Music Engine!");

        List<Song> songs = CSVLoader.loadSongs("data/SpotifySongs.csv");
        if (songs.isEmpty()) {
            System.out.println("No songs loaded. Please ensure 'data/SpotifySongs.csv' exists and is correctly formatted.");
            return;
        }

        System.out.println("Loaded " + songs.size() + " songs.");

        for (Song song : songs) {
            if (currentPlaylist.getSize() < 100) {
                currentPlaylist.addSong(song);
            }
            songLookup.addSong(song);
            int randomRating = (int) (Math.random() * 5) + 1;
            songRatingTree.insertSong(song, randomRating);
        }

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- PlayWise Menu ---");
            System.out.println("1. View Current Playlist");
            System.out.println("2. Add Song to Playlist");
            System.out.println("3. Delete Song from Playlist");
            System.out.println("4. Move Song in Playlist");
            System.out.println("5. Reverse Playlist");
            System.out.println("6. Play a Song (Adds to History)");
            System.out.println("7. Undo Last Played Song");
            System.out.println("8. Search Song by Title/ID");
            System.out.println("9. Search Songs by Rating");
            System.out.println("10. Sort Current Playlist");
            System.out.println("11. Get System Snapshot (Dashboard)");
            System.out.println("12. Pin a Song at Position");
            System.out.println("13. Shuffle Playlist (Keep Pinned Songs Fixed)");
            System.out.println("14. Generate Playlist Summary");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    currentPlaylist.displayPlaylist();
                    break;
                case 2:
                    System.out.print("Enter Song or song ID (for existing songs) or create a new one: ");
                    String songIdentifier = scanner.nextLine();
                    Song songToAdd = songLookup.searchSong(songIdentifier);
                    if (songToAdd == null) {
                        System.out.println("Song not found. Creating new song.");
                        System.out.print("Title: ");
                        String title = scanner.nextLine();
                        System.out.print("Artist: ");
                        String artist = scanner.nextLine();
                        System.out.print("Duration (ms): ");
                        long duration = scanner.nextLong();
                        scanner.nextLine();
                        songToAdd = new Song(title + artist + System.currentTimeMillis(), title, artist, duration, 0);
                        songLookup.addSong(songToAdd);
                    }
                    currentPlaylist.addSong(songToAdd);
                    System.out.println("Song added.");
                    break;
                case 3:
                    System.out.print("Index to delete (1-based): ");
                    int deleteIndex = scanner.nextInt();
                    scanner.nextLine();
                    int zeroBasedIndex = deleteIndex - 1;
                    if (zeroBasedIndex >= 0 && zeroBasedIndex < currentPlaylist.getSize()) {
                        currentPlaylist.deleteSong(zeroBasedIndex);
                        System.out.println("Song at position " + deleteIndex + " deleted.");
                    } else {
                         System.out.println("Invalid index.");
                    }
                    break;
                case 4:
                    System.out.print("From index: ");
                    int from = scanner.nextInt();
                    System.out.print("To index: ");
                    int to = scanner.nextInt();
                    scanner.nextLine();
                    currentPlaylist.moveSong(from, to);
                    System.out.println("Song moved.");
                    break;
                case 5:
                    currentPlaylist.reversePlaylist();
                    System.out.println("Playlist reversed.");
                    break;
                case 6:
                    System.out.print("Enter index to play: ");
                    int playIndex = scanner.nextInt();
                    scanner.nextLine();
                    Song played = currentPlaylist.getSongAtIndex(playIndex);
                    if (played != null) {
                        playbackHistory.push(played);
                        System.out.println("Played: " + played.getTitle());
                    } else {
                        System.out.println("Invalid index.");
                    }
                    break;
                case 7:
                    Song last = playbackHistory.pop();
                    if (last != null) {
                        currentPlaylist.addSong(last);
                        System.out.println("Undo: " + last.getTitle() + " re-added.");
                    } else {
                        System.out.println("No history to undo.");
                    }
                    break;
                case 8:
                    System.out.print("Enter title or ID: ");
                    String query = scanner.nextLine();
                    Song found = songLookup.searchSong(query);
                    System.out.println(found != null ? "Found: " + found : "Not found.");
                    break;
                case 9:
                    System.out.print("Enter rating (1-5): ");
                    int rate = scanner.nextInt();
                    scanner.nextLine();
                    List<Song> rated = songRatingTree.searchByRating(rate);
                    if (!rated.isEmpty()) {
                        rated.forEach(System.out::println);
                    } else {
                        System.out.println("No songs found with rating " + rate);
                    }
                    break;
                case 10:
                    System.out.println("Sort by: 1. Title 2. Duration Asc 3. Duration Desc");
                    int sortChoice = scanner.nextInt();
                    scanner.nextLine();
                    List<Song> toSort = currentPlaylist.getAllSongsAsList();
                    MergeSort ms = new MergeSort();
                    switch (sortChoice) {
                        case 1: ms.sort(toSort, "title"); break;
                        case 2: ms.sort(toSort, "duration_asc"); break;
                        case 3: ms.sort(toSort, "duration_desc"); break;
                        default: System.out.println("Invalid."); return;
                    }
                    currentPlaylist.clear();
                    for (Song s : toSort) currentPlaylist.addSong(s);
                    System.out.println("Playlist sorted.");
                    currentPlaylist.displayPlaylist();
                    break;
                case 11:
                    System.out.println("\n--- System Snapshot ---");
                    List<Song> all = currentPlaylist.getAllSongsAsList();
                    new MergeSort().sort(all, "duration_desc");
                    System.out.println("Top 5 Longest Songs:");
                    for (int i = 0; i < Math.min(5, all.size()); i++) {
                        Song s = all.get(i);
                        System.out.println((i + 1) + ". " + s.getTitle() + " (" + s.getDurationMs() + "ms)");
                    }

                    System.out.println("\nRecent Plays:");
                    List<Song> recent = playbackHistory.getRecentSongs(5);
                    for (int i = recent.size() - 1; i >= 0; i--) {
                        System.out.println((recent.size() - i) + ". " + recent.get(i).getTitle());
                    }

                    System.out.println("\nRating Counts:");
                    songRatingTree.getSongCountByRating().forEach((r, c) ->
                            System.out.println("Rating " + r + ": " + c + " songs"));
                    break;
                case 12:
                    System.out.print("Enter index to pin: ");
                    int pinIndex = scanner.nextInt();
                    scanner.nextLine();
                    currentPlaylist.pinSongAtPosition(pinIndex);
                    System.out.println("Song pinned at position " + pinIndex);
                    break;
                case 13:
                    currentPlaylist.shuffleWithPinned();
                    System.out.println("Playlist shuffled (pinned songs preserved).");
                    currentPlaylist.displayPlaylist();
                    break;
                case 14:
                    currentPlaylist.generateSummary();
                    break;
                case 0:
                    System.out.println("Exiting PlayWise. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
