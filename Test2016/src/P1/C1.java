package P1;

public class C1 {
    private int a;
    int b;
    public static int c() {
        return 11;
    }
    public void setA(int v) {a = v;}
    public void setB(int v) {b = v;}
    public int getA() {return a;}
    public int getB() {return b;}
    @Override
    public String toString() {
        return a + " " + getB() + " " + c();
    }
}

