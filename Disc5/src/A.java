class A{
    protected String name, noise;
    protected int age;
    public A(String name, int age) {
        this.name = name;
        this.age = age;
        this.noise = "Huh?";
    }
    public String makeNoise() {
        return noise;
    }
    public String greet() {
        return name + ": " + makeNoise();
        }
}

class B extends A {

    public B(String name, int age) {
        super(name, age);
        noise = "Woof!";
    }
public void playFetch() {
        System.out.println("Fetch, " + name + "!");
    }
}
class D {
    public static void main(String[] args) {
        A a = new B("Fido", 7);
        System.out.println(a.greet());
//        A b0 = new B();
//        System.out.println(b0.x);
//        b0.m1();
//        System.out.println();
//        b0.m2();
//        System.out.println();
//        //Compile time error: b0.m3();
//
//        B b1 = new B();
//        b1.m3();
//        System.out.println();
//        b1.m4();
//        System.out.println();
//
//        A c0 = new C();
//        c0.m1();
//        System.out.println();
//
//        A a1 = (A) c0;
//        System.out.println("test");
//        a1.m1();
//        System.out.println();
//        C c2 = (C) a1;
//        c2.m4();
//        System.out.println();
//        ((C) c0).m3();
//        System.out.println();
//
//        b0.update();
//        b0.m1();
//
//        a1.m1();
//        C c1 = new C();
//        c1.m4();

    }
}