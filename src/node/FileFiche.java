package node;

import java.util.ArrayList;
import java.util.List;

public class FileFiche {

    private String fileName;
    private List<Integer> locationList = new ArrayList<>();
    private int ownerId;
    private String ownerIp;

    public FileFiche(String name, int id, String ip)
    {
        this.fileName = name;
        this.ownerId = id;
        this.ownerIp = ip;
        
        locationList.add(id);
        System.out.println("Filefiche" + locationList);
        System.out.println("Filefiche" + name);
        System.out.println("Filefiche" + id);
        System.out.println("Filefiche" + ip);
    }

    public void addLocation(int id)
    {
        locationList.add(id);
    }

    public void rmLocation(int id)
    {
        for (int i = 0; i < locationList.size(); i++)
        {
            if (locationList.get(i).equals(id))
                locationList.remove(i);
        }
    }

}
