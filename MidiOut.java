import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Queue;
import javax.sound.midi.*; // package for all midi classes
public class MidiOut
{
    private long timeOn = 0;
    HashMap<String, Integer> nameToNum = new HashMap<>();
    private String[] keyLetters = new String[]{"c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "b", "rest"};
    public MidiOut()
    {
        int note = 0;
        for(int i = 60; i < 72; i += 1)
        {
            nameToNum.put(keyLetters[note], i);
            note += 1;
        }
    }
public void makeSong(Queue<Note> mySong) 
{
    System.out.println("midifile begin ");
	try
	{

        Sequence s = new Sequence(javax.sound.midi.Sequence.PPQ,24);

        //****  Obtain a MIDI track from the sequence  ****
        Track t = s.createTrack();

        //****  General MIDI sysex -- turn on General MIDI sound set  ****
        byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
        SysexMessage sm = new SysexMessage();
        sm.setMessage(b, 6);
        MidiEvent me = new MidiEvent(sm,(long)0);
        t.add(me);

        //****  set tempo (meta event)  ****
        MetaMessage mt = new MetaMessage();
        byte[] bt = {0x02, (byte)0x00, 0x00};
        mt.setMessage(0x51 ,bt, 3);
        me = new MidiEvent(mt,(long)0);
        t.add(me);

        //****  set track name (meta event)  ****
        mt = new MetaMessage();
        String TrackName = new String("midifile track");
        mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
        me = new MidiEvent(mt,(long)0);
        t.add(me);

        //****  set omni on  ****
        ShortMessage mm = new ShortMessage();
        mm.setMessage(0xB0, 0x7D,0x00);
        me = new MidiEvent(mm,(long)0);
        t.add(me);

        //****  set poly on  ****
        mm = new ShortMessage();
        mm.setMessage(0xB0, 0x7F,0x00);
        me = new MidiEvent(mm,(long)0);
        t.add(me);

        //****  set instrument to Piano  ****
        mm = new ShortMessage();
        mm.setMessage(0xC0, 0x00, 0x00);
        me = new MidiEvent(mm,(long)0);
        t.add(me);

        for(int i = 0; i < mySong.size(); i += 1)
        {
            int divisor = 4;
            int note = 0x5 + nameToNum.get(mySong.peek().getName());
            //int note = 0x5C;
            //Long dur = mySong.peek().getDuration();
            Long dur = 20L;
            
            // System.out.print("\nnote: " + mySong.peek().getName());
            // System.out.print(" duration: " + dur);

            //****  note on - middle C  ****
            mm = new ShortMessage();
            mm.setMessage(0x90,note,0x60);
            me = new MidiEvent(mm,(long)timeOn);
            t.add(me);

            //****  note off - middle C - 120 ticks later  ****
            mm = new ShortMessage();
            mm.setMessage(0x80,note,0x40);
            me = new MidiEvent(mm,(long)timeOn + dur * divisor);
            t.add(me);

            mySong.offer(mySong.poll());
            timeOn += dur * divisor;
        }

        //****  set end of track (meta event) 19 ticks later  ****
        mt = new MetaMessage();
        byte[] bet = {}; // empty array
        mt.setMessage(0x2F,bet,0);
        me = new MidiEvent(mt, (long)140);
        t.add(me);

        //****  write the MIDI sequence to a MIDI file  ****
        File f = new File("midifile.mid");
        MidiSystem.write(s,1,f);
        } //try
    catch(Exception e)
    {
        System.out.println("Exception caught " + e.toString());
    }   //catch
        System.out.println("midifile end ");
    }   //main
} //midifile