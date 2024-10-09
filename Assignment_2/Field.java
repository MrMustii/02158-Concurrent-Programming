//Prototype implementation of Field class
//Mandatory assignment 2
//Course 02158 Concurrent Programming, DTU,  Fall 2023

//Hans Henrik Lovengreen     Sep 26, 2023

public class Field {
    Semaphore [][] arr;
    public Field() {
    arr= new Semaphore [11][12];
    for (int row = 0; row < arr.length; row++) {
        for (int col = 0; col < arr[row].length; col++) {
            arr[row][col]= new Semaphore(1);
        }
        // need to set to 0 for where the cars start
    }
    }

    /* Block until car no. may safely enter tile at pos */
    public void enter(int no, Pos pos) throws InterruptedException {
    arr[pos.row][pos.col].P();
    }

    /* Release tile at position pos */
    public void leave(Pos pos) {
    arr[pos.row][pos.col].V();
    }

}