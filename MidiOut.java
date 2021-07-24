public class MidiOut
{
    public MidiOut()
    {

    }
    public void createFile()
    {
        Player player = new Player();
        File file = new File("out.mid")
        String s = "A B C D E F G";
        player.saveMidi(s, file);
    }

}
