package com.stevekung.ingameshadow;

public class SeedUtils
{
    private static final long MULTIPLIER = 6364136223846793005L;
    private static final long ADDEND = 1442695040888963407L;

    public static long getShadowSeed(long worldSeed)
    {
        var nextSeed = mixSeed(worldSeed);
        return unmixSeed(nextSeed, Solution.of(~worldSeed));
    }

    private static long mixSeed(long seed)
    {
        seed *= seed * MULTIPLIER + ADDEND;
        return seed;
    }

    private static long unmixSeed(long seed, Solution solution)
    {
        long r = solution.ordinal();
        int j;

        for (j = 1; j < 64; j <<= 1)
        {
            r -= (MULTIPLIER * r * r + ADDEND * r - seed) * modInverse(2L * MULTIPLIER * r + ADDEND, 64);
        }
        return r;
    }

    private static long pow2(int bits)
    {
        return 1L << bits;
    }

    private static long mask(int bits)
    {
        if (bits >= 64)
        {
            return -1L;
        }
        return pow2(bits) - 1L;
    }

    private static long modInverse(long a, int k)
    {
        var x = ((a << 1L ^ a) & 0x4L) << 1L ^ a;
        x += x - a * x * x;
        x += x - a * x * x;
        x += x - a * x * x;
        x += x - a * x * x;
        return x & mask(k);
    }

    private enum Solution
    {
        EVEN,
        ODD;

        private static Solution of(long n)
        {
            return values()[(int) (n & 0x1L)];
        }
    }
}