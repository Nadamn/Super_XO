/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author david
 */



public class Board {

    List<Point> availablePoints;
//    Scanner scan = new Scanner(System.in);
    int[][] board =new int[][] {{2,2,2},{2,2,2},{2,2,2}};

    public Board() {
    }

    public boolean isGameOver() {
        return (hasXWon() || hasOWon() || getAvailableStates().isEmpty());
    }

    public boolean hasXWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 1) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 1)) {
            return true;
        }
        for (int i = 0; i < 3; ++i) {
            if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 1)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 1))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 0) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 0)) {
            return true;
        }
        for (int i = 0; i < 3; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 0)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 0)) {
                return true;
            }
        }

        return false;
    }

    public List<Point> getAvailableStates() {
        availablePoints = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == 2) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }
        return availablePoints;
    }

    public void placeAMove(Point point, int player) {
        board[point.x][point.y] = player; //player = 1 for X, 0 for O
    } 

//    void takeHumanInput() {
//        System.out.println("Your move: ");
//        int x = scan.nextInt();
//        int y = scan.nextInt();
//        Point point = new Point(x, y);
//        placeAMove(point, 2); 
//    }

    public void displayBoard() {
        System.out.println();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();

        }
    } 

    Point computersMove; 

    public int minimax(int depth, int turn) { 
        if (hasXWon()) return +1; 
        if (hasOWon()) return -1;

        List<Point> pointsAvailable = getAvailableStates();
        if (pointsAvailable.isEmpty()) return 0; 

        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

        for (int i = 0; i < pointsAvailable.size(); ++i) { 
            Point point = pointsAvailable.get(i); 
            if (turn == 1) { 
                placeAMove(point, 1); 
                int currentScore = minimax(depth + 1, 2);
                max = Math.max(currentScore, max);

                if(depth == 0)System.out.println("Score for position "+(i+1)+" = "+currentScore);
                if(currentScore >= 0){ if(depth == 0) computersMove = point;} 
                if(currentScore == 1){board[point.x][point.y] = 2; break;} 
                if(i == pointsAvailable.size()-1 && max < 0){if(depth == 0)computersMove = point;}
            } else if (turn == 2) {
                placeAMove(point, 0); 
                int currentScore = minimax(depth + 1, 1);
                min = Math.min(currentScore, min); 
                if(min == -1){board[point.x][point.y] = 2; break;}
            }
            board[point.x][point.y] = 2; //Reset this point
        } 
        return turn == 1?max:min;
    } 
    public Point takeMove(int x,int y){
        Point point = new Point(x, y);
        this.placeAMove(point, 1);
        this.minimax(0, 1); //compter's trn  1 = compter 2 = user
        placeAMove(this.computersMove, 0);
      return this.computersMove;
    }
}