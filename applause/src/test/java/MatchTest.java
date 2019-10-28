import Match.MatchingFile;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class MatchTest {
    @Test
    public void test1() throws IOException {
        //test country->ALL
        MatchingFile testMatch = new MatchingFile();
        String[] countries = new String[]{"ALL"};
        HashSet countrySet = new HashSet(Arrays.asList(countries));
        String[] devices = new String[]{"iPhone 4S"};
        HashSet deviceSet = new HashSet(Arrays.asList(devices));
        HashMap<String, Integer> map = testMatch.testMatch(countrySet, deviceSet);
        assertEquals("59", map.get("4"));
        assertEquals("26", map.get("1"));
        assertEquals("0", map.get("2"));
        assertEquals("0", map.get("3"));
        assertEquals("0", map.get("5"));
        assertEquals("0", map.get("6"));
        assertEquals("0", map.get("7"));
        assertEquals("0", map.get("8"));
        assertEquals("0", map.get("9"));
    }

    @Test
    public void test2() throws IOException {
        MatchingFile testMatch = new MatchingFile();
        String[] countries = new String[]{"ALL"};
        HashSet countrySet = new HashSet(Arrays.asList(countries));
        String[] devices = new String[]{"ALL"};
        HashSet deviceSet = new HashSet(Arrays.asList(devices));
        HashMap<String, Integer> map = testMatch.testMatch(countrySet, deviceSet);
        assertEquals("125", map.get("4"));
        assertEquals("114", map.get("1"));
        assertEquals("99", map.get("2"));
        assertEquals("106", map.get("3"));
        assertEquals("109", map.get("5"));
        assertEquals("110", map.get("6"));
        assertEquals("117", map.get("7"));
        assertEquals("116", map.get("8"));
        assertEquals("104", map.get("9"));
    }

    @Test
    public void test3() throws IOException {
        MatchingFile testMatch = new MatchingFile();
        String[] countries = new String[]{"US","JP"};
        HashSet countrySet = new HashSet(Arrays.asList(countries));
        String[] devices = new String[]{"iPhone 3"};
        HashSet deviceSet = new HashSet(Arrays.asList(devices));
        HashMap<String, Integer> map = testMatch.testMatch(countrySet, deviceSet);
        assertEquals("0", map.get("4"));
        assertEquals("35", map.get("1"));
        assertEquals("0", map.get("2"));
        assertEquals("19", map.get("5"));
        assertEquals("0", map.get("7"));
        assertEquals("18", map.get("8"));

    }
}
