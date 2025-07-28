import java.math.BigInteger;
import java.util.*;

public class SecretFinder {
    static class P {
        BigInteger x;
        int b;
        String v;
        P(int x, int b, String v) {
            this.x = BigInteger.valueOf(x);
            this.b = b;
            this.v = v;
        }
    }

    // convert number given in base to decimal
    static BigInteger toDec(String val, int base) {
        return new BigInteger(val, base);
    }

    // find f(0) using lagrange interpolation
    static BigInteger lag0(List<BigInteger> xs, List<BigInteger> ys) {
        int k = xs.size();
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < k; i++) {
            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;
            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                num = num.multiply(xs.get(j).negate());
                den = den.multiply(xs.get(i).subtract(xs.get(j)));
            }
            res = res.add(ys.get(i).multiply(num).divide(den));
        }
        return res;
    }

    // helper to get all k-combinations
    static void comb(int n, int k, int st, List<Integer> cur, List<int[]> all) {
        if (cur.size() == k) {
            int[] arr = new int[k];
            for (int i = 0; i < k; i++) arr[i] = cur.get(i);
            all.add(arr);
            return;
        }
        for (int i = st; i < n; i++) {
            cur.add(i);
            comb(n, k, i + 1, cur, all);
            cur.remove(cur.size() - 1);
        }
    }

    // process one test case
    static void solve(int n, int k, List<P> pts, String name) {
        List<BigInteger> xs = new ArrayList<>();
        List<BigInteger> ys = new ArrayList<>();
        for (P p : pts) {
            xs.add(p.x);
            ys.add(toDec(p.v, p.b));
        }

        // generate all combinations
        List<int[]> all = new ArrayList<>();
        comb(xs.size(), k, 0, new ArrayList<>(), all);

        // store frequency of f(0)
        Map<BigInteger, Integer> freq = new HashMap<>();
        for (int[] arr : all) {
            List<BigInteger> cx = new ArrayList<>();
            List<BigInteger> cy = new ArrayList<>();
            for (int id : arr) {
                cx.add(xs.get(id));
                cy.add(ys.get(id));
            }
            BigInteger sec = lag0(cx, cy);
            freq.put(sec, freq.getOrDefault(sec, 0) + 1);
        }

        // find the most frequent secret
        BigInteger ans = null;
        int mx = -1;
        for (Map.Entry<BigInteger, Integer> e : freq.entrySet()) {
            if (e.getValue() > mx) {
                mx = e.getValue();
                ans = e.getKey();
            }
        }
        System.out.println("Secret for " + name + " = " + ans);
    }

    public static void main(String[] args) {
        // first test case
        List<P> t1 = Arrays.asList(
            new P(1,10,"4"),
            new P(2,2,"111"),
            new P(3,10,"12"),
            new P(6,4,"213")
        );
        solve(4, 3, t1, "testcase1");

        // second test case
        List<P> t2 = Arrays.asList(
            new P(1, 6, "13444211440455345511"),
            new P(2, 15, "aed7015a346d63"),
            new P(3, 15, "6aeeb69631c227c"),
            new P(4, 16, "e1b5e05623d881f"),
            new P(5, 8, "316034514573652620673"),
            new P(6, 3, "2122212201122002221120200210011020220200"),
            new P(7, 3, "20120221122211000100210021102001201112121"),
            new P(8, 6, "20220554335330240002224253"),
            new P(9, 12, "45153788322a1255483"),
            new P(10, 7, "1101613130313526312514143")
        );
        solve(10, 7, t2, "testcase2");
    }
}