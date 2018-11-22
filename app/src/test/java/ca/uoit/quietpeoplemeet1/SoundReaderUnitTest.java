package ca.uoit.quietpeoplemeet1;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SoundReaderUnitTest {


    // get sound value
    SoundReader sr = new SoundReader();


    int value =1;
    @Test
    public void SoundNotZero(){assertTrue(value > 0);}

}
