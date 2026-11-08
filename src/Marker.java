import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    public static int[] prefixFunction(String p) {
        int m = p.length();
        int[] pi = new int[m];
        if (m == 0)
            return pi;

        int k = 0;
        for (int i = 1; i < m; i++) {
            while (k > 0 && p.charAt(i) != p.charAt(k)) {
                k = pi[k - 1];
            }
            if (p.charAt(i) == p.charAt(k)) {
                k++;
            }
            pi[i] = k;
        }

        return pi;
    }

    public static int[] findAll(String text, String p) {
        int n = text.length();
        int m = p.length();
        if (m == 0 || n < m)
            return new int[0];

        int[] pi = prefixFunction(p);
        List<Integer> hits = new ArrayList<>();

        int k = 0;
        for (int i = 0; i < n; i++) {
            while (k > 0 && text.charAt(i) != p.charAt(k)) {
                k = pi[k - 1];
            }
            if (text.charAt(i) == p.charAt(k)) {
                k++;
            }
            if (k == m) {
                hits.add(i - m + 1);
                k = pi[m - 1];
            }
        }

        int[] res = new int[hits.size()];
        for (int i = 0; i < hits.size(); i++) {
            res[i] = hits.get(i);
        }
        return res;
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k
     * letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different
     * words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in
     * O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE
     * LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA,
     * GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        int n = a.length();
        int m = b.length();
        if (n < k || m < k)
            return 0;

        final long BASE = 131L;
        final long MOD = 1_000_000_007L;

        // Precompute BASE^(k - 1) % MOD for rolling removal
        long top = 1;
        for (int i = 1; i < k; i++) {
            top = (top * BASE) % MOD;
        }

        // Store hashes of windows in b along with their starting indices
        Map<Long, List<Integer>> bHashes = new HashMap<>();

        // Compute rolling hash for windows of b
        long hb = 0;
        for (int i = 0; i < k; i++) {
            hb = (hb * BASE + b.charAt(i)) % MOD;
        }
        bHashes.computeIfAbsent(hb, key -> new ArrayList<>()).add(0);

        for (int j = 1; j + k <= m; j++) {
            hb = (hb - b.charAt(j - 1) * top % MOD + MOD) % MOD;
            hb = (hb * BASE + b.charAt(j + k - 1)) % MOD;
            bHashes.computeIfAbsent(hb, key -> new ArrayList<>()).add(j);
        }

        // Slide window of length k over string a
        long ha = 0;
        for (int i = 0; i < k; i++) {
            ha = (ha * BASE + a.charAt(i)) % MOD;
        }

        int count = 0;
        for (int i = 0; i + k <= n; i++) {
            if (i > 0) {
                ha = (ha - a.charAt(i - 1) * top % MOD + MOD) % MOD;
                ha = (ha * BASE + a.charAt(i + k - 1)) % MOD;
            }

            List<Integer> bPositions = bHashes.get(ha);
            if (bPositions != null) {
                // Hash matched: compare letters directly to guard against hash collisions
                boolean found = false;
                for (int bPos : bPositions) {
                    boolean equal = true;
                    for (int p = 0; p < k; p++) {
                        if (a.charAt(i + p) != b.charAt(bPos + p)) {
                            equal = false;
                            break;
                        }
                    }
                    if (equal) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    count++;
                }
            }
        }

        return count;
    }
}
