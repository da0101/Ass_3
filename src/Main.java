import java.util.Scanner;

import java.util.Random;

public class Main {

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

	public static void main(String[] args) {
		start();
	}
	
	private static void start() {
		favoriteTeam(usersFavoriteTeam());
	}
	
	private static String usersFavoriteTeam() {
		System.out.print("Enter the name of your Country: ");
		String country = in.nextLine();
		return country;
	}

	private static void favoriteTeam(String country) {
		boolean found = false;
		for (String team : TEAMS16) {
			if (team.equalsIgnoreCase(country)) {
				found = true;
				country = team;
			}
		}
		if (found) {
			System.out.println("\nTeam " + country + " is in the round of 16\n");
			int tournaments = 1;
			while (tournaments <= 20 && !win) {
				gameCounter = 0;
				generateMatches(TEAMS16, tournaments, country);
				tournaments++;
			}
			printGoalStats(tournaments - 1);
		}
		else {
			System.out.println("\nYour team is not in the round of 16\n");
			start();
		}
	}
	
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
	
	
	private static void generateMatches(String[] teams, int tournaments, String usersFavoritTeam) {
		for (String game : gameProgress) {
			System.out.println("\n" + game);
			teams = playGames(teams, tournaments, usersFavoritTeam);
		}
		printWinner(teams[0], tournaments, usersFavoritTeam);
	}
	
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

	
	private static String[] playGames(String[] teams, int tournaments, String usersFavoritTeam) {
		String[] newTeams = new String[teams.length / 2];
		for (int i = 0; i < teams.length; i += 2) {
			int j = i / 2;
			int leftSide = dice.nextInt(5);
			int rightSide = dice.nextInt(5);
			System.out.print("[ " + teams[i] + " " + leftSide + ":" + rightSide + " " + teams[i + 1] + " ] ");
			if (leftSide > rightSide) {
				newTeams[j] = teams[i];
			}
			else if (rightSide > leftSide) {
				newTeams[j] = teams[i + 1];
			}
			else {
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
			averages[tournaments-1][gameCounter++] = leftSide + rightSide;
			System.out.println(newTeams[j] + " won");
		}
		return newTeams;
	}
}
