public class UniqueIpArray {
    private static final int ArraySize = 65536; // 2^32 / 32
    //private final int[] array;

    private boolean[][] array;

    public UniqueIpArray() {
        //array = new int[ArraySize];
        array = new boolean[65536][65536];
    }

    public long getCount() {
        long result = 0;
        int num;
        for (int i = 0; i < ArraySize; i++) {
            for (int j =0; j < ArraySize; j++) {
                if (array[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }

    public void addIp(long ip) {
        int i = Math.toIntExact(ip / ArraySize);
        int j = Math.toIntExact(ip % ArraySize);
        array[i][j] = true;
    }
}
