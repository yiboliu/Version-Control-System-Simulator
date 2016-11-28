import java.util.ArrayList;
import java.util.List;

/**
 * Represents a repository which stores and tracks changes to a collection of 
 * documents.
 * @author
 *
 */
public class Repo {

	/* The current version of the repo. */
	private int version;

	/* The name of the repo. It's a unique identifier for a repository. */
	private final String repoName;

	/* The user who is the administrator of the repo. */
	private final User admin;

	/* The collection(list) of documents in the repo. */
	private final List<Document> docs;

	/* The check-ins queued by different users for admin approval. */
	private final QueueADT<ChangeSet> checkIns;

	/* The stack of copies of the repo at points when any check-in was applied. */
	private final StackADT<RepoCopy> versionRecords; 

	/**
	 * Constructs a repo object.
	 * @param admin The administrator for the repo.
	 * @param reponame The name of the repo.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public Repo(User admin, String repoName) {
		// TODO: Implement this contructor. The following lines 
		// are just meant for the method to compile. You should 
		// remove or edit it in whatever way you like.
		if(admin==null||repoName==null) throw new IllegalArgumentException();
		this.admin = admin;
		this.repoName = repoName;
		this.checkIns = new SimpleQueue<ChangeSet>();
		this.versionRecords = new SimpleStack<RepoCopy>();
		this.docs = new ArrayList<Document>();
		this.versionRecords.push(new RepoCopy(repoName, 0, this.docs));
		this.version = 0;
	}

	/**
	 * Return the name of the repo.
	 * @return The name of the repository.
	 */
	public String getName() {
		return this.repoName;
	}

	/**
	 * Returns the user who is administrator for this repository.
	 * @return The admin user.
	 */
	public User getAdmin() {
		return this.admin;
	}

	/**
	 * Returns a copy of list of all documents in the repository.
	 * @return A list of documents.
	 */
	public List<Document> getDocuments() {
		return new ArrayList<Document>(this.docs);
	}

	/**
	 * Returns a document with a particular name within the repository.
	 * @param searchName The name of document to be searched.
	 * @return The document if found, null otherwise.
	 * @throws IllegalArgumentException if any argument is null.
	 */
	public Document getDocument(String searchName) {
		if (searchName == null) {
			throw new IllegalArgumentException();
		}

		for (Document d : this.docs) {
			if (d.getName().equals(searchName)) {
				return d;
			}
		}

		return null;
	}

	/**
	 * Returns the current version of the repository.
	 * @return The version of the repository.
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Returns the number of versions (or changes made) for this repository.
	 * @return The version count.
	 */
	public int getVersionCount() {
		// TODO: Implement this method. The following lines 
		// are just meant for the method to compile. You can 
		// remove or edit it in whatever way you like.
		return this.versionRecords.size();
	}

	/**
	 * Returns the history of changes made to the repository. 
	 * @return The string containing the history of changes.
	 */
	public String getVersionHistory() {
		// TODO: Implement this method. The following lines 
		// are just meant for the method to compile. You can 
		// remove or edit it in whatever way you like.
		return this.versionRecords.toString();
	}

	/**
	 * Returns the number of pending check-ins queued for approval.
	 * @return The count of changes.
	 */
	public int getCheckInCount() {
		// TODO: Implement this method. The following lines 
		// are just meant for the method to compile. You can 
		// remove or edit it in whatever way you like.
		return this.checkIns.size();
	}


	/**
	 * Queue a new check-in for admin approval.
	 * @param checkIn The check-in to be queued.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public void queueCheckIn(ChangeSet checkIn) {
		// TODO: Implement this method. 
		if(checkIn==null) throw new IllegalArgumentException();
		this.checkIns.enqueue(checkIn);
	}

	/**
	 * Returns and removes the next check-in in the queue 
	 * if the requesting user is the administrator.
	 * @param requestingUser The user requesting for the change set.
	 * @return The checkin if the requestingUser is the admin and a checkin
	 * exists, null otherwise.
	 * @throws EmptyQueueException 
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ChangeSet getNextCheckIn(User requestingUser){
		// TODO: Implement this method. The following lines 
		// are just meant for the method to compile. You can 
		// remove or edit it in whatever way you like.
		if(requestingUser==null) throw new IllegalArgumentException();
		if(this.admin.equals(requestingUser)&&!this.checkIns.isEmpty()){
			try {
				return this.checkIns.dequeue();
			} catch (EmptyQueueException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * Applies the changes contained in a particular checkIn and adds
	 * it to the repository if the requesting user is the administrator.
	 * Also saves a copy of changed repository in the versionRecords.
	 * @param requestingUser The user requesting the approval.
	 * @param checkIn The checkIn to approve.
	 * @return ACCESS_DENIED if requestingUser is not the admin, SUCCESS 
	 * otherwise.
	 * @throws EmptyStackException 
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ErrorType approveCheckIn(User requestingUser, ChangeSet checkIn) throws EmptyStackException {
		// TODO: Implement this method. The following lines 
		// are just meant for the method to compile. You can 
		// remove or edit it whatever way you like.
		if(requestingUser==null||checkIn==null) throw new IllegalArgumentException();
		if(requestingUser.equals(admin)){
			int changeCount = checkIn.getChangeCount();
			for(int i = 0; i < changeCount; i++){
				Change theChange = checkIn.getNextChange();
				if(theChange!=null){
					Document theDoc = theChange.getDoc();
					Change.Type theType = theChange.getType();
					if(theType.equals(Change.Type.ADD)){
						this.docs.add(theDoc);
					}
					else{
						for(int j = 0; j < docs.size(); j++){
							if(theDoc.getName().equals(docs.get(j).getName())){
								if(theType.equals(Change.Type.DEL)){
									if (docs == null) {
										throw new IllegalArgumentException();
									}
									this.docs.remove(theDoc);
								}
								else if(theType.equals(Change.Type.EDIT)){
									docs.get(j).setContent(theChange.getDoc().getContent());
								}
							}
						}
					}
				}
			}

			this.version++;
			this.versionRecords.push(new RepoCopy(repoName, this.version, this.docs));
			return ErrorType.SUCCESS;
		}
		else return ErrorType.ACCESS_DENIED;
	}

	/**
	 * Reverts the repository to the previous version if present version is
	 * not the oldest version and the requesting user is the administrator.
	 * @param requestingUser The user requesting the revert.
	 * @return ACCESS_DENIED if requestingUser is not the admin, 
	 * NO_OLDER_VERSION if the present version is the oldest version, SUCCESS 
	 * otherwise.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ErrorType revert(User requestingUser) {
		// TODO: Implement this method. The following lines 
		// are just meant for the method to compile. You can 
		// remove or edit it whatever way you like.
		if(requestingUser==null) throw new IllegalArgumentException();
		if(this.version > 0 && requestingUser.equals(admin)){
			try{
				this.versionRecords.pop();
				this.docs.clear();
				for(int i = 0; i < versionRecords.peek().getDocuments().size(); i++){
					this.docs.add(this.versionRecords.peek().getDocuments().get(i));
				}
				this.version--;
			}catch(EmptyStackException e){
			}
		}
		else if (!requestingUser.equals(admin)) return ErrorType.ACCESS_DENIED;
		else if (this.version==0) return ErrorType.NO_OLDER_VERSION;
		return ErrorType.SUCCESS;
	}
}
