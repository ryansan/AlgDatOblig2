import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

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
    private Node<T> finnNode(int indeks)
    {

        Node<T> node;

        if(indeks < antall/2){
            //søk fra hode til halve v b a neste

            node = hode;

            for(int i = 0; i <= antall/2; i++){
                if(i == indeks){
                    break;
                }
                node = node.neste;
            }
        }else{

            node = hale;

            for(int i = antall; i >= antall/2; i--){
                if(i == indeks){
                    break;
                }
                node = node.forrige;
            }
        }

        return node;
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
        throw new UnsupportedOperationException("Ikke laget ennå!");
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
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override
    public boolean inneholder(T verdi)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override
    public T hent(int indeks)
    {
        indeksKontroll(indeks,false);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
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
    public boolean fjern(T verdi)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override
    public T fjern(int indeks)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override
    public void nullstill()
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
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
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    public Iterator<T> iterator(int indeks)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
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
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }

        @Override
        public boolean hasNext()
        {
            return denne != null;  // denne koden skal ikke endres!
        }

        @Override
        public T next()
        {
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }

    } // DobbeltLenketListeIterator

} // DobbeltLenketListe  