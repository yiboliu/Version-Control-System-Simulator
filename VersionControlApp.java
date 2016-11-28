import java.util.Scanner;

/**
 * Version control application. Implements the command line utility
 * for Version control.
 * @author
 *
 */
public class VersionControlApp {

	/* Scanner object on input stream. */
	private static final Scanner scnr = new Scanner(System.in);

	/**
	 * An enumeration of all possible commands for Version control system.
	 */
	private enum Cmd {
		AU, DU,	LI, QU, AR, DR, OR, LR, LO, SU, CO, CI, RC, VH, RE, LD, AD,
		ED, DD, VD, HE, UN
	}

	/**
	 * Displays the main menu help. 
	 */
	private static void displayMainMenu() {
		System.out.println("\t Main Menu Help \n" 
				+ "====================================\n"
				+ "au <username> : Registers as a new user \n"
				+ "du <username> : De-registers a existing user \n"
				+ "li <username> : To login \n"
				+ "qu : To exit \n"
				+"====================================\n");
	}

	/**
	 * Displays the user menu help. 
	 */
	private static void displayUserMenu() {
		System.out.println("\t User Menu Help \n" 
				+ "====================================\n"
				+ "ar <reponame> : To add a new repo \n"
				+ "dr <reponame> : To delete a repo \n"
				+ "or <reponame> : To open repo \n"
				+ "lr : To list repo \n"
				+ "lo : To logout \n"
				+ "====================================\n");
	}

	/**
	 * Displays the repo menu help. 
	 */
	private static void displayRepoMenu() {
		System.out.println("\t Repo Menu Help \n" 
				+ "====================================\n"
				+ "su <username> : To subcribe users to repo \n"
				+ "ci: To check in changes \n"
				+ "co: To check out changes \n"
				+ "rc: To review change \n"
				+ "vh: To get revision history \n"
				+ "re: To revert to previous version \n"
				+ "ld : To list documents \n"
				+ "ed <docname>: To edit doc \n"
				+ "ad <docname>: To add doc \n"
				+ "dd <docname>: To delete doc \n"
				+ "vd <docname>: To view doc \n"
				+ "qu : To quit \n" 
				+ "====================================\n");
	}

	/**
	 * Displays the user prompt for command.  
	 * @param prompt The prompt to be displayed.
	 * @return The user entered command (Max: 2 words).
	 */
	private static String[] prompt(String prompt) {
		System.out.print(prompt);
		String line = scnr.nextLine();
		String []words = line.trim().split(" ", 2);
		return words;
	}

	/**
	 * Displays the prompt for file content.  
	 * @param prompt The prompt to be displayed.
	 * @return The user entered content.
	 */
	private static String promptFileContent(String prompt) {
		System.out.println(prompt);
		String line = null;
		String content = "";
		while (!(line = scnr.nextLine()).equals("q")) {
			content += line + "\n";
		}
		return content;
	}

	/**
	 * Validates if the input has exactly 2 elements. 
	 * @param input The user input.
	 * @return True, if the input is valid, false otherwise.
	 */
	private static boolean validateInput2(String[] input) {
		if (input.length != 2) {
			System.out.println(ErrorType.UNKNOWN_COMMAND);
			return false;
		}
		return true;
	}

	/**
	 * Validates if the input has exactly 1 element. 
	 * @param input The user input.
	 * @return True, if the input is valid, false otherwise.
	 */
	private static boolean validateInput1(String[] input) {
		if (input.length != 1) {
			System.out.println(ErrorType.UNKNOWN_COMMAND);
			return false;
		}
		return true;
	}

	/**
	 * Returns the Cmd equivalent for a string command. 
	 * @param strCmd The string command.
	 * @return The Cmd equivalent.
	 */
	private static Cmd stringToCmd(String strCmd) {
		try {
			return Cmd.valueOf(strCmd.toUpperCase().trim());
		}
		catch (IllegalArgumentException e){
			return Cmd.UN;
		}
	}

	/**
	 * Handles add user. Checks if a user with name "username" already exists; 
	 * if exists the user is not registered. 
	 * @param username The user name.
	 * @return USER_ALREADY_EXISTS if the user already exists, SUCCESS otherwise.
	 */
	private static ErrorType handleAddUser(String username) {
		if (VersionControlDb.addUser(username) != null) {
			return ErrorType.SUCCESS;
		}
		else {
			return ErrorType.USERNAME_ALREADY_EXISTS;
		}
	}

	/**
	 * Handles delete user. Checks if a user with name "username" exists; if 
	 * does not exist nothing is done. 
	 * @param username The user name.
	 * @return USER_NOT_FOUND if the user does not exists, SUCCESS otherwise.
	 */
	private static ErrorType handleDelUser(String username) {
		User user = VersionControlDb.findUser(username); 
		if (user == null) {
			return ErrorType.USER_NOT_FOUND;
		}
		else {
			VersionControlDb.delUser(user);
			return ErrorType.SUCCESS;
		}
	}

