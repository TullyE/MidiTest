import java.io.File;
import java.util.ArrayList;

public class Demo {

    public static void main(String... args) {
        File dir = new File(System.getProperty("user.dir"));
        showFiles(dir.listFiles());
    }

    public String[] showFiles(File[] files)
    {
        ArrayList<String> midis = new ArrayList<String>();
        for (File file : files)
        {
            int two = file.getAbsolutePath().length();
            int one = file.getAbsolutePath().length() - 3;
            if(file.getAbsolutePath().substring(one, two).equals("mid"))
            {
                midis.add(file.getAbsolutePath());

                System.out.println(getFileName(file.getAbsolutePath()));
            }
            //System.out.println("File: " + file.getAbsolutePath());
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