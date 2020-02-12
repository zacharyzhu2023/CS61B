package image;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author Zachary Zhu
 */

public class MatrixUtilsTest {
    /** FIXME
     */
    @Test
    public void accumulateVerticalTest() {

        double[][] accumBefore0 = new double[][] {
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515, 38273, 1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}};

        double[][] accumAfter0 = new double[][] {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}};

        double[][] accumBefore1 = new double[][] {
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515, 38273, 1000000}};

        double[][] accumAfter1 = new double[][] {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049}};

        double[][] accumBefore2 = new double[][] {
                {1000000, 1000000, 1000000, 1000000}};

        double[][] accumAfter2 = new double[][] {
                {1000000, 1000000, 1000000, 1000000}};

        assertArrayEquals(accumAfter0,
                MatrixUtils.accumulateVertical(accumBefore0));
        assertArrayEquals(accumAfter1,
                MatrixUtils.accumulateVertical(accumBefore1));
        assertArrayEquals(accumAfter2,
                MatrixUtils.accumulateVertical(accumBefore2));

    }


    @Test
    public void accumulateTest() {
        double[][] accumBeforeVertical0 =
                new double[][] {
                        {1000000, 1000000, 1000000, 1000000},
                        {1000000, 75990, 30003, 1000000},
                        {1000000, 30002, 103046, 1000000},
                        {1000000, 29515, 38273, 1000000},
                        {1000000, 73403, 35399, 1000000},
                        {1000000, 1000000, 1000000, 1000000}};

        double[][] accumAfterVertical0 =
                new double[][] {
                        {1000000, 1000000, 1000000, 1000000},
                        {2000000, 1075990, 1030003, 2000000},
                        {2075990, 1060005, 1133049, 2030003},
                        {2060005, 1089520, 1098278, 2133049},
                        {2089520, 1162923, 1124919, 2098278},
                        {2162923, 2124919, 2124919, 2124919}};

        double[][] accumBeforeHorizontal =
                new double[][] {
                {1000000, 1000000, 1000000, 1000000}};
        double[][] accumAfterHorizontal =
                new double[][] {
                {1000000, 2000000, 3000000, 4000000}};
        assertArrayEquals(accumAfterVertical0,
                MatrixUtils.accumulate(accumBeforeVertical0,
                        MatrixUtils.Orientation.VERTICAL));
        assertArrayEquals(accumAfterHorizontal,
                MatrixUtils.accumulate(accumBeforeHorizontal,
                        MatrixUtils.Orientation.HORIZONTAL));

    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
