package ratings;

import models.Song;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [cite_start]Implements a Binary Search Tree (BST) for indexing songs by user rating (1-5 stars). [cite: 29, 33]
 * [cite_start]Each node in the BST represents a rating bucket and stores multiple songs with that rating. [cite: 34]
 * [cite_start]Supports fast insertion, deletion, and search by rating. [cite: 31]
 * Time and Space complexities are annotated.
 */
public class SongRatingTree {
    private BSTNode root;

    public SongRatingTree() {
        this.root = null;
    }

    /**
     * [cite_start]Inserts a song into the BST based on its rating. [cite: 35]
     * If a node for the given rating already exists, the song is added to that node's list.
     * Otherwise, a new node is created.
     * Time Complexity: O(log N) on average for balanced tree, O(N) in worst case (skewed tree).
     * Space Complexity: O(1) for new node if created, O(log N) for call stack in recursion.
     * @param song The song to insert.
     * @param rating The rating of the song (1-5).
     */
    public void insertSong(Song song, int rating) {
        if (rating < 1 || rating > 5) {
            System.out.println("Warning: Rating must be between 1 and 5. Song '" + song.getTitle() + "' not inserted.");
            return;
        }
        root = insertRecursive(root, song, rating);
        song.setRating(rating); // Update song object's rating
    }

    private BSTNode insertRecursive(BSTNode current, Song song, int rating) {
        if (current == null) {
            return new BSTNode(rating, song);
        }

        if (rating < current.rating) {
            current.left = insertRecursive(current.left, song, rating);
        } else if (rating > current.rating) {
            current.right = insertRecursive(current.right, song, rating);
        } else {
            // Rating already exists, add song to this node's list
            current.songs.add(song);
        }
        return current;
    }

    /**
     * [cite_start]Searches for all songs with a specific rating. [cite: 35]
     * Time Complexity: O(log N) on average for balanced tree, O(N) in worst case (skewed tree)
     * + O(S) where S is the number of songs with that rating.
     * Space Complexity: O(S) for the list of songs returned, O(log N) for call stack.
     * @param rating The rating to search for (1-5).
     * @return A list of songs with the specified rating, or an empty list if none found.
     */
    public List<Song> searchByRating(int rating) {
        if (rating < 1 || rating > 5) {
            System.out.println("Warning: Rating must be between 1 and 5.");
            return new ArrayList<>();
        }
        BSTNode node = searchNode(root, rating);
        return (node != null) ? new ArrayList<>(node.songs) : new ArrayList<>();
    }

    private BSTNode searchNode(BSTNode current, int rating) {
        if (current == null || current.rating == rating) {
            return current;
        }
        if (rating < current.rating) {
            return searchNode(current.left, rating);
        } else {
            return searchNode(current.right, rating);
        }
    }

    /**
     * [cite_start]Deletes a specific song from the tree based on its ID. [cite: 36]
     * This method assumes you want to remove a specific instance of a song.
     * If the song is the only one in its rating bucket, the node might be removed or adjusted.
     * Time Complexity: O(log N + S) on average for balanced tree, O(N + S) in worst case,
     * where S is the number of songs in the target bucket.
     * Space Complexity: O(log N) for call stack.
     * @param songId The ID of the song to delete.
     * @return true if the song was found and deleted, false otherwise.
     */
    public boolean deleteSong(String songId) {
        // First, find the node that *could* contain the song.
        // This requires traversing the tree and then iterating through the songs list.
        return deleteSongRecursive(root, songId, null, false); // Using a helper for parent tracking
    }

    private boolean deleteSongRecursive(BSTNode current, String songId, BSTNode parent, boolean isLeftChild) {
        if (current == null) {
            return false;
        }

        boolean foundAndRemoved = false;
        // Iterate through songs in the current node's bucket
        for (int i = 0; i < current.songs.size(); i++) {
            if (current.songs.get(i).getId().equals(songId)) {
                current.songs.remove(i); // Remove the song
                foundAndRemoved = true;
                break; // Assuming unique song ID per bucket, or remove all matches
            }
        }

        // If the song was in this node's list and the list is now empty, we might need to remove the node
        if (foundAndRemoved && current.songs.isEmpty()) {
            // Logic to remove the node from the BST
            if (current == root) {
                root = deleteNode(root);
            } else if (isLeftChild) {
                parent.left = deleteNode(current);
            } else {
                parent.right = deleteNode(current);
            }
            return true;
        }

        // If not found in current node, try left or right subtrees
        if (current.left != null) {
            if (deleteSongRecursive(current.left, songId, current, true)) {
                return true;
            }
        }
        if (current.right != null) {
            if (deleteSongRecursive(current.right, songId, current, false)) {
                return true;
            }
        }
        return foundAndRemoved;
    }

    // Helper for actual BST node deletion (standard BST deletion logic)
    private BSTNode deleteNode(BSTNode node) {
        if (node.left == null && node.right == null) {
            return null; // No children
        }
        if (node.left == null) {
            return node.right; // Only right child
        }
        if (node.right == null) {
            return node.left; // Only left child
        }

        // Node with two children: Get the in-order successor (smallest in the right subtree)
        BSTNode smallestValueNode = findSmallestValue(node.right);
        node.rating = smallestValueNode.rating; // Replace current node's rating with successor's
        node.songs = new ArrayList<>(smallestValueNode.songs); // Copy songs as well
        node.right = deleteNode(node.right); // Delete the in-order successor from its original position
        return node;
    }

    private BSTNode findSmallestValue(BSTNode root) {
        return root.left == null ? root : findSmallestValue(root.left);
    }

    /**
     * Gets a map of song counts by rating. [cite_start]Useful for the dashboard. [cite: 77]
     * Time Complexity: O(N) where N is the number of nodes in the BST, as it's a traversal.
     * Space Complexity: O(R) where R is the number of distinct ratings (max 5), plus O(H) for recursion stack.
     * @return A map where keys are ratings (1-5) and values are the count of songs for that rating.
     */
    public Map<Integer, Integer> getSongCountByRating() {
        Map<Integer, Integer> counts = new HashMap<>();
        inOrderTraversalForCounts(root, counts);
        return counts;
    }

    private void inOrderTraversalForCounts(BSTNode node, Map<Integer, Integer> counts) {
        if (node != null) {
            inOrderTraversalForCounts(node.left, counts);
            counts.put(node.rating, counts.getOrDefault(node.rating, 0) + node.songs.size());
            inOrderTraversalForCounts(node.right, counts);
        }
    }
}