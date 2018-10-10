import java.util.*;

public class DobbeltLenketListe<T> implements Liste<T>
{
    private static final class Node<T>   // en indre nodeklasse
    {
        // instansvariabler
        private T verdi;
        private Node<T> forrige, neste;

        private Node(T verdi, Node<T> forrige, Node<T> neste)  // konstruktør
        {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        protected Node(T verdi)  // konstruktør
        {
            this(verdi, null, null);
        }

    } // Node

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;   // antall endringer i listen

    // hjelpemetode
    public Node<T> finnNode(int indeks) {

        Node<T> node;

        if(indeks < antall/2){

            node = hode;
            int teller = 0;

            while(teller < indeks){
                node = node.neste;
                teller++;
            }
            return node;
        }else{

            node = hale;

            int teller = antall-1;
            while(teller > indeks){
                node = node.forrige;
                teller--;
            }
            return node;
        }

    }

    // konstruktør
    public DobbeltLenketListe()
    {
        hode = hale = null;
        antall = 0;
        endringer = 0;
    }

    // konstruktør
    public DobbeltLenketListe(T[] a)
    {

        this();

        if(a.length == 0){
            hode = hale = null;
            return;
        }

        Objects.requireNonNull(a,"Tabellen a er null!!");

        int i = 0;

        for (; i < a.length && a[i] == null; i++); // setter i til indeksen til den første som ikke er null

        if(i < a.length){


        Node<T> p = hode = new Node<>(a[i], null,null);  // den første noden
        antall++;

        hale = hode;


        for(i++; i < a.length; i++){
            if(a[i] != null){
                p.neste = new Node<>(a[i], p,null);
                p = p.neste;
                antall++;
            }

        }

        hale = p;
        }

    }

    // subliste
    public Liste<T> subliste(int fra, int til)
    {
        fratilKontroll(antall,fra,til);

        DobbeltLenketListe sub = new DobbeltLenketListe();

        int teller = fra;

        while(teller < til){
            sub.leggInn(finnNode(teller).verdi);
            teller++;
        }

        return sub;
    }

    @Override
    public int antall()
    {
        return antall;
    }

    @Override
    public boolean tom()
    {
        return antall == 0;
    }

    @Override
    public boolean leggInn(T verdi)
    {


        Objects.requireNonNull(verdi,"Kan ikke være null");

        if(tom()){
            //sett hode og hale til verdi
            hode = hale = new Node<>(verdi, null,null);
            antall++;
            endringer++;
        }else{

            Node<T> p = hode;
            Node<T> q = hode;

            if(q.neste != null){
                while(q != null){
                    p = q;
                    q = q.neste; // p til hale
                }
            }


            Node<T> ny = new Node<>(verdi, p, null);

            p.neste = ny;

            ny.forrige = p;

            hale = ny;

            antall++;
            endringer++;

        }

        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi)
    {

        Objects.requireNonNull(verdi,"NULL");

        if(indeks < 0){
            throw new IndexOutOfBoundsException("Lavere enn 0 og større enn antall");
        }

        if(indeks > antall){
           throw new IndexOutOfBoundsException("Lavere enn 0 og større enn antall");

        }

        if(tom()){
            Node<T> ny = new Node<>(verdi,null,null);
            hode = hale = ny;
            antall++;
            endringer++;
            return;
        }

        if(indeks == 0){ // først
            //            Node<T> ny = new Node<>(verdi,null,hode.neste); dette hadde jeg, kanskje bruk dette

            Node<T> ny = new Node<>(verdi,null,null);
            ny.neste = hode;
            hode.forrige = ny;
            hode = ny;

            if(antall == 0){
                hode = hale;
            }

        }else if(indeks == antall){// bakerst
            Node<T> ny = new Node<>(verdi,hale,null);
            hale.neste = ny;
            hale = ny;
        }else{ //midt i
            Node<T> current = hode;

            for(int i = 1; i < indeks; i++){
                current = current.neste;
            }

            //fått tak i p må oppdatere

            Node<T> next = current.neste;

            Node<T> ny = new Node(verdi, current, next);

            current.neste = ny;

            next.forrige = ny;


        }

        antall++;
        endringer++;

        System.out.println(toString());

    }

    @Override
    public boolean inneholder(T verdi)
    {
        if(indeksTil(verdi) != -1){
            return true;
        }
        return false;
    }

    @Override
    public T hent(int indeks)
    {
        indeksKontroll(indeks,false);
        return Objects.requireNonNull(finnNode(indeks)).verdi;
    }

    @Override
    public int indeksTil(T verdi)
    {
        if(verdi == null){
            return -1;
        }

        Node<T> p = hode;

        for(int i = 0; i < antall; i++){
            if(p.verdi.equals(verdi)){
                return i;
            }
            p = p.neste;
        }

        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi)
    {

        indeksKontroll(indeks, false);
        Objects.requireNonNull(nyverdi, "NULL verdi");

        Node<T> p = hode;

        T temp = null;

        for(int i = 0; i < antall; i++){
            if(i == indeks){
                temp = p.verdi;
                p.verdi = nyverdi;
                endringer++;
                break;
            }
            p = p.neste;
        }

        return temp;
    }

