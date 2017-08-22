
import java.io.*;
import java.util.*;

public class SongVoting {
    
    private static final String inputFileName = "src/LogFile.txt";
    
    private static final int    indentWidth = 5;          // table indent
    private static final int    colWidth = 30;            // width of table column
    
    
    
    
    public static void main (String[] args) throws Exception {

       Scanner sc = new Scanner(new File(inputFileName));
      
       String command, songName;                // strings read from file
           
           while (sc.hasNext()) {
              command = sc.next();                 // read next line of file
              songName = sc.next();
              
              // ... continue here
           }
   
   
   
   // Print results for array list, first save the operation count


   // Print out top three by getting top name, then votes, then deleting
   
   // Do same for the linked list

 }
 
    // Utility methods for extracting band and song name from string
    // of the form "band:songname".
     
     private static String getBandName(String s) {
         // your code here
     }
     
     private static String getSongName(String s) {
         //your code here
     }
     
     // Skip n space on console output
     
     private static void skipSpaces(int n) {
         for (int i = 0; i < n; i++)
             System.out.print(" ");
     }
     
     // more methods if needed
     
     
 
}
