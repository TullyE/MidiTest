import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class HomepageMenu extends JPanel implements ActionListener
{
    private String selectedMidiFile;

    boolean transition = false;


    //https://stackoverflow.com/questions/2544759/java-reading-images-and-displaying-as-an-imageicon
    //Play New Song Button
    private ImageIcon im = new ImageIcon(Toolkit.getDefaultToolkit().createImage("PlayNewSong.png"));
    private JButton PlayNewSong = new JButton(im);

    //Play Original Song Button
    private ImageIcon im1 = new ImageIcon(Toolkit.getDefaultToolkit().createImage("PlayOriginalSong.png"));
    private JButton PlayOriginalSong = new JButton(im1);

    //Save New Song Button
    private ImageIcon im2 = new ImageIcon(Toolkit.getDefaultToolkit().createImage("SaveNewSong.png"));
    private JButton SaveNewSong = new JButton(im2);

    //File Selector
    private JComboBox midiFiles;
    private String[] midiFileNames;

    //play sound
    private Sequencer sequencer;
    private Sequence sequence;

    //Stop Sound Button
    private ImageIcon im3 = new ImageIcon(Toolkit.getDefaultToolkit().createImage("StopPlayingSound.png"));
    private JButton StopSound = new JButton(im3);

    //File Name input text box
    private JTextField FileNameInputField = new JTextField(20);
    private JTextArea FileNameInputArea = new JTextArea(5, 20);

    //MUSIC STUFF NOW!

    //Duration
    private int customSongDuration = 500;

    //int Reader
    MidiReader myReader; // = new MidiReader();

    //make a noteArray of the notes from the song when the user selects from the list
    ArrayList<Note> originalSong; //= myReader.getNewSong("FurElise.mid");

    //init Midi Maker
    MidiOut output; // = new MidiOut();

    //Create the Queue for my new song
    Queue<Note> mySong = new LinkedList<Note>();

    //init the Markov chain
    MarkovChain myChain; // = new MarkovChain(originalSong);

    //Slider Max
    private int sliderMax = 1000;
    private int sliderMin = 10;
    private int sliderInit = 500;
    private JSlider SongDurationSlider = new JSlider(JSlider.HORIZONTAL, sliderMin, sliderMax, sliderInit);
    private int delay;
    private Timer timer;
    private boolean frozen = false;
    
    public HomepageMenu()
    {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1000, 1000));
        this.setSize(new Dimension(1000, 1000));
        this.setLayout(null);
        
        //https://stackoverflow.com/questions/4898584/java-using-an-image-as-a-button for empty buttons but not used
        // PlayNewSong.setBorder(BorderFactory.createEmptyBorder());
        // PlayNewSong.setContentAreaFilled(false);
        File dir = new File(System.getProperty("user.dir"));
        midiFileNames = showFiles(dir.listFiles());
        midiFiles = new JComboBox(midiFileNames);
        midiFiles.setSelectedItem(midiFileNames.length-1);
        selectedMidiFile = (String) midiFiles.getSelectedItem();

        //https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/SliderDemoProject/src/components/SliderDemo.java
        // SongDurationSlider.setMajorTickSpacing(4);
        // SongDurationSlider.setMinorTickSpacing(1);
        // SongDurationSlider.setPaintTicks(true);
        // SongDurationSlider.setPaintLabels(true);
        // Font font = new Font("Serif", Font.BOLD, 4);
        // SongDurationSlider.setFont(font);


        FileNameInputArea.setEditable(false);
        

        PlayNewSong.addActionListener(new PlayNewSongAction());
        PlayOriginalSong.addActionListener(new PlayOriginalSongAction());
        SaveNewSong.addActionListener(new SaveNewSongAction());
        midiFiles.addActionListener(new JComboBoxAction());
        StopSound.addActionListener(new StopSoundAction());
        FileNameInputField.addActionListener(new FileNameInputAction());
        SongDurationSlider.addChangeListener(new DurationSliderAction());

        PlayNewSong.setBounds(425,425, 150, 150);
        PlayOriginalSong.setBounds(200,425, 150, 150);
        SaveNewSong.setBounds(650, 425, 150, 150);
        midiFiles.setBounds(200, 575, 150, 25);
        StopSound.setBounds(775, 780, 75, 75);
        FileNameInputField.setBounds(650, 575, 150, 25);
        SongDurationSlider.setBounds(425, 580, 150, 25);
        
        this.add(PlayNewSong); 
        this.add(PlayOriginalSong);
        this.add(SaveNewSong);
        this.add(midiFiles);
        this.add(StopSound);
        this.add(FileNameInputArea);
        this.add(FileNameInputField);
        // this.add(sliderLabel);
        this.add(SongDurationSlider);
        
        timer = new Timer(delay, this);
        timer.setInitialDelay(delay * 7);
        timer.setCoalesce(true);
        
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        Toolkit t = Toolkit.getDefaultToolkit();
        Image o = t.getImage("Generative Music.png");
        g2.drawImage(o, 0, 0, this);

        g2.setFont(new Font("Monospaced", Font.BOLD, 22));
        g2.setColor(new Color(164, 164, 163));
        g2.drawString("Duration: " + customSongDuration, 425, 620);
    }
    // https://riptutorial.com/java/example/621/play-a-midi-file
    public void playFile(String FileName)
    {
        try
        {
            if(sequencer != null)
            {
                sequencer.stop();
            }
            sequencer = MidiSystem.getSequencer();
            if(sequencer == null)
            {
                System.err.println("Sequencer device not supported");
                return;
            }
            sequencer.open();
            sequence = MidiSystem.getSequence(new File((System.getProperty("user.dir") + "/" + FileName)));
            sequencer.setSequence(sequence);
            sequencer.start();
        }
        catch(MidiUnavailableException | InvalidMidiDataException | IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private class PlayNewSongAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("PlayNewSong");
            //get the original song 
            myReader = new MidiReader();
            // originalSong = myReader.getNewSong("FurElise.mid");
            try
            {
                //System.out.println(selectedMidiFile);
                originalSong = myReader.getNewSong(selectedMidiFile);
            }
            catch (Exception e1)
            {
                System.out.println(selectedMidiFile);
                //e1.printStackTrace();
            }
            output = new MidiOut();
            mySong = new LinkedList<Note>();
            myChain = new MarkovChain(originalSong);

            mySong.offer(originalSong.get(originalSong.size()-1));
            for(int i = 0; i < customSongDuration; i += 1)
            {
                Note noteToAdd = myChain.getNext(mySong.peek());
                mySong.offer(noteToAdd);
            }
            output.makeSong(mySong, "DefaultTitle");
            playFile("DefaultTitle.mid");
        }
    }

    private class PlayOriginalSongAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("PlayOriginalSong");
            //https://riptutorial.com/java/example/621/play-a-midi-file
            playFile((String) midiFiles.getSelectedItem());
        }
    }

    private class SaveNewSongAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.print(FileNameInputField.getText());
            System.out.println("SaveNewSong");
            MidiOut newOutput = new MidiOut();
            newOutput.makeSong(mySong, FileNameInputField.getText());
        }
    }

    private class JComboBoxAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println(midiFiles.getSelectedItem());
            selectedMidiFile = (String) midiFiles.getSelectedItem();
        }
    }

    private class StopSoundAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Sound Stopped");
            if(sequencer != null)
            {
                sequencer.stop();
            }
        }
    }

    private class FileNameInputAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String text = FileNameInputField.getText();
            FileNameInputArea.append(text + "\n");
            FileNameInputArea.selectAll();
            FileNameInputArea.setCaretPosition(FileNameInputArea.getDocument().getLength());
        }
    }

    private class DurationSliderAction implements ChangeListener
    {
        @Override
        public void stateChanged(ChangeEvent e)
        {
            JSlider source = (JSlider)e.getSource();
            // if (!source.getValueIsAdjusting())
            // {
               System.out.println(source.getValue());
               customSongDuration = source.getValue(); 
               repaint();
            //}
        }
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {

    }

    //https://stackoverflow.com/questions/4871051/how-to-get-the-current-working-directory-in-java
    //https://stackoverflow.com/questions/3154488/how-do-i-iterate-through-the-files-in-a-directory-in-java
    public String[] showFiles(File[] files)
    {
        ArrayList<String> midis = new ArrayList<String>();
        for (File file : files)
        {
            int two = file.getAbsolutePath().length();
            int one = file.getAbsolutePath().length() - 3;
            if(file.getAbsolutePath().substring(one, two).equals("mid"))
            {
                midis.add(getFileName(file.getAbsolutePath()));
            }
        }
        String[] output = new String[midis.size()];
        for(int i = 0; i < midis.size(); i += 1)
        {
            output[i] = midis.get(i);
        }
        return output;
    }

    public String getFileName(String name)
    {
        if(name.indexOf("\\") == -1)
        {
            return name;
        }
        else
        {
            return getFileName(name.substring(name.indexOf("\\") + 1, name.length()));
        }
    }
}