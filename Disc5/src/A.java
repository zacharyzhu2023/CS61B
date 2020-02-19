class A{
    int x = 5;
    public void m1() { System.out.println("Am1 -> " + x); }
    public void m2() { System.out.println("Am2 -> " + this.x); }
    public void update() { x = 99; }
}

class B extends A {
    int x = 10;
    public void m2() { System.out.println("Bm2 -> " + x); }
    public void m3() { System.out.println("Bm3 -> " + super.x); }
    public void m4() { System.out.println("Bm4 -> "); super.m2(); }
}

class C extends B {
    int y = x + 1;
    public void m2() { System.out.println("Cm1 -> " + super.x); }
    //Compile time error: public void m3() { System.out.println("Cm1 -> " + super.super.x); }
    public void m4() { System.out.println("Cm4 -> " + y); }
    //Compile time error: public void m5() { System.out.println("Am1 -> " + super.y); }
}

class D {
    public static void main(String[] args) {
        A b0 = new B();
        System.out.println(b0.x);
        System.out.println();
        b0.m1();
        System.out.println();
        b0.m2();
        System.out.println();
        //Compile time error: b0.m3();

        B b1 = new B();
        b1.m3();
        System.out.println();
        b1.m4();
        System.out.println();

        A c0 = new C();
        c0.m1();
        System.out.println();

        A a1 = (A) c0;
        System.out.println("test");
        a1.m1();
        System.out.println();
        C c2 = (C) a1;
        c2.m4();
        System.out.println();
        ((C) c0).m3();
        System.out.println();

        b0.update();
        b0.m1();

        a1.m1();

    }
}