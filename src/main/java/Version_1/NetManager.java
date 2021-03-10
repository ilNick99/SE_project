package Version_1;

import Utility.JsonReader;
import Utility.JsonWriter;
import Utility.Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetManager {
    public static final String ANOTHER_NET = "You want add another Net?";
    public static final String NAME_OF_NET = "Add the name of Net:";
    public static final String MENU = "What do you want do?\n0)EXIT\n1)Add new Net\n"+/*"2)Check the net\n3)Save Net\n"+*/"2)Load net";
    public static final String WANT_TO_DO_ANOTHER_OPERATION = "you want to do another operation ";
    public static final String DIGIT_YOUR_CHOISE = "Digit your choise ";
    public static final String DIGIT_VALID_CHOISE = "Digit valid choise!";

    private ArrayList<Net> netList = new ArrayList<Net>();

    public void menageOption() throws IOException {
        boolean check=true;
        int choise=0;
        do {
            System.out.println(MENU);
            choise = Reader.readNumber(DIGIT_YOUR_CHOISE);
            while(choise<0 || choise>4){
                System.out.println(DIGIT_VALID_CHOISE);
                choise=Reader.readNumber(DIGIT_YOUR_CHOISE);
            }

            switch (choise){
                case 0:
                    check=false;
                    break;
                case 1:

                    addNet();
                    check=Reader.yesOrNo(WANT_TO_DO_ANOTHER_OPERATION);
                    break;

//                case 2:
//                    checkNet();
//                    check=Reader.yesOrNo(WANT_TO_DO_ANOTHER_OPERATION);
//                    break;
//
//                case 3:
//                    //TODO
//                    check=Reader.yesOrNo(WANT_TO_DO_ANOTHER_OPERATION);
//                    break;

                case 2:
                    loadNet();
                    check=Reader.yesOrNo(WANT_TO_DO_ANOTHER_OPERATION);
                    break;
            }
        }while(check==true);

    }


    public void addNet() throws IOException {
        do {
            Net n= new Net(Reader.ReadString(NAME_OF_NET));
            if(checkNet(n)) {
                netList.add(n);
                JsonWriter.writeJsonFile(n);
            }
        } while (Reader.yesOrNo(ANOTHER_NET));
    }
    //DA DEBUGGARE
    public boolean checkNet(Net n){

        if(n.checkPendantNode()==false){

            System.out.println("The net is incorrect, it can't be saved");
            return false;
        }else{
            showNet(n);
            System.out.println("The net is correct, we are going to save it");
            return true;
        }
    }

    public void loadNet() throws FileNotFoundException {
        File directory = new File("src/main/java/Json");
        String[] pathname = directory.list();
        int i = 0;
        for (String s: pathname) {
            i++;
            System.out.println(i+") "+s);
        }
        if (i==0) {
            System.out.println("There aren't any files to load");
        }
        else {
            int number = Reader.leggiIntero("Insert the id of the file you want to load ", 1, i);
            String path = "src/main/java/Json/"+pathname[number-1];
            Net newNet = JsonReader.readJson(path);
            netList.add(newNet);
            System.out.println("File is loaded");
            System.out.println("Visualizzazione della lista");
            showNet(newNet);
        }
    }

    public void showNet(Net net) {
        String nameNet = net.getName();
        String idNet = net.getID();
        ArrayList<String> places = new ArrayList<String>();
        ArrayList<String> transitions = new ArrayList<String>();

        for (Pair p: net.getNet()) {
            String place = p.getPlace().getId();
            String trans = p.getTrans().getId();
            places.add(place);
            transitions.add(trans);
        }

        HashMap<Integer, Integer> index = new HashMap<Integer, Integer>();
        for (int i = 0; i < transitions.size(); i++) {
            for (int j = 0; j < transitions.size(); j++) {
                if (i != j) {
                    if (transitions.get(i).equals(transitions.get(j))) {
                        index.put(i, j);
                    }
                }
            }
        }
        HashMap<Integer, Integer> indexUpdate = checkDuplicate(index);
        ArrayList<String> couples = new ArrayList<String>();
        for (Map.Entry<Integer, Integer> entry: indexUpdate.entrySet()) {
            String couple = places.get(entry.getKey())+"-----"+transitions.get(entry.getKey())+"-----"+places.get(entry.getValue());
            couples.add(couple);
        }

        System.out.println("\nName net: "+nameNet+"\tID net: "+idNet);
        System.out.println("List pairs:");
        for (String s: couples) {
            System.out.println("\t"+s);
        }
        System.out.println();
    }

    private HashMap<Integer, Integer> checkDuplicate(HashMap<Integer, Integer> map) {
        HashMap<Integer, Integer> newMap = new HashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> copy: map.entrySet()) {
            newMap.put(copy.getKey(),copy.getValue());
        }
        ArrayList<Integer> done = new ArrayList<Integer>();
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            boolean ctrl = true;
            for (Integer d: done) {
                if (entry.getKey().equals(d)) {
                    ctrl = false;
                    break;
                }
            }
            if (ctrl) {
                for (Map.Entry<Integer, Integer> entry2: map.entrySet()) {
                    if (entry.getKey().equals(entry2.getValue())) {
                        if (entry.getValue().equals(entry2.getKey())) {
                            newMap.remove(entry2.getKey());
                            done.add(entry2.getKey());
                        }
                    }
                }
            }
        }
        return newMap;
    }
}
