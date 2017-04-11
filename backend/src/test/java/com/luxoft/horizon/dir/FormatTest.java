package com.luxoft.horizon.dir;

import org.junit.Assert;
import org.junit.Test;

import java.text.NumberFormat;

/**
 * Created by bogatp on 01.04.16.
 */
public class FormatTest {

    @Test
    public void testFormat(){
        String strValue = "1234567890";
        Double value = Double.valueOf(strValue);
        NumberFormat f = NumberFormat.getInstance();
        f.setGroupingUsed(false);
        String strVal = f.format(value);
        Assert.assertEquals(strValue, strVal);
    }


}
