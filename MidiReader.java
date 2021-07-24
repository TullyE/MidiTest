import java.io.File;
import java.util.ArrayList;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiReader
{
    private int NOTE_ON = 0x90;
    private String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public MidiReader()
    {
        System.out.println("MidiReader Initialized!");
    }

    public ArrayList<String> getNewSong(String name) throws Exception
    {
        ArrayList<String> mySong = new ArrayList<String>();
        Sequence sequence = MidiSystem.getSequence(new File(name));
        //https://www.geeksforgeeks.org/java-midi/
        //Track: It is a sequence of Midi events.
        //Sequence: It is a data structure containing multiple tracks and timing information.The sequencer takes in a sequence and plays it.  
        int trackNumber = 0;
        for(Track track : sequence.getTracks())
        {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++)
            { 
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                
                if (message instanceof ShortMessage)
                {
                    ShortMessage sm = (ShortMessage) message;
                    //System.out.print("Channel: " + sm.getChannel() + " ");

                    if (sm.getCommand() == NOTE_ON)
                    {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        //System.out.println(noteName);
                        mySong.add(noteName.toLowerCase());
                        //System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    }
                }
            }
        }
        return mySong;
    }
}