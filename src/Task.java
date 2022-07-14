import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Task implements Runnable {
    private final long startPosition;
    private final long endPosition;
    private final RandomAccessFile fileAccess;
    private final UniqueIpArray ipCounter;

    private static int count = 0;

    private static final int BUFFER_SIZE = 20480;

    public Task(long start, long end, RandomAccessFile file, UniqueIpArray counter) {
        startPosition = start;
        endPosition = end;
        fileAccess = file;
        ipCounter = counter;
    }

    @Override
    public void run() {
        System.out.println("Start task " + (count++));
        long ip = 0;
        int result = 0;
        long nextPosition = startPosition;
        int noOfBytesRead = BUFFER_SIZE;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        String str;
        byte tmp = 0; // Hope utf-8 encoding

        try {
            if (startPosition != 0) {
                fileAccess.seek(startPosition - 1);
                while (fileAccess.readUnsignedByte() != '\n') {
                    nextPosition++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileChannel chanel = fileAccess.getChannel();


        try {
            while (noOfBytesRead == BUFFER_SIZE) {
                noOfBytesRead = chanel.read(buffer);
                buffer.flip();

                while (buffer.hasRemaining()) {
                    tmp = buffer.get();
                    nextPosition++;

                    if (tmp == '.') {
                        //System.out.println("New Value: " + result);
                        ip = ip * 256 + result;
                        result = 0;
                    } else if (tmp == '\n') {
                        ip = ip * 256 + result;
                        //System.out.println("New IP: " + ip);
                        ipCounter.addIp(ip);
                        result = 0;
                        ip = 0;
                        if (nextPosition >= endPosition) {
                            return;
                        }
                    } else {
                        //System.out.println("New digit: " + (tmp - '0'));
                        result = result * 10 + (tmp - '0');
                    }
                }
                buffer.clear();
            }
            if (tmp != '\n') {
                ip = ip * 256 + result;
                ipCounter.addIp(ip);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
