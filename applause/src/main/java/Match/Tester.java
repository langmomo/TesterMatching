package Match;

import java.util.HashMap;
import java.util.HashSet;

public class Tester{
    HashSet<String> devices = new HashSet<>();
    //deviceId:: bugsSet
    HashMap<String, HashSet<String>> bugs = new HashMap<>();
    private String country;
    private String testerId;
    private String name;

    Tester(String testerId, String firstName, String lastName, String country){
        this.country = country;
        this.testerId = testerId;
        this.name = firstName+" "+lastName;
    }

    public void addDevice(String deviceId){
        this.devices.add(deviceId);
    }

    public void addBug(String deviceId,  String bugId){
        HashSet<String> bugSet = bugs.getOrDefault(deviceId, new HashSet());
        bugSet.add(bugId);
        bugs.put(deviceId, bugSet);
    }

    public String getCountry(){
        return this.country;
    }

    public String getTesterId(){
        return this.testerId;
    }

    public String getName(){
        return this.name;
    }
}
