public class Note
{
    private String noteName;
    private long noteDuration;
    private int noteOctave;
    public Note(String _noteName, long _noteDuration, int _noteOctave)
    {
        this.noteName = _noteName;
        this.noteDuration = _noteDuration; 
        this.noteOctave = _noteOctave;
    }  
    public String getName()
    {
        return this.noteName;
    }

    public long getDuration()
    {
        return this.noteDuration;
    }

    public int getOctave()
    {
        return this.noteOctave;
    }

    public void setDuration(long _duration)
    {
        this.noteDuration = _duration;
    }
    public void setNote(String _noteName)
    {
        this.noteName = _noteName;
    }

    public void setOctave(int _octave)
    {
        this.noteOctave = _octave;
    }

    public String toString()
    {
        return this.getName().toUpperCase() + " Length: " + this.getDuration() + " Octave: " + this.getOctave();
    }
}
