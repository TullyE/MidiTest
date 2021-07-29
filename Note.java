/**
Note.java
has the note class
notes have a note, duration, octave
octave is never actually generated
Tully Eva
07/29/2021
*/

public class Note
{
    private String noteName;
    private long noteDuration;
    private int noteOctave;
    /**
     * contsturctor for the Note class
     * @param _noteName
     * @param _noteDuration
     * @param _noteOctave
     */
    public Note(String _noteName, long _noteDuration, int _noteOctave)
    {
        this.noteName = _noteName;
        this.noteDuration = _noteDuration; 
        this.noteOctave = _noteOctave;
    }  
    /**
     * get the note name
     * @return Note Name
     */
    public String getName()
    {
        return this.noteName;
    }
    /**
     * get the duration
     * @return duration
     */
    public long getDuration()
    {
        return this.noteDuration;
    }
    /**
     * get Octave
     * @return theOctave
     */
    public int getOctave()
    {
        return this.noteOctave;
    }
    /**
     * set the duration
     * @param _duration
     */
    public void setDuration(long _duration)
    {
        this.noteDuration = _duration;
    }
    /**
     * set the Note
     * @param _noteName
     */
    public void setNote(String _noteName)
    {
        this.noteName = _noteName;
    }
    /**
     * set the octave
     * @param _octave
     */
    public void setOctave(int _octave)
    {
        this.noteOctave = _octave;
    }
    /**
     * print the note name, lenght and octave
     * @return String of the note
     */
    public String toString()
    {
        return this.getName().toUpperCase() + " Length: " + this.getDuration() + " Octave: " + this.getOctave();
    }
}
