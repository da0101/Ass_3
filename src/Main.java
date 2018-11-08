// -------------------------------------------------------
// Assignment_3 Question_1
// Written by: Danil Ulmashev 27033389
// For COMP 248 Section EE – Fall 2018
// --------------------------------------------------------
// --------------------------------------------------------
// A simple Java program to invite a user to input his/her 
// favorite team and generate soccer matches if their team
// is on the list. The program utilizes variety of one 
// dimensional arrays to keep track of the winning teams 
// and two dimensional array to keep track of the averages.
// There is a maximum of 20 tournaments and if ever user's 
// team wins before the end of 20 tournaments, it will be
// announced as the winner and the game will stop with the
// results and goal statistics.
// --------------------------------------------------------
// --------------------------------------------------------

// Importing libraries.
import java.util.Scanner;
import java.util.Random;

public class Main {

	// Declaring global variables 
	private static boolean win = false;
	private static Random dice = new Random();
	private static Scanner in = new Scanner(System.in);
	
	private static int[][] averages = new int[20][15];
	private static int gameCounter = 0;
	
	private static final String[] TEAMS16 = {
			"Uruguay", "Portugal",
			"France", "Argentina",
			"Brazil", "Mexico",
			"Belgium", "Japan",
			"Spain", "Russia",
			"Croatia", "Denmark",
			"Sweden", "Switzerland",
			"Colombia", "England"
	};
	private static final String[] gameProgress = { "ROUND OF 16", "QUARTER-FINALS", "SEMI-FINALS", "FINAL" };

	// main method.
	public static void main(String[] args) {
		// Calling method printMessageWelcome()
		printMessageWelcome();
		//Calling method start() 
		start();
	}
	
	// Printing the welcome message.
	private static void printMessageWelcome() {
		System.out.println("-------****-------****-------****-------****-----****-----\n"
				         + "|  Welcome to the Soccer Tournaments generator program!  |\n"
				         + "-------****-------****-------****-------****-----****-----\n\n");
	}
	
	// Method start to activate the game.
	private static void start() {
		// Calling method checkFavoriteTeam()
		checkFavoriteTeam(usersFavoriteTeam());
	}
	
	// Allowing user to enter the name of their favorite team.
	private static String usersFavoriteTeam() {
		System.out.print("Enter the name of your Country: ");
		String country = in.nextLine();
		return country;
	}

	// Checking if user's favorite team is in the list.
	private static void checkFavoriteTeam(String country) {
		boolean found = false;
		for (String team : TEAMS16) {
			if (team.equalsIgnoreCase(country)) {
				found = true;
				country = team;
			}
		}
		// Starting the tournaments 
		if (found) {
			System.out.println("\nTeam " + country + " is in the round of 16\n");
			int tournaments = 1;
			while (tournaments <= 20 && !win) {
				gameCounter = 0;
				// Calling method generateMatches()
				generateMatches(TEAMS16, tournaments, country);
				tournaments++;
			}
			// Calling method printGoalStats()
			printGoalStats(tournaments - 1);
		}
		else {
			System.out.println("\nYour team is not in the round of 16\n");
			// Restarting the game if user's country is not in the list.
			start();
		}
	}
	
	// Generating matches 
	private static void generateMatches(String[] teams, int tournaments, String usersFavoritTeam) {
		for (String game : gameProgress) {
			System.out.println("\n" + game);
			teams = playGames(teams, tournaments, usersFavoritTeam);
		}
		// Calling method printWinner()
		printWinner(teams[0], tournaments, usersFavoritTeam);
	}
	
	// Playing games between teams 
	private static String[] playGames(String[] teams, int tournaments, String usersFavoritTeam) {
		String[] newTeams = new String[teams.length / 2];
		for (int i = 0; i < teams.length; i += 2) {
			int j = i / 2;
			// Generating random numbers for each team to record goals 
			int leftSide = dice.nextInt(5);
			int rightSide = dice.nextInt(5);
			System.out.print("[ " + teams[i] + " " + leftSide + ":" + rightSide + " " + teams[i + 1] + " ] ");
			// Checking which team won.
			if (leftSide > rightSide) {
				newTeams[j] = teams[i];
			}
			else if (rightSide > leftSide) {
				newTeams[j] = teams[i + 1];
			}
			else {
				// Adding additional an other round if the first game was a draw.
				System.out.println("Additional 30 minutes");
				leftSide = dice.nextInt(5);
				rightSide = dice.nextInt(5);
				System.out.print("[ " + teams[i] + " " + leftSide + ":" + rightSide + " " + teams[i + 1] + " ] ");
				if (leftSide > rightSide) {
					newTeams[j] = teams[i];
				}
				else if (rightSide > leftSide) {
					newTeams[j] = teams[i + 1];
				}
				else {
					newTeams[j] = teams[i + dice.nextInt(1)];
				}
			}
			// Recording averages of goals 
			averages[tournaments-1][gameCounter++] = leftSide + rightSide;
			System.out.println(newTeams[j] + " won");
		}
		// New teams that won matches.
		return newTeams;
	}
	
	// Printing the winners and checking if user's favorite team won.
	private static void printWinner(String winner, int tournaments, String usersTeam) {
		System.out.println("=========================\n");
		System.out.println("Tournament: " + (tournaments) + " The WINNER is " + winner + "\n");
		if (winner.equalsIgnoreCase(usersTeam)) {
			win = true;
		}
		if (tournaments == 20 && !win) {
			System.out.println("Sorry, " + usersTeam + " didn't win in " + (tournaments ));
		}
	}
	
	// Printing goal statistics after each tournament and 
	private static void printGoalStats(int numTurnoments) {
		double averageTournaments = 0;
		for (int i = 0; i < numTurnoments; i++) {
			System.out.print("[Tournament " + (i < 9 && numTurnoments > 9 ? " " : "") + (i + 1) + "] Total goals: [");
			int[] gameAverages = averages[i];
			double total = 0;
			for (int j = 0; j < gameAverages.length; j++) {
				total += gameAverages[j];
				System.out.print(gameAverages[j]);
				if (j < gameAverages.length - 1) {
					System.out.print(", ");	
				}
			}
			double average = Math.floor((total / gameAverages.length) * 10) / 10;
			averageTournaments += average;
			System.out.print("] [Average: " + average + "]\n");
		}
		double overallAverage = Math.floor((averageTournaments / numTurnoments) * 10) / 10;
		System.out.println("Average goals for " + numTurnoments + " tournament(s): " + overallAverage);
		
		int overAverageCounter = 0;
		for (int i = 0; i < numTurnoments; i++) {
			int[] gameAverages = averages[i];
			for (int j = 0; j < gameAverages.length; j++) {
				if (gameAverages[j] > overallAverage) {
					overAverageCounter++;
				} 
			}
		}
		System.out.println("Total matches in all tournaments over the average goal value: " + overAverageCounter);
	}
}
