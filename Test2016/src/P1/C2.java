package P1;

public class C2 extends C1{
    public C2() {}
    public C2(int a, int b) {
        setA(a); // PREVIOUS: this.a = a;
        this.b = b;
    }

    public static int c() {
        return 12;
    }
    public C1 gen() {
        return new C3();
    }
}
