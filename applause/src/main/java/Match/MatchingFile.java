package Match;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MatchingFile {

    // each tester contains one bug map
    // first condition country-- filter tester testerMap testerId: Tester(country: String)  return hashSet<testerId>
    // second condition device-- filter tester deviceTesterMap deviceId: List<testerId>  return HashSet<testerId>


    //key: testerId value: Tester
    HashMap<String, Tester> tester = new HashMap<>();
    HashMap<String, String> device = new HashMap<>();
    HashMap<String, List<String>> deviceTesterMap = new HashMap<>();
    public void readBugFile(File path) throws IOException {
        //bugId::deviceId::testerId
        List<String[]> lines = ReadFile.readFile(path);
        for(String[] lineItems: lines){
            if(tester.containsKey(lineItems[2])){
                Tester currTester = tester.get(lineItems[2]);
                currTester.addBug(lineItems[1], lineItems[0]);
            }
        }
    }

    public void readDeviceFile(File path) throws IOException {

        List<String[]> lines = ReadFile.readFile(path);

        for(String[] deviceItems: lines){

            device.put(deviceItems[0], deviceItems[1]);
        }

    }

    public void readTesterFile(File path, File deviceTesterPath) throws IOException {
        List<String[]> testerLines = ReadFile.readFile(path);
        for(String[] lineItems: testerLines){

            tester.put(lineItems[0], new Tester(lineItems[0], lineItems[1], lineItems[2], lineItems[3]));
        }
        updateDeviceTester(deviceTesterPath);

    }

    public void updateDeviceTester(File path) throws IOException {
        List<String[]> testerDeviceLines = ReadFile.readFile(path);
        for(String[] lineItems: testerDeviceLines){

            if(tester.containsKey(lineItems[0])){
                tester.get(lineItems[0]).addDevice(lineItems[1]);
                List<String> testerList = deviceTesterMap.getOrDefault(lineItems[1], new ArrayList<>());
                testerList.add(lineItems[0]);
                deviceTesterMap.put(lineItems[1], testerList);
            }
        }
    }

    public HashMap<String, String> searchCountry(HashSet<String> countrySet, HashSet<String> deviceSet) throws IOException {
        HashSet<Tester> availTester = new HashSet<>();
        if(countrySet.contains("ALL")){
            availTester.addAll(tester.values());
        }else{
            for(Tester currTester: tester.values()){
                if(countrySet.contains(currTester.getCountry())){
                    availTester.add(currTester);
                }
            }
        }
        HashMap<String, String> map = new HashMap<>();
        PriorityQueue<String[]> pq = searchDevice(deviceSet, availTester);
        Path path = Paths.get("./output.txt");
        Files.deleteIfExists(path);
        File file = new File("./output.txt");

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            while(!pq.isEmpty()){
                String[] curr = pq.poll();
                String testerName = tester.get(curr[0]).getName();
                map.put(curr[0], curr[1]);
                String line = String.format("testerId: %s,testerName: %s, bugs: %s \n",curr[0], testerName, curr[1]);
                System.out.println(line);
                bw.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bw.flush();
            if(bw!=null)
                bw.close();
        }
        return map;
    }

    public PriorityQueue<String[]> searchDevice(HashSet<String> deviceSet, HashSet<Tester> availTester) {
        PriorityQueue<String[]> pq = new PriorityQueue<>((a,b)->Integer.parseInt(b[1])-Integer.parseInt(a[1]));
        for(Tester currTest: availTester){
            int value = 0;
            for(Map.Entry<String, HashSet<String>> entry: currTest.bugs.entrySet()){
                String currDeviceId = entry.getKey();
                if(deviceSet.contains("ALL") || deviceSet.contains(device.get(currDeviceId))){
                    value += entry.getValue().size();
                }
            }

            pq.offer(new String[]{currTest.getTesterId(), String.valueOf(value)});
        }
        return pq;
    }

    public HashMap<String, String> testMatch(HashSet<String> countrySet, HashSet<String> deviceSet) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
//        File device = new File(classLoader.getResource("devices.csv").getFile());
        File device = new File("./devices.csv");
        readDeviceFile(device);
        //File tester = new File(classLoader.getResource("testers.csv").getFile());
        File tester = new File("./testers.csv");
        File testerDevice = new File("./tester_device.csv");
        //File testerDevoce = new File(classLoader.getResource("tester_device.csv").getFile());
        readTesterFile(tester, testerDevice);
        //File bugs = new File(classLoader.getResource("bugs.csv").getFile());
        File bugs = new File("./bugs.csv");
        readBugFile(bugs);

        return searchCountry(countrySet, deviceSet);

    }

    public static void main(String[] args) throws IOException {
        System.out.println("Enter Countries(separate by,no space between two countries, case sensitive) or ALL: ");
        Scanner scanner = new Scanner(System.in);
        String country = scanner.nextLine();
        String[] countries = country.split(",");
        HashSet<String> countrySet = new HashSet<>(Arrays.asList(countries));
        System.out.println("Enter Devices(separate by, no space between two devices, case sensitive) or ALL: ");
        String device = scanner.nextLine();
        String[] devices = device.split(",");
        HashSet<String> deviceSet = new HashSet<>(Arrays.asList(devices));
        MatchingFile matchFile = new MatchingFile();
        matchFile.testMatch(countrySet, deviceSet);

    }
}
class ReadFile {
    public static List<String[]> readFile(File file){
        System.out.println(file.getPath());
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(file));
            return reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
