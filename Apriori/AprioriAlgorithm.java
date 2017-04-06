import java.util.*;
import java.io.*;

/*calculates the frequency item sets of a CSV from a transactional database.
uses the Apriori algorithm in order to determine the itemsets that have
related frequenceis amount the transactions in the database*/
public class AprioriAlg extends Observable {


    public static void main(String[] args)  {
        AprioriAlg ap = new AprioriAlg(args);
    }

    private List<int[]> itemsets ;
    private String transactions;
    private int totalItems;
    private int numTransactions; 

    private double minSup; 
    

    private boolean usedAsLibrary = false;

    public  AprioriAlg(String[] args, Observer ob) 
    {
    	usedAsLibrary = true;
    	configure(args);
    	this.addObserver(ob);
    	start();
    }

    private void log(String message) {
    	if (!usedAsLibrary) {
    		System.err.println(message);
    	}
    }

    public  AprioriAlg(String[] args) 
    {
        configure(args);
        start();
    }

    private void start()  {
        long start = System.currentTimeMillis();

        ItemSetSize1();        
        int itemsetnum=1;
        int nbFrequentSets=0;
        
        while (itemsets.size()>0)
        {

            computeFrequencyItemsets();

            if(itemsets.size()!=0)
            {
                nbFrequentSets+=itemsets.size();
                log("Found "+itemsets.size()+" frequent itemsets of size " + itemsetnum + " (with support "+(minSup*100)+"%)");;
                GenerateNewItemSets();
            }

            itemsetnum++;
        } 

        long end = System.currentTimeMillis();
        log("Execution time is: "+((double)(end-start)/1000) + " seconds.");
        log("Found "+nbFrequentSets+ " frequents sets for support "+(minSup*100)+"% (absolute "+Math.round(numTransactions*minSup)+")");
        log("Done");
    }

    private void configure(String[] args) 
    {        
        if (args.length!=0) transactions = args[0];
        else transactions = "chess.dat"; // default
    	
    	if (args.length>=2) minSup=(Double.valueOf(args[1]).doubleValue());    	
    	else minSup = .8;
    	totalItems = 0;
    	numTransactions=0;
    	BufferedReader data_in = new BufferedReader(new FileReader(transactions));
    	while (data_in.ready()) {    		
    		String line=data_in.readLine();
    		if (line.matches("\\s*")) continue; 
    		numTransactions++;
    		StringTokenizer t = new StringTokenizer(line," ");
    		while (t.hasMoreTokens()) {
    			int x = Integer.parseInt(t.nextToken());
    			if (x+1>totalItems) totalItems=x+1;
    		}    		
    	}  
    	
        outputConfig();

    }
     private void foundFrequentItemSet(int[] itemset, int support) {
    	if (usedAsLibrary) {
            this.setChanged();
            notifyObservers(itemset);
    	}
    	else {System.out.println(Arrays.toString(itemset) + "  ("+ ((support / (double) numTransactions))+" "+support+")");}
    }

    private void ItemSetSize1() {
		itemsets = new ArrayList<int[]>();
        for(int i=0; i<totalItems; i++)
        {
        	int[] cand = {i};
        	itemsets.add(cand);
        }
	}
	
	private void computeFrequencyItemsets() 
    {
    	
        log("Using data to calculate frequency of " + itemsets.size()+ " itemsets of size "+itemsets.get(0).length);

        List<int[]> frequentCandidates = new ArrayList<int[]>();

        boolean match; 
        int count[] = new int[itemsets.size()]; 

		BufferedReader data_in = new BufferedReader(new InputStreamReader(new FileInputStream(transactions)));

		boolean[] trans = new boolean[totalItems];
		
		for (int i = 0; i < numTransactions; i++) {
			String line = data_in.readLine();
			createBooleanArray(line, trans);

			for (int c = 0; c < itemsets.size(); c++) {
				match = true; 
				int[] cand = itemsets.get(c);
				for (int xx : cand) {
					if (trans[xx] == false) {
						match = false;
						break;
					}
				}
				if (match) { 
					count[c]++;
				}
			}

		}
		
		data_in.close();

		for (int i = 0; i < itemsets.size(); i++) {

			if ((count[i] / (double) (numTransactions)) >= minSup) {
				foundFrequentItemSet(itemsets.get(i),count[i]);
				frequentCandidates.add(itemsets.get(i));
			}
		}

        itemsets = frequentCandidates;
    }
    		
    private void GenerateNewItemSets()
    {
    	// by construction, all existing itemsets have the same size
    	int currentSizeOfItemsets = itemsets.get(0).length;
    	log("Generating itemsets of size "+(currentSizeOfItemsets+1)+" based on "+itemsets.size()+" itemsets of size "+currentSizeOfItemsets);
    		
    	HashMap<String, int[]> tempCandidates = new HashMap<String, int[]>(); //temporary candidates
    	
        // compare each pair of itemsets of size n-1
        for(int i=0; i<itemsets.size(); i++)
        {
            for(int j=i+1; j<itemsets.size(); j++)
            {
                int[] X = itemsets.get(i);
                int[] Y = itemsets.get(j);

                assert (X.length==Y.length);
                
                //make a string of the first n-2 tokens of the strings
                int [] newCand = new int[currentSizeOfItemsets+1];
                for(int s=0; s<newCand.length-1; s++) {
                	newCand[s] = X[s];
                }
                    
                int ndifferent = 0;
                // then we find the missing value
                for(int s1=0; s1<Y.length; s1++)
                {
                	boolean found = false;
                	// is Y[s1] in X?
                    for(int s2=0; s2<X.length; s2++) {
                    	if (X[s2]==Y[s1]) { 
                    		found = true;
                    		break;
                    	}
                	}
                	if (!found){ // Y[s1] is not in X
                		ndifferent++;
                		// we put the missing value at the end of newCand
                		newCand[newCand.length -1] = Y[s1];
                	}
            	
            	}
                
                // we have to find at least 1 different, otherwise it means that we have two times the same set in the existing candidates
                assert(ndifferent>0);
                
                
                if (ndifferent==1) {
                    // HashMap does not have the correct "equals" for int[] :-(
                    // I have to create the hash myself using a String :-(
                	// I use Arrays.toString to reuse equals and hashcode of String
                	Arrays.sort(newCand);
                	tempCandidates.put(Arrays.toString(newCand),newCand);
                }
            }
        }
        
        //set the new itemsets
        itemsets = new ArrayList<int[]>(tempCandidates.values());
    	log("Generated "+itemsets.size()+" unique itemsets of size "+(currentSizeOfItemsets+1));

    }

    private void createBooleanArray(String line, boolean[] trans) {
	    Arrays.fill(trans, false);
	    StringTokenizer stFile = new StringTokenizer(line, " "); 
	    while (stFile.hasMoreTokens())
	    {
	    	
	        int parsedVal = Integer.parseInt(stFile.nextToken());
			trans[parsedVal]=true; //if it is not a 0, assign the value to true
	    }
    }
}