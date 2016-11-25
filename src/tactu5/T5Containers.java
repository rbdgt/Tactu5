package tactu5;

/*
 * TACTU5 by Alessandro Capozzo  
 * www.abstract-codex.net
 */

//////////////////////////////////////
//  interface for clusters          //
//  and sequences                   //
///////////////////////////////////////
public interface T5Containers {

	/**
	 * Add a note to the cluster.
	 * 
	 * @param inote Note
	 */
	void addNote(Note inote);

	/**
	 * 
	 * @return	int  (containernumber)
	 */
	int getContainerNum();

	/**
	 * Returns note at certain position.
	 * 
	 * @param index int (noteposition)
	 * @return Note
	 */
	Note getNote(int index);

}