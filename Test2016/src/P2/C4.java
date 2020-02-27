package P2;

public class C4 extends P1.C2{
    public int getB() {
        return 2 * super.getB(); // PREVIOUS: return 2 * b;

    }
    public C4(int a, int b) {
        setA(a); // PREVIOUS: this.a = a;
        setB(b); // PREVIOUS: this.b = b;
    }

    public C4(int v) {
        setA(v); setB(v); // PREVIOUS: this.a = this.b = v;
    }
}
