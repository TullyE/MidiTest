/**
HomepageMenu.java
has the graphics code for the Homepage
Tully Eva
07/29/2021
*/
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
    private JComboBox<String> midiFiles;
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
    //Duration Slider
    private int sliderMax = 1000;
    private int sliderMin = 10;
    private int sliderInit = 500;
    private JSlider SongDurationSlider = new JSlider(JSlider.HORIZONTAL, sliderMin, sliderMax, sliderInit);
    //MUSIC STUFF NOW!
    //Duration
    private int customSongDuration = 500;
    //int Reader
    MidiReader myReader;
    //make a noteArray of the notes from the song when the user selects from the list
    ArrayList<Note> originalSong;
    //init Midi Maker
    MidiOut output; // = new MidiOut();
    //Create the Queue for my new song
    Queue<Note> mySong = new LinkedList<Note>();
    //init the Markov chain
    MarkovChain myChain;
    /**
     * Default constructor for HomepageMenu
     * create/setup all the components and add them to the screen
     */
    public HomepageMenu()
    {
        //initiate the screen 
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1000, 1000));
        this.setSize(new Dimension(1000, 1000));
        this.setLayout(null);
        
        //set up the drop down menu
        File dir = new File(System.getProperty("user.dir"));
        midiFileNames = showFiles(dir.listFiles());
        midiFiles = new JComboBox<String>(midiFileNames);
        midiFiles.setSelectedItem(midiFileNames.length-1);
        selectedMidiFile = (String) midiFiles.getSelectedItem();


        FileNameInputArea.setEditable(false);
        
        //create action listeners
        PlayNewSong.addActionListener(new PlayNewSongAction());
        PlayOriginalSong.addActionListener(new PlayOriginalSongAction());
        SaveNewSong.addActionListener(new SaveNewSongAction());
        midiFiles.addActionListener(new JComboBoxAction());
        StopSound.addActionListener(new StopSoundAction());
        FileNameInputField.addActionListener(new FileNameInputAction());
        SongDurationSlider.addChangeListener(new DurationSliderAction());

        //set the size of all the components
        PlayNewSong.setBounds(425,425, 150, 150);
        PlayOriginalSong.setBounds(200,425, 150, 150);
        SaveNewSong.setBounds(650, 425, 150, 150);
        midiFiles.setBounds(200, 575, 150, 25);
        StopSound.setBounds(775, 780, 75, 75);
        FileNameInputField.setBounds(650, 575, 150, 25);
        SongDurationSlider.setBounds(425, 580, 150, 25);
        
        //add all the components to the screen
        this.add(PlayNewSong); 
        this.add(PlayOriginalSong);
        this.add(SaveNewSong);
        this.add(midiFiles);
        this.add(StopSound);
        this.add(FileNameInputArea);
        this.add(FileNameInputField);
        this.add(SongDurationSlider);
    }
    /**
     * set the background aswell as the text for the slider
     * @param g the Graphics object to protect
     */
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        Toolkit t = Toolkit.getDefaultToolkit();
        Image o = t.getImage("Generative Music.png");
        g2.drawImage(o, 0, 0, this);

        //Draw the font 
        g2.setFont(new Font("Monospaced", Font.BOLD, 22));
        g2.setColor(new Color(164, 164, 163));
        g2.drawString("Duration: " + customSongDuration, 425, 620);
    }
    /**
     * play a specified midi file
     * https://riptutorial.com/java/example/621/play-a-midi-file
     * @param FileName SONG to play
     */
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
    /**
     * ActionListener for the Play New Song button
     */
    private class PlayNewSongAction implements ActionListener
    {
        /**
         * Invoked when Play New Song click action occurs.
         * creates a Markov Chain and generates and plays a new song with the customSongDuration length
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            myReader = new MidiReader();
            try
            {
                originalSong = myReader.getNewSong(selectedMidiFile);
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
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
    /**
     * ActionListener for the Play Original Song button
     */
    private class PlayOriginalSongAction implements ActionListener
    {
        /**
         * Invoked when Play Original Song click action occurs.
         * plays the original song 
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            playFile((String) midiFiles.getSelectedItem());
        }
    }
    /**
     * ActionListener for the Save New Song button
     */
    private class SaveNewSongAction implements ActionListener
    {
        /**
         * Invoked when Save New Song click action occurs.
         * Saves the new song as the text in the textbox
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.print(FileNameInputField.getText());
            MidiOut newOutput = new MidiOut();
            newOutput.makeSong(mySong, FileNameInputField.getText());
        }
    }
    /**
     * ActionListener for the JComboBox button
     */
    private class JComboBoxAction implements ActionListener
    {
        /**
         * Invoked when a JComboBox action occurs.
         * Selects a song
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            selectedMidiFile = (String) midiFiles.getSelectedItem();
        }
    }
    /**
     * ActionListener for the Stop Sound button
     */
    private class StopSoundAction implements ActionListener
    {
        /**
         * Invoked when SoupSound action occurs.
         * Stops all sound output
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(sequencer != null)
            {
                sequencer.stop();
            }
        }
    }
    /**
     * ActionListener for the Typing Text Box
     */
    private class FileNameInputAction implements ActionListener
    {
        /**
         * Invoked when KeyTyped action occurs.
         * updates the text box
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String text = FileNameInputField.getText();
            FileNameInputArea.append(text + "\n");
            FileNameInputArea.selectAll();
            FileNameInputArea.setCaretPosition(FileNameInputArea.getDocument().getLength());
        }
    }
    /**
     * ActionListener for the Slider
     */
    private class DurationSliderAction implements ChangeListener
    {
        /**
         * Invoked when a slider update action occurs.
         * updates slider Icon and repaints
         * @param e
         */
        @Override
        public void stateChanged(ChangeEvent e)
        {
            JSlider source = (JSlider)e.getSource();
            customSongDuration = source.getValue(); 
            repaint();
        }
    }
    /**
     * default ActionListener never used however is requrired
     */
    @Override
    public void actionPerformed(ActionEvent e){}
    /**
     * iterate through all the files in this directory. if the file ends in mid
     * then use recursion to get rid of everything before the last \.
     *Link 1: https://stackoverflow.com/questions/4871051/how-to-get-the-current-working-directory-in-java
     *Link 2: https://stackoverflow.com/questions/3154488/how-do-i-iterate-through-the-files-in-a-directory-in-java
     * @param files
     * @return a String array of all the midi files
     */
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
    /**
     * recursivly remove everthing before the last \
     * @param name
     * @return the finished string OR the string with 1 less \
     */
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