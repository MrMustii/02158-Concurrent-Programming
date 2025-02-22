//Implementation of CarControl class
//Mandatory assignment 3
//Course 02158 Concurrent Programming, DTU, Fall 2023

//Hans Henrik Lovengreen     Oct 26, 2023


import java.awt.Color;

class Conductor extends Thread {

    double basespeed = 7.0;          // Tiles per second
    double variation = 40;          // Percentage of base speed

    CarDisplayI cd;                  // GUI part

    Field field;                     // Field control
    Alley alley;                     // Alley control    
    Barrier barrier;                 // Barrier control    

    int no;                          // Car number
    Pos startpos;                    // Start position (provided by GUI)
    Pos barpos;                      // Barrier position (provided by GUI)
    Color col;                       // Car  color
    Gate mygate;                     // Gate at start position

    Pos curpos;                      // Current position 
    Pos newpos;                      // New position to go to
    volatile boolean removed = false;         ////////////Now volatile // NEW attribute TO CHECK IF A CAR IS REMOVED
    boolean nextpos = false;        // NEW attribute IF TRUE THE CAR OCCUPIES 2 POSITIONS
    boolean inAlly = false;         // NEW attribute IF TRUE THE CAR IS IN THE ALLY
    boolean initialize =false;

    public Conductor(int no, CarDisplayI cd, Gate g, Field field, Alley alley, Barrier barrier) {

        this.no = no;
        this.cd = cd;
        this.field = field;
        this.alley = alley;
        this.barrier = barrier;
        mygate = g;
        startpos = cd.getStartPos(no);
        barpos = cd.getBarrierPos(no);  // For later use

        col = chooseColor();

        // special settings for car no. 0
        if (no == 0) {
            basespeed = -1.0;
            variation = 0;
        }
    }

    public synchronized void setSpeed(double speed) {
        basespeed = speed;
    }

    public synchronized void setVariation(int var) {
        if (no != 0 && 0 <= var && var <= 100) {
            variation = var;
        } else
            cd.println("Illegal variation settings");
    }

    synchronized double chooseSpeed() {
        double factor = (1.0D + (Math.random() - 0.5D) * 2 * variation / 100);
        return factor * basespeed;
    }

    Color chooseColor() {
        return Color.blue; // You can get any color, as longs as it's blue 
    }

    Pos nextPos(Pos pos) {
        // Get my track from display
        return cd.nextPos(no, pos);
    }

    boolean atGate(Pos pos) {
        return pos.equals(startpos);
    }

    boolean atEntry(Pos pos) {
        return (pos.row == 1 && pos.col == 1) || (pos.row == 2 && pos.col == 1) ||
                (pos.row == 10 && pos.col == 0);
    }

    boolean atExit(Pos pos) {
        return (pos.row == 0 && pos.col == 0) || (pos.row == 9 && pos.col == 1);
    }

    boolean atBarrier(Pos pos) {
        return pos.equals(barpos);
    }

    public void run() {
        try {
            CarI car = cd.newCar(no, col, startpos);
            curpos = startpos;
            field.enter(no, curpos);// do not care
            initialize=true;        // NEW  car now entered the start
            cd.register(car);

            while (true) {

                try {                               // CHECKS IF A THREAD IS INTERRUPTED

                    if (atGate(curpos)) {
                        mygate.pass();
                        car.setSpeed(chooseSpeed());
                    }

                    newpos = nextPos(curpos);

                    if (atBarrier(curpos)) barrier.sync(no);

                    if (atEntry(curpos)) {
                        alley.enter(no);
                        inAlly = true;      //SET TO TRUE IF THE CAR IS IN THE ALLY// THIS IS THE CORRECTION

                    }

                    field.enter(no, newpos);
                    nextpos = true;             // SET TO TRUE SINCE A CAR IS IN 2 FIELDS
                    car.driveTo(newpos);

                    field.leave(curpos);
                    nextpos = false;            // CAR GAVE UP THE OLD FIELD
                    if (atExit(newpos)) {
                        inAlly = false;         //CAR LEFT THE ALLY
                        alley.leave(no);
                    }

                    curpos = newpos;
                    //NEW ADDED HERE : to fix a car that does not run into a thread blocking statement  
                    //

                    if (removed) {
                        cd.deregister(car);             //////////NOW UP HERE    // REMOVE CAR
                        field.leave(curpos);            //RETURN THE RESOURCES OF THE FIELD IT IS ON
                        if (inAlly) alley.leave(no);    //IF IT IS IN ALLY, THEN LEAVE IT
                        if (nextpos) {
                            field.leave(newpos);        // IF OCCUPYING 2 POSITIONS THEN LEAVE THE SECOND
                        }
                    }
                    //
                    //

                } catch (InterruptedException e) {
                    if (removed) {                      //CHECK IF A CAR HAS BEEN REMOVED
                        cd.deregister(car);             //////////NOW UP HERE    // REMOVE CAR
                        field.leave(curpos);            //RETURN THE RESOURCES OF THE FIELD IT IS ON
                        if (inAlly) alley.leave(no);    //IF IT IS IN ALLY, THEN LEAVE IT
                        if (nextpos) {
                            field.leave(newpos);        // IF OCCUPYING 2 POSITIONS THEN LEAVE THE SECOND
                        }

                        break;
                    }

                }
            }

        } catch (InterruptedException e) {
            if (!removed) removed=true;
            if (initialize) field.leave(startpos);//leave the start position if the car is immediately removed
        } catch (Exception e) {
            cd.println("Exception in Conductor no. " + no);
            System.err.println("Exception in Conductor no. " + no + ":" + e);
            e.printStackTrace();
        }

    }
}

public class CarControl implements CarControlI{

    CarDisplayI cd;           // Reference to GUI
    Conductor[] conductor;    // Car controllers
    Gate[] gate;              // Gates
    Field field;              // Field
    Alley alley;              // Alley
    Barrier barrier;          // Barrier

    public CarControl(CarDisplayI cd) {
        this.cd = cd;
        conductor = new Conductor[9];
        gate = new Gate[9];
        field = new Field();
        alley = Alley.create();
        barrier = Barrier.create(cd);

        for (int no = 0; no < 9; no++) {
            gate[no] = Gate.create();
            conductor[no] = new Conductor(no,cd,gate[no],field,alley,barrier);
            conductor[no].setName("Conductor-" + no);
            conductor[no].start();
        } 
    }
    public void startCar(int no) {
        gate[no].open();
    }

    public void stopCar(int no) {
        gate[no].close();
    }

    public void barrierOn() { 
        barrier.on();
    }

    public void barrierOff() {
        barrier.off();
    }

   public void barrierSet(int k) {
        barrier.set(k);
   }
    
    public void removeCar(int no) {
        cd.println("removing");
            conductor[no].removed = true;
            conductor[no].interrupt();
        }



    public void restoreCar(int no) {
        if (conductor[no].removed) {
            cd.println("restoring");
            conductor[no] = new Conductor(no, cd, gate[no], field, alley, barrier);
            conductor[no].setName("Conductor-" + no);
            conductor[no].start();
        }

    }

    /* Speed settings for testing purposes */

    public void setSpeed(int no, double speed) { 
        conductor[no].setSpeed(speed);
    }

    public void setVariation(int no, int var) { 
        conductor[no].setVariation(var);
    }
}






