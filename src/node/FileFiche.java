package node;

import java.util.List;

public class FileFiche {

    private String fileName;
    private List<Integer> locationList;
    private int ownerId;
    private String ownerIp;

    public FileFiche(String name, int id, String ip)
    {
        this.fileName = name;
        this.ownerId = id;
        this.ownerIp = ip;

        locationList.add(id);
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
