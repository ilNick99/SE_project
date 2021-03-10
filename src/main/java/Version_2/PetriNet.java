package Version_2;

import Utility.Reader;

import java.util.HashSet;

public class PetriNet extends Net {
    public static final String HOW_MANY_TOKEN = "How many tokens do you want this place to have?\n(if you don't want tokens enter 0)";
    private static int i;
    //private boolean def; //indicates whether the petri net has the default values or not


    public PetriNet(Net genericNet, String name) {
        super(genericNet);
        super.setID(super.getName() + (++i));
        super.setName(name);
        addWeight();
        addPrePost();
        addToken();
    }


    public void addWeight() {
        HashSet<Transition> tempTrans = new HashSet<>(); //creo un set temporaneo di transizioni che usero per stampare i nomi
        String transId;
        String temp;
        int weight;
        boolean check = false;
        boolean again = true;
        int i;

        for (i = 0; i < super.getNet().size(); i++) {
            tempTrans.add(super.getNet().get(i).getTrans());
        }

        i = 0;
        do {
            do {
                //Stampo tutte le alternative
                System.out.println("Transition:");
                for (Transition trans : tempTrans) {
                    System.out.println(i + ") " + trans.getId());
                    i++;
                }
                System.out.println("If you want exit insert: EXIT");
                transId = Reader.readNotEmpityString("Enter the ID of the transition you want to edit: ");
                if (transId.compareTo("EXIT") == 0) {
                    again = false;
                    break;
                }
                //inizio ricerca per vedere se esiste
                for (Transition trans : tempTrans) {
                    if (trans.getId().compareTo(transId) == 0) {
                        check = true;
                        break;
                    }
                }
                //Se check è falso non l'ha trovato, stampo l'error e ricomincio
                if (!check) {
                    System.out.println("ERROR, WRONG ID");
                } else { //se check è true la ricerca è andata a buon fine, posso chiedere all'utente il peso della transizione e modificarla nella rete
                    weight = Reader.leggiInteroConMinimo("Insert the weight of the transition", 1);
                    for (i = 0; i < super.getNet().size(); i++) {
                        temp = super.getNet().get(i).getTrans().getId(); //In temp metto l'id della transizione corrente
                        if (temp.compareTo(transId) == 0) {
                            super.getNet().get(i).getTrans().setWeight(weight);
                        }
                    }
                }
            } while (!check);
            again = Reader.yesOrNo("You want continue?");
        } while (again);

    }

    public void addToken() {
        HashSet<Place> tempPlace = new HashSet<>(); //creo un set temporaneo di transizioni che usero per stampare i nomi
        String placeId;
        String temp;
        int token;
        boolean check = false;
        boolean again = true;
        int i;

        for (i = 0; i < super.getNet().size(); i++) {
            tempPlace.add(super.getNet().get(i).getPlace());
        }

        i = 0;
        do {
            do {
                //Stampo tutte le alternative
                System.out.println("Place:");
                for (Place place: tempPlace) {
                    System.out.println(i + ") " + place.getId());
                    i++;
                }
                System.out.println("If you want exit insert: EXIT");
                placeId = Reader.readNotEmpityString("Enter the ID of the Place you want to edit: ");
                if (placeId.compareTo("EXIT") == 0) {
                    again = false;
                    break;
                }
                //inizio ricerca per vedere se esiste
                for (Place place : tempPlace) {
                    if (place.getId().compareTo(placeId) == 0) {
                        check = true;
                        break;
                    }
                }
                //Se check è falso non l'ha trovato, stampo l'error e ricomincio
                if (check == false) {
                    System.out.println("ERROR, WRONG ID");
                } else { //se check è true la ricerca è andata a buon fine, posso chiedere all'utente il peso della transizione e modificarla nella rete
                    token = Reader.leggiInteroConMinimo("Insert the number of tokens: ", 0);
                    for (i = 0; i < super.getNet().size(); i++) {
                        temp = super.getNet().get(i).getPlace().getId(); //In temp metto l'id della transizione corrente
                        if (temp.compareTo(placeId) == 0) {
                            super.getNet().get(i).getPlace().setToken(token);
                        }
                    }
                }
            } while (check == false);
            again = Reader.yesOrNo("You want continue?");
        } while (again == true);

    }

    public void addPrePost() {
        HashSet<String> transChecked = new HashSet<>();
        HashSet<String> placeToAdd = new HashSet<>();
        int i = 0;
        int j = 0;
        int answer;
        boolean check = false;
        int netSize = super.getNet().size();
        String attualTrans;
        String wantAdd;

        for (i = 0; i < netSize; i++) {
            attualTrans = super.getNet().get(i).getTrans().getId();

            if (transChecked.add(attualTrans)) { //se l'aggiunta di questa transizione va a buon fine vuol dire che non ho mai esaminato questa transizione e procedo

                //riempio un set con tutti i posti legati alla transizione
                for (j = 0; i < super.getNet().size(); j++) {
                    if (attualTrans.compareTo(super.getNet().get(j).getTrans().getId()) == 0) {
                        placeToAdd.add(super.getNet().get(j).getPlace().getId());
                    }
                }

                do {
                    //seleziono dal set il posto che voglio aggiungere ai Pre/Post
                    wantAdd = selectPlace(placeToAdd);

                    //chiedo all'utente se sono post o pre e li aggiungo alla transizione della rete
                    answer = Reader.leggiIntero("This place is:\n0) Predecessor\n1) Successor", 0, 1);
                    if (answer == 0) {
                        super.getNet().get(i).getTrans().getIdPre().add(wantAdd);
                    } else {
                        super.getNet().get(i).getTrans().getIdPost().add(wantAdd);
                    }

                    //rimuovo il nodo appena aggiunto nel set; così sono sicuro che l'utente non possa più reinserire quel nodo come pre/post
                    placeToAdd.remove(wantAdd);
                    if (placeToAdd.size() > 0) {
                        check = true;
                    } else {
                        check = false;
                    }
                } while (check); //ripeto finchè il set è vuoto
            }
        }
    }


    public String selectPlace(HashSet<String> set) {
        String select = null;
        //printo all'utente tutti i nodi
        System.out.println("the places connected to the transition are:");
        for (String stringa : set) {
            System.out.println("--> " + stringa);
        }
        //gli faccio scegliere un nodo e se quel nodo esiste lo restituisco
        do {
            select = Reader.readNotEmpityString("What place you want to link to the connection?");
            if (set.contains(select)) {
                return select;
            }
        } while (!set.contains(select));
        return null;
    }


}

