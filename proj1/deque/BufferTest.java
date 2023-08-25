import deque.ArrayDeque;
import deque.Deque;
import java.nio.Buffer;
import java.nio.DoubleBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

public class BufferTest {



    @Test
    public void BufferCalculationSpeedTest() {
        double[] D1 = new double[100];
        int length = D1.length;
        DoubleBuffer DF = DoubleBuffer.wrap(D1);
        for (int i = 0; i< length; i++){
            for (int i1 = 0; i1< length; i1++){
                for (int i2 = 0; i2< length; i2++) {
                    D1[i2] = D1[i2] + 2000.00;
                }
            }
        }
        System.out.println(DF.get(99));
        assertTrue(D1[99] > 0);
    }

    @Test
    public void NormalCalculationSpeedTest() {
         double[] D2 = new double[100];
        int length = D2.length;
        for (int i = 0; i< length; i++){
            for (int i1 = 0; i1< length; i1++){
                for (int i2 = 0; i2< length; i2++) {
                    D2[i2] = D2[i2] + 2000.00;
                }
            }
        }
        System.out.println(D2[99]);
        assertTrue(D2[99] > 0);
    }
    @Test
    public void SmallTest(){
        double s1 = 0.001;
        double s2 = 0.002;
        double s3 = 0.003;
        DoubleBuffer DB = DoubleBuffer.allocate(20);
        DB.put(s1);
        s1 = 0.004;
        System.out.println(DB.get(0));
    }
}
