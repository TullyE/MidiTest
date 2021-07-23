import javax.sound.midi.spi.MidiFileReader;
import javax.sound.midi.spi.*;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MidiTestSolo
{
    public static void readFile() throws InvalidMidiDataException, IOException
    {
        int NOTE_ON = 0x90;
        int NOTE_OFF = 0x80;
        File file = new File("Birthday.mid");
        Sequence mySong = MidiSystem.getSequence(file);
        //https://www.geeksforgeeks.org/java-midi/
        //Track: It is a sequence of Midi events.
        //Sequence: It is a data structure containing multiple tracks and timing information.The sequencer takes in a sequence and plays it.  
        int i = 0;
        //iterate through each track
        for(Track track : mySong.getTracks())
        {
            //interate through each Midi event
            i += 1;
            MidiEvent event = track.get(i);
            if(event.getMessage() instanceof ShortMessage)
            {
                ShortMessage sm = (ShortMessage) event.getMessage()
                if(sm.getCommand() == NOTE_ON)
                {
                    System.out.println(sm.getData1());
                    System.out.println(sm.getDatat2());
                }
            }
        }
    }
    public static void main(String[] args) throws InvalidMidiDataException, IOException
    {
        readFile();
    }
}
