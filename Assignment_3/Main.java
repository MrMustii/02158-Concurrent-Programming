import java.util.Random;
import java.util.Scanner;

public class Main{

    //Create constants for the board
    private static final int Board = 20;
    private static final int winX = Board - 1;
    private static final int winY = Board - 1;
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private static final int turn = 5;

    //Players starting pos.
    private static int playerX = 0;
    private static int playerY = 0;

    //Boolean array that declares pits.
    private static boolean[][] pits = new boolean[Board][Board];


    public static void main(String[] args) {
        //declare scanner and random class
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.println("Welcome to the Pitfall Game! \n Use N/W/E/S to move");


        //while loop that checks for players input for moving and if they lost or won
        while (true) {
            makeBoard();

            //input from player. can only be ONE move per turn.
            System.out.print("MAKE YOUR MOVE >:)\n");
            String move = scanner.nextLine();

            //how many pits it should place per turn.
            int placePits = random.nextInt(turn) + 1;
            putPits(placePits);

            //If player steps into pit, then lose.
            if (!makeMove(move)) {
                System.out.println("Game over! You stepped on a pit.");
                break;
            }

            //if player reachess 19,19, then they win.
            if (playerX == winX && playerY == winY) {
                System.out.println("Congratulations! You reached the goal. You win!");
                break;
            }

            //If player is surrounded by pitss and cant move, then they lose.
            if (surrPits()) {
                System.out.println("Game over! You are surrounded by pits.");
                break;
            }
        }

        scanner.close();
    }


    //method to create the board. U= player/ O= pit/ .=available cell
    private static void makeBoard() {
        System.out.println("Current position: (" + playerX + ", " + playerY + ")");
        //i=x axis & j=y axis
        for (int i = 0; i < Board; i++) {
            for (int j = 0; j < Board; j++) {
                if (i == playerX && j == playerY) {
                    System.out.print("X ");
                } else if (pits[i][j]) {
                    System.out.print("O ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    //method to place pits.
    private static void putPits(int numPits) {
        Random random = new Random();
        for (int i = 0; i < numPits; i++) {
            int x, y;
            boolean validPlacement;

            // Keep generating new coordinates until a valid placement is found
            while (true) {
                x = random.nextInt(Board);
                y = random.nextInt(Board);

                // Check if the coordinates are valid
                validPlacement = !pits[x][y] && (x != playerX || y != playerY);

                // Exit the loop if a valid placement is found
                if (validPlacement) {
                    break;
                }
            }

            // Place the pit on the board
            pits[x][y] = true;
        }
    }

    //method on how the player moves. The method is responsible for updating the player's position on the game board based on the specified move.
    private static boolean makeMove(String move) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            switch (move.toUpperCase()) {
                case "N":
                    if (playerX > 0) {
                        playerX--;
                        return true;
                    }break;
                case "S":
                    if (playerX < Board - 1) {
                        playerX++;
                        return true;
                    }break;
                case "E":
                    if (playerY < Board - 1) {
                        playerY++;
                        return true;
                    } break;
                case "W":
                    if (playerY > 0) {
                        playerY--;
                        return true;
                    }break;
                default:
                    // If the move is invalid, prompt the user for a new move
                    System.out.print("Invalid move. Try again: ");
                    move = scanner.nextLine();
                    continue;
            }break;
        }return false;
    }

    //Method to check if the player is surrounded with pits and if true then run surrPrint.
    private static boolean surrPits(int playerX, int playerY, int winX, int winY) {
        if (playerX != winX && playerY != winY) {
            return true; // search the destination
        }
    }
}