/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rorevmarkovchaingenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Matteo
 */
public class RoRevMarkovChainGenerator {

    /**
     * @param args the command line arguments
     */
    public static Tripla getChain(int n) {
        Tripla ret = new Tripla();
        ret.chain = new double[n][n];
        ret.pi = new double[n];
        ret.ro = new int[n];
        ArrayList<Integer> s = new ArrayList();
        ArrayList<Integer> appoggio = new ArrayList();
        Random gen = new Random();
        int a, b;
        for (int i = 0; i < n; i++) {
            ret.pi[i] = gen.nextDouble();
            appoggio.add(i);
            s.add(i);
            ret.pi[i] = 1;//DA TOGLIERE
        }
        for (int i = 0; i < n; i++) {
            ret.ro[i] = appoggio.remove(gen.nextInt(n - i));
        }
        a = s.get(0);
        s.remove(0);
        while (!s.isEmpty()) {
            System.out.println(s);
            int flag = 0;
            ArrayList<Integer> adj = new ArrayList();
            for (int j = 0; j < n; j++) {
                if (ret.chain[a][j] != 0 && s.contains(j)) {
                    adj.add(j);
                    s.remove((Integer) j);
                    flag++;
                }
            }
            if (flag != 0) {
                //s.remove((Integer) b);
                b = adj.get(gen.nextInt(flag));
            } else {
                b = s.get(gen.nextInt(s.size()));
            }
            rinomine(a, b, gen.nextDouble(), ret.pi, ret.chain, ret.ro);
            a = b;
            s.remove((Integer)b);
        }
        double max = 0.0;
        for (int i = 0; i < n; i++) {
            double somma = 0.0;
            for (int j = 0; j < n; j++) {
                somma += ret.chain[i][j];
            }
            if (somma >= max) {
                max = somma;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ret.chain[i][j] /= max;
            }
        }
        for (int i = 0; i < n; i++) {
            double cappio = 1.0;
            for (int j = 0; j < n; j++) {
                cappio -= ret.chain[i][j];
            }
            if (cappio > 0) {
                ret.chain[i][i] = cappio;//cappio con peso uguale a ciò che manca per avere la somma dei nodi uscenti pari a uno
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        int n = 4;
        long startTime = System.currentTimeMillis();
        long stopTime;
        long elapsedTime;
        Tripla tests[] = new Tripla[1000];
        for (int k = 0; k < 1000; k++) {
            Tripla chain = getChain(n);
            double archi[][] = chain.chain;
            double nodi[] = chain.pi;
            tests[k] = chain;
            for (int i = 0; i < n; i++) {
                System.out.print(nodi[i] + " ");
            }
            System.out.println("");
            for (int i = 0; i < n; i++) {
                System.out.print(i + "->" + chain.ro[i] + "/");
            }
            System.out.println("");
            System.out.println("");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.print(archi[i][j] + " | ");
                }
                System.out.println("");
            }
            System.out.println("---------------------------------------------");
        }
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time: " + elapsedTime + "ms");
    }

    private static void rinomine(int a, int b, double nextDouble, double[] pi, double[][] chain, int[] ro) {
        Random gen = new Random();
        int ap = ro[b];
        int bp = ro[a];
        int temp;
        chain[a][b] = gen.nextDouble();
        chain[ap][bp] = pi[a] * chain[a][b] / pi[ap];
        temp = ap;
        ap = ro[bp];
        bp = ro[temp];
        while (a != ap || b != bp) {
            chain[ap][bp] = gen.nextDouble();
            chain[ro[bp]][ro[ap]] = pi[ap] * chain[ap][bp] / pi[ro[bp]];
            temp = ap;
            ap = ro[bp];
            bp = ro[temp];
        }
    }

}

class Tripla {

    double pi[];
    double chain[][];
    int ro[];
}
