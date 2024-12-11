package main;

/*
Runs the entire game from beginning to end
 */
import exceptions.*;
import leaderboards.Player;
import leaderboards.PlayerDataHandler;
import puzzles.*;
import database.PlayerDAO;
import leaderboards.Leaderboard;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RunGame {

    public static int numProblems;
    public static String difficulty;
    public static ExecutorService executorService;

    public static void main(String[] args) throws NullPuzzleException, InvalidPuzzleException, ExecutionException, InterruptedException {
        // Postcondition 1 (Game): Game updates and text appear on the console as the user continues playing
        // Postcondition 2 (Save): User response to outputting game stats to an external file, with text
        // confirmation of the player's choice on the console
        executorService = Executors.newCachedThreadPool();
        Leaderboard leaderboard = new Leaderboard();
        Future<ArrayList<Player>> playerFuture = executorService.submit(leaderboard::getLeaderboardFromTable);
        numProblems = chooseDifficulty();
        Future<ArrayList<Puzzle>> puzzleFuture = executorService.submit(() ->
                PuzzleDataHandler.loadPuzzlesFromTable(numProblems));
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a name:");
        String playerName = scanner.nextLine();
        gameMenu(playerName, puzzleFuture, playerFuture);
        executorService.shutdown();
    }

    public static void gameMenu(String name, Future<ArrayList<Puzzle>> puzzleFuture,
                                Future<ArrayList<Player>> playerFuture)
            throws NullPuzzleException, InvalidPuzzleException, ExecutionException, InterruptedException {
        /*
        Intent: Provides user with various menu options to choose from.
        Example: String "Toby"
        Precondition: String name is not null, Future objects are valid
         */
        Scanner scanner = new Scanner(System.in);
        String response;

        while (true) {
            System.out.println("Welcome to PuzzCape! In order to escape the room, solve puzzles!");
            System.out.println("To begin the game, type \"start\" or \"s\".");
            System.out.println("To view the leaderboard, type \"leader\" or \"l\".");
            System.out.println("To exit, type \"quit\" or \"q\".");
            response = scanner.nextLine();
            if (response.equalsIgnoreCase("start") || response.equalsIgnoreCase("s")) {
                ArrayList<Puzzle> puzzles = puzzleFuture.get();
                runGame(name, puzzles);
                break;
            }
            else if (response.equalsIgnoreCase("leader") || response.equalsIgnoreCase("l")) {
                ArrayList<Player> players = playerFuture.get();
                Leaderboard leaderboard = new Leaderboard();
                leaderboard.displayLeaderboard(players);
            }
            else if (response.equalsIgnoreCase("quit") || response.equalsIgnoreCase("q")) {
                break;
            }
            else {
                System.out.println("Invalid command.");
            }
        }
    }

    public static void runGame(String name, ArrayList<Puzzle> thePuzzles) throws NullPuzzleException, InvalidPuzzleException {
        /*
        Intent: Starts the current game.
        Example: thePuzzles = [Riddle riddle1, OddOneOut oddOneOut1, Sudoku sudoku1], playerName = "Toby",
        return value = "Toby"
        Precondition 1: Player name is not null
        Precondition 2: length of thePuzzles is greater than 0
        Precondition 3: all Puzzle elements types in thePuzzles are instantiated by a valid subclass
        Postcondition: Runs the game, with return value = new Player instance containing the player's stats when
         they are finished playing
         */
        if (thePuzzles.isEmpty()) {
            throw new NullPuzzleException();
        }
        int puzzlesSolved = 0;
        int currScore = 0;
        String response;

        // Instantiates the PuzzleSolver generic class for each type of Puzzle
        PuzzleSolver<Object[][]> sudokuSolver = new PuzzleSolver<>();
        PuzzleSolver<String[]> oddOneOutSolver = new PuzzleSolver<>();
        PuzzleSolver<String> riddleSolver = new PuzzleSolver<>();
        PuzzleSolver<String[][]> wrdSearchSolver = new PuzzleSolver<>();

        Scanner scanner = new Scanner(System.in);
        Instant start = Instant.now();
        System.out.println("Score: "+ currScore);
        while (puzzlesSolved < thePuzzles.size()) {
            int counter = 0;
            for (Puzzle puzzle : thePuzzles) {
                counter++;
                if (puzzle == null) {
                    throw new NullPuzzleException();
                }
                else if (puzzle.getType().equalsIgnoreCase("Riddle") ||
                        puzzle.getType().equalsIgnoreCase("OddOneOut") ||
                        puzzle.getType().equalsIgnoreCase("Sudoku") ||
                        puzzle.getType().equalsIgnoreCase("WordSearch")) {
                    System.out.println("Puzzle "+ counter+": Type - "+puzzle.getType()+
                            " Solved: "+puzzle.isCompleted());
                }
                else  {
                    throw new InvalidPuzzleException(puzzle.getType());
                }

            }
            System.out.println("Which puzzle would you like to solve? ");
            response = scanner.nextLine().toLowerCase();
            try {
                int choice = Integer.parseInt(response);
                Puzzle currPuzzle = thePuzzles.get(choice-1);
                if (currPuzzle.isCompleted()) {
                    System.out.println("Puzzle already solved.");
                    continue;
                }
                String guess = "";
                while (!guess.equals(currPuzzle.getAnswer().toLowerCase())) {
                    System.out.println("Score: "+ currScore);
                    System.out.println();
                    switch (currPuzzle) {
                        case Riddle riddle -> riddleSolver.displayPuzzle(riddle);
                        case OddOneOut oddOneOut -> oddOneOutSolver.displayPuzzle(oddOneOut);
                        case Sudoku sudoku -> sudokuSolver.displayPuzzle(sudoku);
                        case WordSearch wordSearch -> wrdSearchSolver.displayPuzzle(wordSearch);
                        default -> {
                        }
                    }
                    System.out.println("Your answer: ");
                    guess = scanner.nextLine().toLowerCase();
                    if (guess.equalsIgnoreCase("back") || guess.equalsIgnoreCase("b")) {
                        break;
                    } else if (guess.equalsIgnoreCase("hint") || guess.equalsIgnoreCase("h")) {
                        System.out.println(currPuzzle.getHint());
                    } else if (guess.equalsIgnoreCase(currPuzzle.getAnswer())) {
                        System.out.println("Correct! You solved this puzzle!");
                        System.out.println("+1000");
                        puzzlesSolved++;
                        currScore += 1000;
                        currPuzzle.setCompleted();
                        break;
                    }
                    else {
                        System.out.println("Sorry, that answer is incorrect. Try again!");
                        System.out.println("-50");
                        currScore-= 50;
                    }
                }

            } catch (NumberFormatException e) {
                if (response.equalsIgnoreCase("quit") || response.equalsIgnoreCase("q")) {
                    break;
                } else {
                    System.out.println("Not a valid number/command. Please re-enter.");
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Not a valid number. Please re-enter.");
            }

        }
        Instant finish = Instant.now();
        long timeTaken = Duration.between(start,finish).toSeconds();
        if (puzzlesSolved == thePuzzles.size()) {
            System.out.println("Congrats! You solved all the puzzles and escaped! You are now free!");
        } else {
            System.out.println("Thanks for playing!");
        }
        saveInfo(new Player(name, puzzlesSolved, timeTaken, currScore));

    }

    public static void saveInfo(Player player) {
        /*
        Intent: Provides user with the option to save their results to a Leaderboard.
        Example: Player object: playerName = "Toby", puzzlesSolved = 5, timeCompleted = 90 secs
        Precondition 1: Player instance is not null nor are any of its fields null
        Postcondition: If the user chooses so, their game stats will be saved to a Leaderboard file.
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to save your info to the leaderboard? yes (y) or no (n)");
        String response = scanner.nextLine();
        while (true) {
            if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
                System.out.println("Saved successfully!");

                Leaderboard leaderboard = new Leaderboard();
                ArrayList<Player> playerList = leaderboard.getLeaderboardFromBinary("fullLeaderboard.ser");
                leaderboard.updateLeaderboard(playerList, player, "fullLeaderboard.ser");
                PlayerDataHandler.insertToPlayerStatTable(player, difficulty, numProblems);

                break;
            }
            else if (response.equalsIgnoreCase("n") || response.equalsIgnoreCase("no")) {
                System.out.println("Did not save.");
                break;
            }
            else {
                System.out.println("Invalid response. Please try again.");
            }
            response = scanner.nextLine();
        }
    }

    public static int chooseDifficulty() {
        /*
        Intent: Has the user select their difficulty of choice to start the game.
        Postcondition: Returns a specified number of puzzles to solve based on the chosen difficulty
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose your difficulty: (Easy, Medium, Hard)");
        String response;
        while (true) {
            response = scanner.nextLine();
            if (response.equalsIgnoreCase("easy")) {
                difficulty = "Easy";
                return 3;
            }
            else if (response.equalsIgnoreCase("medium")) {
                difficulty = "Medium";
                return 5;
            }
            else if (response.equalsIgnoreCase("hard")) {
                difficulty = "Hard";
                return 7;
            }
            else {
                System.out.println("Invalid difficulty.");
            }
        }
    }
}

