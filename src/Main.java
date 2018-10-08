public class Main {

    public static void main(String[] args) {

        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        for (int i = 0; i <= 3; i++) {
            liste.leggInn(i);
        }

        System.out.println(liste.toString());

        liste.oppdater(3,100);

        System.out.println(liste.hent(3));

        System.out.println(liste.toString());


    }
}
