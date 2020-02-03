import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        assertEquals(10, CompoundInterest.numYears(2030));
        assertEquals(0, CompoundInterest.numYears(2020));
        assertEquals(11, CompoundInterest.numYears(2031));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(1060, CompoundInterest.futureValue(1000, 6, 2021), tolerance);
        assertEquals(250, CompoundInterest.futureValue(250, 0, 2030), tolerance);
        assertEquals(15, CompoundInterest.futureValue(15, 6,2020), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(498.2, CompoundInterest.futureValueReal(500, 6, 2021, 6), tolerance);
        assertEquals(50, CompoundInterest.futureValueReal(50, 10, 2020, 5), tolerance);
        assertEquals(879.165, CompoundInterest.futureValueReal(250, 8, 2030, -5), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(100, CompoundInterest.totalSavings(100, 2020, 4), tolerance);
        assertEquals(410, CompoundInterest.totalSavings(200, 2021, 5), tolerance);
        assertEquals(955.08, CompoundInterest.totalSavings(300, 2022, 6), tolerance);

    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(500, CompoundInterest.totalSavingsReal(500, 2020, 6, 5), tolerance);
        assertEquals(1890, CompoundInterest.totalSavingsReal(1000, 2021, 10, 10), tolerance);
        assertEquals(5176.25, CompoundInterest.totalSavingsReal(2000, 2022, 11, 12), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
