import java.util.ArrayList;
import java.util.Collections;

public class NoRepeats 
{
    private ArrayList<Long> noDuplicates = new ArrayList<Long>();

    public NoRepeats()
    {
    }

    public Long[] removeDuplicates(Long[] List)
    {
        for(int i = 0; i < List.length; i += 1)
        {
            this.noDuplicates.add(List[i]);
        }
        Collections.sort(this.noDuplicates);
        Long last = noDuplicates.get(noDuplicates.size()-1);
        for(int i = noDuplicates.size()-2; i > 0; i -= 1)
        {
            if(noDuplicates.get(i) == last)
            {
                noDuplicates.remove(i);
            }
            else
            {
                last = noDuplicates.get(i);
            }
        }
        Long[] finalList = new Long[noDuplicates.size()];
        for(int i = 0; i < noDuplicates.size(); i +=1)
        {
            finalList[i] = noDuplicates.get(i);
        }
        return finalList;
    }
    public static void main(String[] args)
    {
        Long[] test = new Long[]{3L, 5L, 4L, 4L, 5L, 5L, 3L, 2L};
        NoRepeats myTest = new NoRepeats();
        Long[] updatedTest = myTest.removeDuplicates(test);
        for(int i = 0; i < test.length; i += 1)
        {
            System.out.println(test[i]);
        }
        System.out.println("UPDATED");
        for(int i = 0; i < updatedTest.length; i += 1)
        {
            System.out.println(updatedTest[i]);
        }
    }
}
