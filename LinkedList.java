
public class LinkedList {

    private Node head;
    private boolean songFound;              
    private int operationCount;

    class Node {
        public String songName;
        public int votes = 0;
        Node next;

        Node(String s) { songName = s; }
        Node(String s, Node n) { songName = s; next = n; }
    }

    public LinkedList() {
        head = new Node("head node");        
        operationCount = 0;
    }

 

    public void add(String song) {

  

    }

   

    public void delete(String song) {

   

    }



    public void vote(String song) {
    

    }


    public String topSong() {
        int    topVotes    = -1;
        String topSongName = "no top song in empty list";


        return topSongName;
    }



    private Node search(String song) {
        Node p;

 

        return p;
    }

}



