import java.util.HashMap;
import java.util.HashSet;
// import java.util.LinkedList;
// import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;

public class MarkovChain
{
    //Used by note, dur
    private HashMap<String, Integer> numToKey = new HashMap<>();

    //Used by note, dur
    private HashMap<String, Integer> occurences = new HashMap<>();

    //Used by note, dur for "for" loops
    private int keys = 12;

    //defined in dur
    private double[][] probibsDur;

    //defined in note
    private double[][] probibs = new double[keys][keys];
    
    //used in note
    private String[] keyLetters = new String[]{"c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "b"};
    
    //used in generating next dur... used to get the actual duration
    private Long[] _durArray;

    public MarkovChain(ArrayList<Note> baseSong)
    {
        noteChain(baseSong);
        lengthChain(baseSong);
    }

    public void lengthChain(ArrayList<Note> song)
    {
        //create an array of the possible Durations
        Set<Long> durs = new HashSet<Long>();
        for(int i = 0; i < song.size(); i += 1)
        {
            durs.add(song.get(i).getDuration());
            //System.out.print(song.get(i).getName());
            //System.out.println(song.get(i).getDuration());
        }
        Long[] durArray = durs.toArray(new Long[durs.size()]);
        Arrays.sort(durArray);
        this._durArray = durArray;
        //done creating that array of possible Durations

        //numToKey is already Init in NoteChain
        //Occurences is also already Init in NoteChain
        //occurencs is the same for both
        //numToKey is ALWAYS going to be the same

        //init the 2D array of notes/lengths
        this.probibsDur = new double[keys][durArray.length];
        // go from a duration to an index pos
        // the pos being for proibsDur
        HashMap<Long, Integer> durToIndexPos = new HashMap<>();

        //init durToindexpos by looping through the durArray and having i incre3ase by 1
        for(int i = 0; i < durArray.length; i += 1)
        {
            durToIndexPos.put(durArray[i], i);
        }

        //set each probablity defalt to zero
        for(int row = 0; row < keys; row += 1)
        {
            for(int col = 0; col < durArray.length; col += 1)
            {
                probibsDur[row][col] = 0;
            }
        }
        //loop through all the notes in the song and add the duration to the correct spot

        for(Note notes : song)
        {
            // probibs[numToKey.get(note)][numToKey.get(prevNote)] += 1;
            probibsDur[numToKey.get(notes.getName())][durToIndexPos.get(notes.getDuration())] += 1;
        }
        for(int i = 0; i < keys; i += 1)
        {
            if (occurences.get(keyLetters[i]) == 0)
            {
                continue;
            }
            PercentagizeRow(probibsDur[i], occurences.get(keyLetters[i]));
        }
    }

    public void noteChain(ArrayList<Note> song)
    {
        //letters is a string[] of all the notes in the song
        String[] letters = new String[song.size()];

        for(int i = 0; i < song.size(); i += 1)
        {
            //puts the name of the note in the correct position in the string[] letters
            letters[i] = song.get(i).getName();
        }

        //make the hasmaps have the k, v pair of k = the notes in keyLetters
        //keLetters is equal to {"c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "b", "rest"}
        //LINE 25
        for(int i = 0; i < keys; i += 1)
        {
            //v is the index position of the letter in the list of notes or letters
            numToKey.put(keyLetters[i], i);

            // v is 0 by default
            //increased by 1 every time the letter shows up to create percentages
            occurences.put(keyLetters[i], 0);
        }

        //for every key create an array with the 0th index being the first note in keyLetters and set the dfalut chance to 0
        for(int row = 0; row < keys; row += 1)
        {
            for(int col = 0; col < keys; col += 1)
            {
                //defalut chance is 0
                probibs[row][col] = 0;
            }
        }
        String prevNote = "";
        int noteNum = 0;
        //iterate through the whole song (letters) //LINE 94-99
        for(String note : letters)
        {
            if(noteNum == 0)
            {
                prevNote = note;
            }
            else
            {
                // probibs[numToKey.get(note)][numToKey.get(prevNote)] += 1;
                probibs[numToKey.get(prevNote)][numToKey.get(note)] += 1;
                occurences.put(note, occurences.get(note) + 1);
                prevNote = note;
                if(note == letters[letters.length-1])
                {
                    occurences.put(note, occurences.get(note) - 1);
                }
            }
            noteNum += 1;

        }
        for(int i = 0; i < keys; i += 1)
        {
            if (occurences.get(keyLetters[i]) == 0)
            {
                continue;
            }
            PercentagizeRow(probibs[i], occurences.get(keyLetters[i]));
        }
    }

     public void PercentagizeRow(double[] list, double divide)
    {
        for(int i = 0; i < list.length; i +=1) 
        {
            list[i] = list[i]/divide;
        }
    }
    public Note getNext(Note Start)
    {
        Note finalNote = new Note("f", 120, 4);

        ////generate next note;
        int i = 0;
        double total = 0;
        double target = new Random().nextDouble();
        for(double val : probibs[numToKey.get(Start.getName())])
        {
            total += val;
            if(total >= target)
            {
                finalNote.setNote(keyLetters[i]);
                break;
            }  
            i += 1;
        }
        
        ////generate duration;
        i = 0;
        total = 0;
        target = new Random().nextDouble();
        for(double val : probibsDur[numToKey.get(finalNote.getName())])
        {
            total += val;
            if (total >= target)
            {
                finalNote.setDuration(_durArray[i]);
                //System.out.println(finalNote.getDuration());
                break;
            }
            i += 1;
        }
        return finalNote;
    }
    public String toString()
    {
        String theString = "";
        //NOTES
        for(int row = 0; row < keys; row += 1)
        {
            theString = theString + "\n\n" + keyLetters[row];
            for(int col = 0; col < keys; col += 1)
            {
                theString = theString + "\n  " + keyLetters[col] + " " + probibs[row][col] + "%";
            }
        }

        //DURS
        // for(int row = 0; row < keys; row += 1)
        // {
        //     theString = theString + "\n\n" + keyLetters[row];
        //     for(int col = 0; col < this.probibsDur[0].length; col += 1)
        //     {
        //         theString = theString + "\n  " + this._durArray[col] + " " + probibsDur[row][col] + "%";
        //     }
        // }
        return theString;
    }
    
    // public static void main(String[] args) throws Exception
    // {
    //     //int Reader
    //     MidiReader myReader = new MidiReader();
    //     //make a noteArray of the notes from the song
    //     ArrayList<Note> originalSong = myReader.getNewSong("FurElise.mid");
    //     //https://musiclab.chromeexperiments.com/Song-Maker/song/5176723736363008
    //     // System.out.println(myString);
    //     //init Midi Maker
    //     MidiOut output = new MidiOut();

    //     //Create the Queue for my new song
    //     Queue<Note> mySong = new LinkedList<Note>();

    //     //init the Markov chain
    //     MarkovChain myChain = new MarkovChain(originalSong);

    //     mySong.offer(originalSong.get(originalSong.size()-1));

    //     System.out.println(myChain);
        // for(int i = 0; i < 15; i += 1)
        // {
        //     //AHHAHAHAHHA STUPID IT'S A QUEUE SO IT'S FIRST IN FIRST OUT NOT FIRST IN LAST OUT SO IT'S 
        //     //ALWAYS oing to have the same peek value!
        //     //System.out.print(" : " + mySong.peek().getName()); this alwasy prints the first in
        //     Note noteToAdd = myChain.getNext(mySong.peek());
        //     //System.out.print(noteToAdd.getName());
        //     mySong.offer(noteToAdd);
        //     //System.out.println(noteToAdd);
        // }
        
        // for(int i = 0; i < mySong.size(); i +=1)
        // {
        //     //System.out.print(" " + mySong.peek().getName());
        //     //System.out.println(" " + mySong.peek().getDuration());
        //     mySong.offer(mySong.poll());
        // }

        // output.makeSong(mySong, "mySickTitle");
    // }
}