    @Override
    public boolean fjern(T verdi) {


        if(verdi == null){
            return false;
        }

        int teller = 0;

        Node<T> p = hode;

        while(p != null){

            if(p.verdi.equals(verdi)){

                if(teller == 0){
                    hode = hode.neste;
                    antall--;
                    endringer++;
                    return true;
                }else if(teller == antall-1){
                    hale = hale.forrige;
                    hale.neste = null;
                    antall--;
                    endringer++;
                    return true;
                }else{
                    Node<T> prev = p.forrige;

                    prev.neste = p.neste;
                    prev.neste.forrige = prev;

                    antall--;
                    endringer++;
                    return true;
                }
            }

            p = p.neste;
            teller++;
        }

        return false;
    }

    @Override
    public T fjern(int indeks)
    {
        if(indeks < 0 || indeks >= antall){
            throw new IndexOutOfBoundsException(" må være: 0 < indeks < antall ");
        }

        if(tom()){
            throw new IndexOutOfBoundsException("Tom!");
        }

        if(indeks == 0){
            //første
            T temp = hode.verdi;
            hode = hode.neste;
            antall--;
            endringer++;
            return temp;
        }else if(indeks == antall-1){
            T temp = hale.verdi;
            hale = hale.forrige;
            hale.neste = null;
            antall--;
            endringer++;
            return temp;
        }else{
            Node<T> p = hode;

            int teller = 0;
            while(p != null){
                if(teller == indeks){
                    Node<T> prev = p.forrige;
                    T temp = p.verdi;

                    prev.neste = p.neste;
                    prev.neste.forrige = prev;

                    antall--;
                    endringer++;
                    return temp;
                }
                p = p.neste;
                teller++;
            }
        }
        return null;
    }

    @Override
    public void nullstill() {
        Node<T> p = hode;

        for (Node<T> x = hode; x != null; ) {
            Node<T> next = x.neste;
            x.neste= null;
            x.forrige= null;
            x = next;
        }
        hode = hale = null;
        antall = 0;
        endringer++;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();


        Node<T> p = hode;

        sb.append("[");

        if (!tom()) {
            sb.append(p.verdi);

            p = p.neste;

            while (p != null) {
                sb.append(',').append(' ').append(p.verdi);
                p = p.neste;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    public String omvendtString()
    {
        StringBuilder sb = new StringBuilder();


        Node<T> p = hale;


        sb.append("[");

        if (!tom()) {
            sb.append(p.verdi);

            p = p.forrige;

            for (int i = antall - 1; i > 0; i--) {
                sb.append(',').append(' ').append(p.verdi);
                p = p.forrige;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override
    public Iterator<T> iterator()
    {
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks,false);



        return new DobbeltLenketListeIterator(indeks);
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator()
        {
            denne = hode;     // denne starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks)
        {
            denne = finnNode(indeks);     // denne starter på noden med indeks = indeks
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        @Override
        public boolean hasNext()
        {
            return denne != null;  // denne koden skal ikke endres!
        }

        @Override
        public T next()
        {
            if(iteratorendringer != endringer) {
                throw new ConcurrentModificationException("Ikke lik");
            }


            if(!hasNext()){
                throw new NoSuchElementException();
            }


            fjernOK = true;

            T temp = denne.verdi;
            denne = denne.neste;

            return temp;
        }

        @Override
        public void remove()
        {
            if (endringer != iteratorendringer) {
                throw new ConcurrentModificationException("Listen er endret!");
            }


            if (!fjernOK) {throw new IllegalStateException("Ulovlig tilstand!");}

            fjernOK = false;

            Node<T> p = denne;

            if(antall == 1){
                hode = hale = null;
            }
            //siste
            else if(denne == null){
                hale = hale.forrige;
                hale.neste = null;
            }
            // hvis første skal fjerne
            else if(hode.neste == denne){
                hode = hode.neste;
                hode.forrige = null;
                if(denne == null){
                    hale = null;
                }
            }
            else{


                Node<T> r = hode;            // må finne forgjengeren
                // til forgjengeren til p
                while (r.neste.neste != denne)
                {
                    r = r.neste;               // flytter r
                }

                p = r.neste;                 // det er q som skal fjernes
                r.neste = denne;                 // "hopper" over q
                if (denne == null) hale = r;     // q var den siste

            }

            p.verdi = null;                // nuller verdien i noden
            p.neste = null;
            p.forrige = null;           // nuller nestepeker

            endringer++;             // en endring i listen
            iteratorendringer++;    // en endring av denne iteratoren
            antall--;                      // en node mindre i listen


        }

    } // DobbeltLenketListeIterator


    //hjelpemetoder
    public static void fratilKontroll(int antall, int fra, int til)
    {
        if (fra < 0)                                  // fra er negativ
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");

        if (til > antall)                          // til er utenfor tabellen
            throw new IndexOutOfBoundsException
                    ("til(" + til + ") > antall(" + antall + ")");

        if (fra > til)                                // fra er større enn til
            throw new IllegalArgumentException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }

} // DobbeltLenketListe  