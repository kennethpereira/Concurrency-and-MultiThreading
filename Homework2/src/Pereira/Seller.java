package Pereira;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.naming.SizeLimitExceededException;

public class Seller implements Client
{
	private static final int MaxItems = 100;
	
	private String name;
	private int cycles;
	private int maxSleepTimeMs;
	
	private List<String> items;
	private Random rand;
	
	private AuctionServer server;
	
	public Seller(AuctionServer server, String name, int cycles, int maxSleepTimeMs, long randomSeed)
	{
		this.name = name;
		this.cycles = cycles;
		this.maxSleepTimeMs = maxSleepTimeMs;
		
		// Generate items
		this.rand = new Random(randomSeed);
        int itemCount = MaxItems;
        this.items = new ArrayList<String>();
        
        for (int i = 0; i < itemCount; ++i)
        {
            items.add(this.name() + "#" + i);
        }
        
        this.server = server;
	}
	
	@Override
	public String name()
	{
		return this.name;
	}

	@Override
    public void run()
    {			
		for (int i = 0; i < this.cycles && this.items.size() > 0; ++i)
	    {
	    	int index = this.rand.nextInt(this.items.size());
	    	String item = this.items.get(index);
	    	
	    	int listingID = 0;
			try {
				listingID = server.submitItem(this.name(), item, this.rand.nextInt(100), this.rand.nextInt(100) + 100);
			} catch (SizeLimitExceededException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
	    	
	    	if (listingID != -1)
	    	{
	    		this.items.remove(index);
	    	}

    		try
            {
                Thread.sleep(this.rand.nextInt(this.maxSleepTimeMs));
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                return;
            }
	    }
    }
}