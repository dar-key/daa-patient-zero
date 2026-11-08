import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in
 * person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of
 * people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at
     * least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price
     * of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the
     * smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the
     * children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the
     * cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed")
     * and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        if (n == 0)
            return new boolean[0];

        // Root at person 0 and obtain parent pointers + BFS order
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int[] order = new int[n];
        int head = 0, tail = 0;

        order[tail++] = 0;
        parent[0] = 0;

        while (head < tail) {
            int u = order[head++];
            for (int v : nb[u]) {
                if (parent[v] == -1) {
                    parent[v] = u;
                    order[tail++] = v;
                }
            }
        }

        // DP states:
        // take[v]: min cost of subtree when v IS tested
        // skip[v]: min cost of subtree when v is NOT tested
        long[] take = new long[n];
        long[] skip = new long[n];
        for (int v = 0; v < n; v++) {
            take[v] = cost[v];
            skip[v] = 0;
        }

        // Bottom-up pass from leaves to root
        for (int i = n - 1; i >= 1; i--) {
            int v = order[i];
            int p = parent[v];
            take[p] += Math.min(take[v], skip[v]);
            skip[p] += take[v]; // if parent is skipped, child v must be tested
        }

        // Reconstruct choice top-down from root
        boolean[] tested = new boolean[n];
        tested[0] = take[0] <= skip[0];

        for (int i = 1; i < n; i++) {
            int v = order[i];
            int p = parent[v];
            if (!tested[p]) {
                tested[v] = true; // parent was skipped, so child must be tested
            } else {
                tested[v] = take[v] <= skip[v]; // parent was tested, pick cheaper option for child
            }
        }

        return tested;
    }

    /**
     * The number of links on the longest chain of infections, that is the longest
     * path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need
     * the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3
     * has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        if (n <= 1)
            return 0;

        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int[] order = new int[n];
        int head = 0, tail = 0;

        order[tail++] = 0;
        parent[0] = 0;

        while (head < tail) {
            int u = order[head++];
            for (int v : nb[u]) {
                if (parent[v] == -1) {
                    parent[v] = u;
                    order[tail++] = v;
                }
            }
        }

        int[] top1 = new int[n]; // longest downward branch from v
        int[] top2 = new int[n]; // second longest downward branch from v
        int maxChain = 0;

        // Bottom-up pass
        for (int i = n - 1; i >= 0; i--) {
            int v = order[i];
            maxChain = Math.max(maxChain, top1[v] + top2[v]);

            if (v != 0) {
                int p = parent[v];
                int branchLen = top1[v] + 1;
                if (branchLen > top1[p]) {
                    top2[p] = top1[p];
                    top1[p] = branchLen;
                } else if (branchLen > top2[p]) {
                    top2[p] = branchLen;
                }
            }
        }

        return maxChain;
    }
}