	/**
	 * Handles a user login. Checks if a user with name "username" exists; 
	 * if does not exist nothing is done; else the user is taken to the 
	 * user menu. 
	 * @param username The user name.
	 * @return USER_NOT_FOUND if the user does not exists, SUCCESS otherwise.
	 * @throws EmptyStackException 
	 */
	private static ErrorType handleLogin(String username) throws EmptyStackException {
		User currUser = VersionControlDb.findUser(username);
		if (currUser != null) {
			System.out.println(ErrorType.SUCCESS);
			processUserMenu(currUser);
			return ErrorType.SUCCESS;
		}
		else {
			return ErrorType.USER_NOT_FOUND;
		}
	}

	private static String approveOrNot(){
		String answer = scnr.nextLine();
		return answer;
	}

	/**
	 * Processes the main menu commands.
	 * @throws EmptyStackException 
	 * 
	 */
	public static void processMainMenu() throws EmptyStackException {

		String mainPrompt = "[anon@root]: ";
		boolean execute = true;

		while (execute) {
			String[] words = prompt(mainPrompt);
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			case AU:
				if (validateInput2(words)) {
					System.out.println(handleAddUser(words[1].trim()));
				}
				break;
			case DU:
				if (validateInput2(words)) {
					System.out.println(handleDelUser(words[1].trim())); 
				}
				break;
			case LI:
				if (validateInput2(words)) {
					System.out.println(handleLogin(words[1].trim()));
				}
				break;
			case HE:
				if (validateInput1(words)) {
					displayMainMenu();
				}
				break;
			case QU:
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	/**
	 * Processes the user menu commands for a logged in user.
	 * @param logInUser The logged in user.
	 * @throws EmptyStackException 
	 * @throws IllegalArgumentException in case any argument is null.
	 */
	public static void processUserMenu(User logInUser) throws EmptyStackException {

		if (logInUser == null) {
			throw new IllegalArgumentException();
		}

		String userPrompt = "[" + logInUser.getName() + "@root" + "]: ";
		boolean execute = true;

		while (execute) {

			String[] words = prompt(userPrompt);
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			case AR:
				if (validateInput2(words)) {
					// TODO: Implement logic to handle AR.
					String repoName = words[1];
					if(VersionControlDb.findRepo(repoName)!=null) System.out.println(ErrorType.REPONAME_ALREADY_EXISTS);
					else {
						logInUser.subscribeRepo(repoName);
						VersionControlDb.addRepo(repoName, logInUser);
						System.out.println(ErrorType.SUCCESS);
					}
				}
				break;
			case DR:
				if (validateInput2(words)) {
					// TODO: Implement logic to handle DR.
					String repoName = words[1];
					Repo theRepo = VersionControlDb.findRepo(repoName);
					if(theRepo==null) System.out.println(ErrorType.REPO_NOT_FOUND);
					else{
						if(theRepo.getAdmin().equals(logInUser)){
							VersionControlDb.delRepo(theRepo);
							System.out.println(ErrorType.SUCCESS);
						}
						else System.out.println(ErrorType.ACCESS_DENIED);
					}
				}
				break;
			case LR:
				if (validateInput1(words)) {
					// TODO: Implement logic to handle LR.
					System.out.println(logInUser.toString());
				}
				break;
			case OR:
				if (validateInput2(words)) {
					// TODO: Implement logic to handle OR.
					String repoName = words[1];
					Repo open = VersionControlDb.findRepo(repoName);
					if(open==null) System.out.println(ErrorType.REPO_NOT_FOUND);
					else if(logInUser.getWorkingCopy(repoName)==null){
						ErrorType checkOut = logInUser.checkOut(repoName);
						System.out.println(checkOut);
						if(checkOut.equals(ErrorType.SUCCESS)){
							processRepoMenu(logInUser, repoName);
							System.out.println(ErrorType.SUCCESS);
						}
					}
					else{
						System.out.println(ErrorType.SUCCESS);
						processRepoMenu(logInUser, repoName);
						System.out.println(ErrorType.SUCCESS);
					}
				}
				break;
			case LO:
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			case HE:
				if (validateInput1(words)) {
					displayUserMenu();
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	/**
	 * Process the repo menu commands for a logged in user and current
	 * working repository.
	 * @param logInUser The logged in user. 
	 * @param currRepo The current working repo.
	 * @throws EmptyStackException 
	 * @throws IllegalArgumentException in case any argument is null.
	 */
	public static void processRepoMenu(User logInUser, String currRepo) throws EmptyStackException {

		if (logInUser  == null || currRepo == null) {
			throw new IllegalArgumentException();
		}

		String repoPrompt = "["+ logInUser.getName() + "@" + currRepo + "]: ";
		boolean execute = true;

		while (execute) {

			String[] words = prompt(repoPrompt);
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			case SU:
				if (validateInput2(words)) {
					// TODO: Implement logic to handle SU.
					Repo theRepo = VersionControlDb.findRepo(currRepo);
					if(theRepo.getAdmin().equals(logInUser)){
						String userName = words[1];
						User theUser = VersionControlDb.findUser(userName);
						if(theUser==null) System.out.println(ErrorType.USER_NOT_FOUND);
						else{
							theUser.subscribeRepo(currRepo);
							System.out.println(ErrorType.SUCCESS);
						}
					}
					else System.out.println(ErrorType.ACCESS_DENIED);
				}
				break;
			case LD:
				if (validateInput1(words)) {
					// TODO: Implement logic to handle LD.
					System.out.println(logInUser.getWorkingCopy(currRepo).toString());
				}
				break;
			case ED:
				if (validateInput2(words)) {
					// TODO: Implement logic to handle ED.
					String docName = words[1];
					Document theDoc = logInUser.getWorkingCopy(currRepo).getDoc(docName);
					if(theDoc==null) System.out.println(ErrorType.DOC_NOT_FOUND);
					else{
						theDoc.setContent(promptFileContent("Enter the file content and press q to quit: "));
						logInUser.addToPendingCheckIn(theDoc, Change.Type.EDIT, currRepo);
						System.out.println(ErrorType.SUCCESS);
					}
				}					
				break;
			case AD:
				if (validateInput2(words)) {
					// TODO: Implement logic to handle AD.
					String docName = words[1];
					if(logInUser.getWorkingCopy(currRepo).getDoc(docName)!=null) System.out.println(ErrorType.DOCNAME_ALREADY_EXISTS);
					else{
						Document theDoc = new Document(docName, promptFileContent("Enter the file content and press q to quit: "), currRepo);
						if(logInUser.getWorkingCopy(currRepo).addDoc(theDoc)){
							logInUser.addToPendingCheckIn(theDoc, Change.Type.ADD, currRepo);
							System.out.println(ErrorType.SUCCESS);
						}
					}
				}
				break;
			case DD:
				if (validateInput2(words)) {
					// TODO: Implement logic to handle DD.
					String docName = words[1];
					RepoCopy theWorkingCopy = logInUser.getWorkingCopy(currRepo);
					if(theWorkingCopy.getDoc(docName)==null) System.out.println(ErrorType.DOC_NOT_FOUND);
					else{
						Document theDoc = theWorkingCopy.getDoc(docName);
						theWorkingCopy.delDoc(theDoc);
						logInUser.addToPendingCheckIn(theDoc, Change.Type.DEL, currRepo);
						System.out.println(ErrorType.SUCCESS);
					}
				}
				break;
			case VD:
				if (validateInput2(words)) {
					// TODO: Implement logic to handle VD.
					String docName = words[1];
					RepoCopy theWorkingCopy = logInUser.getWorkingCopy(currRepo);
					if(theWorkingCopy.getDoc(docName)==null) System.out.println(ErrorType.DOC_NOT_FOUND);
					else System.out.println(theWorkingCopy.getDoc(docName).toString());
				}
				break;
			case CI:
				if (validateInput1(words)) {
					// TODO: Implement logic to handle CI.
					System.out.println(logInUser.checkIn(currRepo));
				}
				break;
			case CO:
				if (validateInput1(words)) {
					// TODO: Implement logic to handle CO.
					System.out.println(logInUser.checkOut(currRepo));
				}
				break;
			case RC:
				if (validateInput1(words)) {
					// TODO: Implement logic to handle RC.
					if(VersionControlDb.findRepo(currRepo).getCheckInCount()==0){
						System.out.println(ErrorType.NO_PENDING_CHECKINS);
						break;
					}
					ChangeSet display = VersionControlDb.findRepo(currRepo).getNextCheckIn(logInUser);
					if(!VersionControlDb.findRepo(currRepo).getAdmin().equals(logInUser)) System.out.println(ErrorType.ACCESS_DENIED);
					else{
						System.out.println(display.toString());
						System.out.print("Approve changes? Press y to accept: ");
						String answer = approveOrNot();
						if(answer.equals("y")) System.out.println(VersionControlDb.findRepo(currRepo).approveCheckIn(logInUser, display));
					}
				}
				break;
			case VH:
				if (validateInput1(words)) {
					// TODO: Implement logic to handle VH.
					System.out.println(VersionControlDb.findRepo(currRepo).getVersionHistory().toString());
				}
				break;
			case RE:	
				if (validateInput1(words)) {
					// TODO: Implement logic to handle RE.
					System.out.println(VersionControlDb.findRepo(currRepo).revert(logInUser));
				}
				break;
			case HE:
				if (validateInput1(words)) {
					displayRepoMenu();
				}
				break;
			case QU:
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	/**
	 * The main method. Simulation starts here.
	 * @param args Unused
	 * @throws EmptyStackException 
	 */
	public static void main(String []args) throws EmptyStackException {
		try {
			processMainMenu(); 
		}
		// Any exception thrown by the simulation is caught here.
		catch (Exception e) {
			System.out.println(ErrorType.INTERNAL_ERROR);
			// Uncomment this to print the stack trace for debugging purpose.
			e.printStackTrace();
		}
		// Any clean up code goes here.
		finally {
			System.out.println("Quitting the simulation.");
		}
	}
}
