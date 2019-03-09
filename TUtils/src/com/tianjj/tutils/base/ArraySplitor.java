package com.tianjj.tutils.base;

import java.util.ArrayList;

/**
 * An integer array splitting util.
 */
public class ArraySplitor extends ArrayList<int[]> {
    private static int mRawLen;
    private static int mRawGap;

    private ArraySplitor(int[][] ints) {
        super();
        final boolean ext = mRawLen % mRawGap != 0;

        for (int i = 0; i < ints.length; i++) {
            if (ext && i == ints.length - 1) {
                int[] lastTmp = new int[mRawLen % mRawGap];
                System.arraycopy(ints[i], 0, lastTmp, 0, mRawLen % mRawGap);
                add(lastTmp);
                break;
            }
            add(ints[i]);
        }
    }

    /**
     * Splitting a integer array with a specific gap.
     * @param arr The integer array will be splitted.
     * @param gap The splitting step length.
     * @return An {@link ArraySplitor}, which contains all sub-arrays after splitting.
     */
    public static ArraySplitor split(int[] arr, int gap) {
        return new ArraySplitor(arrSplit(arr, gap));
    }

    private static int[][] arrSplit(int[] arr, int i) {
        int len = arr.length;
        mRawLen = len;
        mRawGap = i;

        int slice = (len / i) + (len % i != 0 ? 1 : 0);

        int[][] tmp = new int[slice][i];
        {
            int iIndex = 0;
            int t = 0;
            int cpLen = i;

            while (t < slice) {
                if (iIndex + i > len) {
                    cpLen = len - iIndex;
                }

                System.arraycopy(arr, iIndex, tmp[t], 0, cpLen);
                iIndex += i;
                t++;
            }
        }
        return tmp;
    }
}
