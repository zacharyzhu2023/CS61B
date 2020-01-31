import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        assertEquals(10, CompoundInterest.numYears(2029));
        assertEquals(0, CompoundInterest.numYears(2019));
        assertEquals(11, CompoundInterest.numYears(2030));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(1060, CompoundInterest.futureValue(1000, 6, 2020), tolerance);
        assertEquals(250, CompoundInterest.futureValue(250, 0, 2029), tolerance);
        assertEquals(15, CompoundInterest.futureValue(15, 6,2019), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(500, CompoundInterest.futureValueReal(500, 6, 2020, 6), tolerance);
        assertEquals(50, CompoundInterest.futureValueReal(50, 10, 2019, 5), tolerance);
        assertEquals(901.4497, CompoundInterest.futureValueReal(250, 8, 2029, -5), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(100, CompoundInterest.totalSavings(100, 2019, 4), tolerance);
        assertEquals(410, CompoundInterest.totalSavings(200, 2020, 5), tolerance);
        assertEquals(955.08, CompoundInterest.totalSavings(300, 2021, 6), tolerance);

    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(500, CompoundInterest.totalSavingsReal(500, 2019, 6, 5), tolerance);
        assertEquals(1909.09091, CompoundInterest.totalSavingsReal(1000, 2020, 10, 10), tolerance);
        assertEquals(5328.60332, CompoundInterest.totalSavingsReal(2000, 2021, 11, 12), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
