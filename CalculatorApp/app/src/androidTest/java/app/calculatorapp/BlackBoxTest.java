package app.calculatorapp;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by kalpesh on 07/06/2015.
 */
public class BlackBoxTest extends ActivityInstrumentationTestCase2 {

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "app.calculatorapp.MainActivity";
    private static Class launcherActivityClass;
    static {

        try {
            launcherActivityClass = Class
                    .forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public BlackBoxTest() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

    private Solo solo;

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }
//PASSED
    public void testDisplayBlackBox() {
        // Enter any integer/decimal value for first editfield, we are writing  10
        solo.clearEditText(0);
        solo.enterText(0, "1");

        // Enter any integer/decimal value for first editfield, we are writing  20
        solo.clearEditText(1);
        solo.enterText(1, "20");

        // Click on Multiply button
        solo.clickOnButton("Multiply");

        // Verify that resultant of 10 x 20
        assertTrue(solo.searchText("20"));
    }
//FAILED
    public void testDisplayBlackBox1() {
        // Enter any integer/decimal value for first editfield, we are writing  10
        solo.clearEditText(0);
        solo.enterText(0, "9");

        // Enter any integer/decimal value for first editfield, we are writing  20
        solo.clearEditText(1);
        solo.enterText(1, "20");

        // Click on Multiply button
        solo.clickOnButton("Multiply");

        // Verify that resultant of 10 x 20
        assertTrue(solo.searchText("880"));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}