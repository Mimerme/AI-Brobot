package io.github.mimerme.dobotsPort;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

public class BlowFish {
    private static final int f10N = 16;
    private int[] f11P;
    private int[][] f12S;
    private boolean bInitDataLoaded;

    public BlowFish(InputStream is) {
        this.f11P = new int[18];
        this.f12S = (int[][]) Array.newInstance(Integer.TYPE, new int[]{4, 0x00000100});
        this.bInitDataLoaded = false;
        loadInitData(is);
    }

    public BlowFish(int[] P, int[][] S) {
        this.f11P = new int[18];
        this.f12S = (int[][]) Array.newInstance(Integer.TYPE, new int[]{4, 0x00000100});
        this.bInitDataLoaded = false;
        loadInitData(P, S);
    }

    private int m1F(int x) {
        int d = (short) (x & 255);
        x >>= 8;
        int c = (short) (x & 255);
        x >>= 8;
        return ((this.f12S[0][(short) ((x >> 8) & 255)] + this.f12S[1][(short) (x & 255)]) ^ this.f12S[2][c]) + this.f12S[3][d];
    }

    public void encipher(int[] left, int[] right) {
        int temp;
        int Xl = left[0];
        int Xr = right[0];
        for (int i = 0; i < f10N; i++) {
            Xl ^= this.f11P[i];
            temp = Xl;
            Xl = Xr ^ m1F(Xl);
            Xr = temp;
        }
        temp = Xl;
        Xl = Xr;
        Xr = temp ^ this.f11P[f10N];
        left[0] = Xl ^ this.f11P[17];
        right[0] = Xr;
    }

    public void decipher(int[] left, int[] right) {
        int temp;
        int Xl = left[0];
        int Xr = right[0];
        for (int i = 17; i > 1; i--) {
            Xl ^= this.f11P[i];
            temp = Xl;
            Xl = Xr ^ m1F(Xl);
            Xr = temp;
        }
        temp = Xl;
        Xl = Xr;
        Xr = temp ^ this.f11P[1];
        left[0] = Xl ^ this.f11P[0];
        right[0] = Xr;
    }

    private int loadInitData(InputStream is) {
        DataInputStream dis = new DataInputStream(is);
        if (dis == null) {
            return -1;
        }
        int i = 0;
        while (i < 18) {
            try {
                this.f11P[i] = dis.readInt();
                i++;
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        for (i = 0; i < 4; i++) {
            int j = 0;
            while (j < 0x00000100) {
                try {
                    this.f12S[i][j] = dis.readInt();
                    j++;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return -1;
                }
            }
        }
        try {
            dis.close();
        } catch (IOException e22) {
            e22.printStackTrace();
        }
        this.bInitDataLoaded = true;
        return 0;
    }

    private int loadInitData(int[] P, int[][] S) {
        this.f11P = (int[]) P.clone();
        for (int i = 0; i < S.length; i++) {
            this.f12S[i] = (int[]) S[i].clone();
        }
        this.bInitDataLoaded = true;
        return 0;
    }

    public int initialize(byte[] key, int keybytes) {
        int[] datal = new int[1];
        int[] datar = new int[1];
        if (!this.bInitDataLoaded) {
            return -1;
        }
        int i;
        int j = 0;
        for (i = 0; i < 18; i++) {
            int data = 0;
            for (int k = 0; k < 4; k++) {
                data = (data << 8) | key[j];
                j++;
                if (j >= keybytes) {
                    j = 0;
                }
            }
            this.f11P[i] = this.f11P[i] ^ data;
        }
        datal[0] = 0;
        datar[0] = 0;
        for (i = 0; i < 18; i += 2) {
            encipher(datal, datar);
            this.f11P[i] = datal[0];
            this.f11P[i + 1] = datar[0];
        }
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 0x00000100; j += 2) {
                encipher(datal, datar);
                this.f12S[i][j] = datal[0];
                this.f12S[i][j + 1] = datar[0];
            }
        }
        return 0;
    }
}
