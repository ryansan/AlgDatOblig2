import java.util.Iterator;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();

        for (int k = 1; k <= 13; k++) {
            liste.leggInn(k);
        }

        for (Iterator<Integer> i = liste.iterator(); i.hasNext(); ) {
            int verdi = i.next();
            if (verdi % 2 == 1) {
                i.remove(); // fjerner oddetallene
            }
        }
    }
}
