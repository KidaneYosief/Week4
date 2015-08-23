package app.calculatorapp;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;
//White Box
/**
 * Created by kalpesh on 07/06/2015.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testDisplayWhiteBox() {

        //Defining our own values to multiply
        float vFirstNumber = 10;
        float vSecondNumber = 20;
        float vResutl = vFirstNumber * vSecondNumber ;

        //Access First value (edit-filed) and putting firstNumber value in it
        EditText vFirstEditText = (EditText) solo.getView(R.id.EditText01);
        solo.clearEditText(vFirstEditText);
        solo.enterText(vFirstEditText, String.valueOf(vFirstNumber));

        //Access Second value (edit-filed) and putting SecondNumber value in it
        EditText vSecondEditText = (EditText) solo.getView(R.id.EditText02);
        solo.clearEditText(vSecondEditText);
        solo.enterText(vSecondEditText, String.valueOf(vSecondNumber));

        //Click on Multiply button
        solo.clickOnButton("Multiply");

        assertTrue(solo.searchText(String.valueOf(vResutl)));
        TextView outputField = (TextView) solo.getView(R.id.TextView01);
        //Assert to verify result with visible value
        assertEquals(String.valueOf(vResutl), outputField.getText().toString());
    }

    @Override
    protected void tearDown() throws Exception{

        solo.finishOpenedActivities();
    }
}