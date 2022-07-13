import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Task implements Runnable {
    private long startPosition;
    private long endPosition;
    private RandomAccessFile fileAccess;
    private UniqueIpArray ipCounter;

    private static int count = 0;

    public Task(long start, long end, RandomAccessFile file, UniqueIpArray counter) {
        startPosition = start;
        endPosition = end;
        fileAccess = file;
        ipCounter = counter;
    }

    @Override
    public void run() {
        System.out.print("Start task " + (count++) + "\n");
        long ip = 0;
        int result = 0;
        long position = startPosition;
        int tmp;

        try {
            if (startPosition != 0)
            {
                fileAccess.seek(startPosition - 1);
                while (fileAccess.readUnsignedByte() != '\n') {
                    position++;
                }
            }
        } catch (Exception e) {
            System.out.print("Exception 1");
        }

        try {
            while (true) {
                tmp = fileAccess.readUnsignedByte();
                position++;
                if (tmp == '.') {
                    ip = ip * 256 + result;
                    result = 0;
                } else if (tmp == '\n') {
                    ip = ip * 256 + result;
                    ipCounter.addIp(ip);
                    result = 0;
                    ip = 0;
                    if (position >= endPosition) {
                        break;
                    }
                } else {
                    result = result * 10 + (tmp - '0');
                }
            }
        } catch (EOFException e) {
            ip = ip * 256 + result;
            ipCounter.addIp(ip);
        }
        catch (Exception e) {
            System.out.print("Exception 2");
        }
    }
}
