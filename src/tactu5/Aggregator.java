package tactu5;

/**
 * Aggregator class generates objects to feed the sequencer. It's possible to
 * pass it Sequence and ClusterSequence objects. You could create many
 * Aggregator objects and use them to change the internal sequencer contents in
 * real time.
 * 
 * @author Alessandro Capozzo
 * @author RBDGT (www.rbdgt.be)
 *
 */

// generate clusters sequences for the sequencer
public class Aggregator {
	private Sequence[] sequencesContainer; // the container of all the stored sequence
	private ClusterSequence[] clustersequencesContainer;	// the container of all the stored sequence of clusters
	private InternalSequence score;	// the result of aggregation process

	// unique id store
	private int idSequenceCounter;
	private int idClusterSequenceCounter;

	public Aggregator() {
		// init containers
		sequencesContainer = new Sequence[0];
		clustersequencesContainer = new ClusterSequence[0];
		score = new InternalSequence();
		// init counters
		idSequenceCounter = 0;
		idClusterSequenceCounter = 0;
	}

	/**
	 * Add a sequence with no offset declared.
	 * 
	 * @param s Sequence
	 * 
	 */
	public void addSequence(Sequence s) {
		addSequence(s, 0.0f);
	}

	/**
	 * Add a sequence to Aggregator container, it allows to define a time
	 * offset in milliseconds.
	 * 
	 * 
	 * @param s Sequence
	 * @param offSet float (milliseconds)
	 */
	public void addSequence(Sequence s, float offSet) {
		// transform sequence in clustersequence
		int seqLength = s.getSequenceLength();
		ClusterSequence tempCSeq = new ClusterSequence();
		Cluster tempCluster;
		for (int inxS = 0; inxS < seqLength; inxS++) {
			tempCluster = new Cluster();
			tempCluster.addNote(s.getNote(inxS));
			tempCSeq.addCluster(tempCluster);
		}
		addClusterSequence(tempCSeq, offSet);
	}

	/**
	 * Add ClusterSequence to Aggregator container.
	 * 
	 * 
	 * @param cs	Clustersequence
	 */
	public void addClusterSequence(ClusterSequence cs) {
		addClusterSequence(cs, 0.0f);
	}

	/**
	 * Add ClusterSequence to Aggregator container, it allows to define a time
	 * offset in milliseconds.
	 * 
	 * 
	 * @param cs	ClusterSequence
	 * @param offSet	float (milliseconds)
	 */
	public void addClusterSequence(ClusterSequence cs, float offSet) {
		// add sequence to the score
		if (score.getClusterNumber() > 0) {
			insertClusterSequence(cs, offSet);
		} else {
			// fill the score with first sequence
			for (int j = 0; j < cs.getClusterNumber(); j++) {
				score.addCluster(cs.getCluster(j));
			}
		}
	}

	/** Add a ClusterSequence to the score, starting from a offSet time
	 * 
	 * @param cs	ClusterSequence
	 * @param offSet	float (milliseconds)
	 */
	private void insertClusterSequence(ClusterSequence cs, float offSet) {
		//
		int scoreIndex = 0;
		float clusterTime;
		for (int i = 0; i < cs.getClusterNumber(); i++) {
			clusterTime = cs.getTimeAtStep(i) + offSet;
			System.out.println(clusterTime + "eccolo");
			for (int j = scoreIndex; j < score.getClusterNumber(); j++) {
				// ERRORE CONTROLLARE
				if (clusterTime == score.getTimeAtStep(j)) {
					// add notes to existing cluster
					// aggiunger elemnti al cluster esistente, per ora forza
					// bruta
					// score.
					score.addToCluster(cs.getCluster(i), j);
					scoreIndex = j;
					break;
				} else if (j == score.getClusterNumber() - 1) {
					// it creates a new cluster in the internal sequence
					score.insertCluster(cs.getCluster(i), j + 1, clusterTime);
					scoreIndex = j;
					break;
				} else if ((j == 0) && (clusterTime < score.getTimeAtStep(0))) {
					// it creates a new cluster in the internal sequence
					score.insertCluster(cs.getCluster(i), 0, clusterTime);
					scoreIndex = j;
					break;
				} else if ((clusterTime > score.getTimeAtStep(j)) && (clusterTime < score.getTimeAtStep(j + 1))) {
					// it creates a new cluster in the internal sequence
					score.insertCluster(cs.getCluster(i), j + 1, clusterTime);
					scoreIndex = j;
					break;
				}
			}
		}
	}

	/**
	 * 
	 * @param s	Sequence
	 * @return	int
	 */
	int addAndStoreSequence(Sequence s) {
		idSequenceCounter++;
		addSequence(s, 0.0f);
		return idSequenceCounter;
	}

	/**
	 * 
	 * @param cs ClusterSequence
	 * @return	int
	 */
	int addAndStoreClusterSequence(ClusterSequence cs) {
		//TODO Fix addAndStoreClusterSequence(ClusterSequence cs)!
		// int id;
		return 1;
	}

	/**
	 * 
	 * @param s	Sequence
	 * @param offSet	float (milliseconds)
	 * @return	int
	 */
	int addAndStoreSequence(Sequence s, float offSet) {
		idSequenceCounter++;
		addSequence(s, offSet);
		return idSequenceCounter;
	}

	/**
	 * 
	 * @param cs	ClusterSequence
	 * @param offSet	float (milliseconds)
	 * @return	int
	 */
	int addAndStoreClusterSequence(ClusterSequence cs, float offSet) {
		//TODO Fix addAndStoreClusterSequence(ClusterSequence cs, float offSet)!
		// int id;
		return 1;
	}

	/**
	 * Returns the aggregated score.
	 * It's necessary to call this method to feed the Tactu5 internal sequencer.
	 * 
	 * 
	 * @return	InternalSequence
	 */
	public InternalSequence getScore() {
		return score;
	}

	/**
	 * Reset the Aggregator-object.
	 * 
	 * 
	 * 
	 */
	public void resetAll() {
		// init containers
		sequencesContainer = new Sequence[0];
		clustersequencesContainer = new ClusterSequence[0];
		score = new InternalSequence();
		// init counters
		idSequenceCounter = 0;
		idClusterSequenceCounter = 0;
	}

}