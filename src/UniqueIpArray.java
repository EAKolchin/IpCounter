
import java.util.stream.Stream;

public class UniqueIpArray {
    private static final int ArraySize = 134217728; // 2^32 / 32
    private static final int MutexSize = 1024;
    private final int[] array;

    private final Object[] mutexes;

    public UniqueIpArray() {
        array = new int[ArraySize];
        mutexes = Stream.generate(Object::new).limit(MutexSize).toArray();
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
        int flag = 1 << Math.toIntExact(ip % 32);
        int mutexIndex = Math.toIntExact(ip % MutexSize);
        if ((array[index] & flag) == 0) {
            synchronized (mutexes[mutexIndex]) {
                array[index] |= flag;
            }
        }
    }
}
