package gh2;

// TODO: uncomment the following import once you're ready to start this portion
import deque.ArrayDeque;
import deque.Deque;
import deque.LinkedListDeque;
import org.junit.Test;
import static org.junit.Assert.*;
// TODO: maybe more imports

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    // TODO: uncomment the following line once you're ready to start this portion
private Deque<Double> buffer;


    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int Capacity = (int) Math.round(SR / frequency);
        buffer = new LinkedListDeque<>();
        for (int i = 0; i < Capacity; i ++){
            buffer.addLast(0.0);
        }
        System.out.println(buffer.size());
        assertEquals(Capacity, buffer.size());
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        // TODO: Dequeue everything in buffer, and replace with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
        int Capacity = buffer.size();
        for (int i = 0; i < buffer.size(); i++){
            buffer.removeFirst();
            buffer.addLast((Math.random() - 0.5));
        }
        assertEquals(Capacity, buffer.size());
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        // TODO: Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       **Do not call StdAudio.play().**
        double removed = buffer.removeFirst();
        double newDouble = (removed * buffer.get(0)) * 0.5 * DECAY;
        buffer.addLast(newDouble);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        // TODO: Return the correct thing.;
        System.out.println(buffer.get(0));
        return buffer.get(0);
    }
}
