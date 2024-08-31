package byow.Core;

public class RoomNode {
    public final int X;
    public final int Y;
    public RoomNode(int x, int y) {
        X = x;
        Y = y;
    }
    public int hashCode() {
        String identity = "X = " + X + "Y = " + Y;
        return identity.hashCode();
    }
}
