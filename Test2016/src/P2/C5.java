package P2;

public class C5 {
    public static void main(String[] args) {
        P1.C1 y = new P1.C1(); // PREVIOUS: P1.C1 y = new C1();
        //P1.C2 x = new C4(13, 14);
        //P1.C1 q = x.gen();

        P2.C4 x = new C4(13, 14);
        System.out.println(x);
//        System.out.println(q + ", " + x);
//        System.out.println(x);
//        System.out.println((P1.C2) y);
//        System.out.println(y);
    }
}
