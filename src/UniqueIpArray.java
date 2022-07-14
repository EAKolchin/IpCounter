public class UniqueIpArray {
    private static final int ArraySize = 134217728; // 2^32 / 32
    private final int[] array;

    public UniqueIpArray() {
        array = new int[ArraySize];
    }

    public long getCount() {
        long result = 0;
        int num;
        for (int i = 0; i < ArraySize; i++) {
            num = array[i];
            while (num != 0) {
                num = num & (num - 1);
                result++;
            }
        }
        return result;
    }

    public void addIp(long ip) {
        int index = Math.toIntExact(ip / 32);
        int bitIndex = Math.toIntExact(ip % 32);
        //synchronized (this) { // Ten times faster without synchronization
            array[index] |= 1 << bitIndex;
        //}
    }
}
