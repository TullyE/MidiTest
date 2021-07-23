import java.util.HashMap;

public class MarkovChain
{
    private HashMap<String, Integer> numToKey = new HashMap<>();
    private HashMap<String, Integer> occurences = new HashMap<>();
    private int keys = 13;
    private String[] list;
    private double[][] probibs = new double[keys][keys];
    private String[] keyLetters = new String[]{"c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "b", "rest"};
    
    public MarkovChain(String[] letters)
    {
        for(int i = 0; i < keys; i += 1)
        {
            numToKey.put(keyLetters[i], i);
            occurences.put(keyLetters[i], 0);
        }
        for(int row = 0; row < keys; row += 1)
        {
            for(int col = 0; col < keys; col += 1)
            {
                probibs[row][col] = 0;
            }
        }
        String prevNote = "rest";
        for(String note : letters)
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

    public String toString()
    {
        String theString = "";
        for(int row = 0; row < keys; row += 1)
        {
            theString = theString + "\n\n" + keyLetters[row];
            for(int col = 0; col < keys; col += 1)
            {
                theString = theString + "\n  " + keyLetters[col] + " " + probibs[row][col] + "%";
            }
        }
        return theString;
    }
    public static void main(String[] args)
    {
        String[] myString = new String[]{"a", "b", "a", "b", "f", "c", "d", "e", "f", "g", "a", "c", "d","a", "d", "b", "f", "g"};
        MarkovChain myChain = new MarkovChain(myString);
        System.out.println(myChain);
    }
}
