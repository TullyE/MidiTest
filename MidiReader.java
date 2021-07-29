/**
MidiReader.java
reads in a midi file and creates an array of notes
Tully Eva
07/29/2021
*/
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

    /**
     * defalut constructor for the MidiReader
     */
    public MidiReader()
    {
        System.out.println("MidiReader Initialized!");
    }
    /**
     * convert a midi to an arraylist of notes
     * https://stackoverflow.com/questions/3850688/reading-midi-files-in-java
     * @param name
     * @return ArrayList of notes
     * @throws Exception
     */
    public ArrayList<Note> getNewSong(String name) throws Exception
    {
        //TULLY ADJUSTED ORIGINAL
        ArrayList<Note> mySong = new ArrayList<Note>();
        Sequence sequence = MidiSystem.getSequence(new File(name));
        for(Track track : sequence.getTracks())
        {
            long prevTick = 0L;
            for (int i=0; i < track.size(); i++)
            { 
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                
                if (message instanceof ShortMessage)
                {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE_ON)
                    {
                        long Duration = event.getTick() - prevTick;
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        mySong.add(new Note(noteName.toLowerCase(), Duration, octave));
                        prevTick = event.getTick();
                    }
                }
            }
        }
        return mySong;
    }
}