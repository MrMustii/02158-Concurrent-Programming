//Implementation of Alley class with inner alley (skeleton)
//CP Lab 3
//Course 02158 Concurrent Programming, DTU

//Hans Henrik Lovengreen

public class DoubleAlley extends Alley {
   
    DoubleAlley() {
    }

    @Override
    /* Block until car no. may enter alley */
    public void enter(int no) throws InterruptedException {
    }

    @Override
    /* Register that car no. has left the alley */
    public void leave(int no) {
    }
    
    @Override
    /* Register that car no. has left the inner alley */
    public void leaveInner(int no) {
    }

}
