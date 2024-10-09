//Basic implementation of Alley class (skeleton)
//CP Lab 3
//Course 02158 Concurrent Programming, DTU

//Hans Henrik Lovengreen

public class BasicAlley extends Alley {
   private int s=0;
   private  int down=0; // true is up
   private int up=0;
    BasicAlley() {
    }
//1234 down
    @Override
    /* Block until car no. may enter alley */
    public synchronized void enter(int no) throws InterruptedException {
        if (no < 5) {
            if (up == 0) {
                down++;
            } else {
                wait();
                down++;
            }
        } else if (no >= 5) {
            if (down == 0) {
                up++;
            } else {
                wait();
                up++;
            }
        }
    }

        @Override
        /* Register that car no. has left the alley */
        public synchronized void leave(int no){
            if (no < 5) {

                down--;
                if (down == 0) {
                    notify();
                }
            } else if (no >= 5) {

                up--;
                if (up == 0) {
                    notify();
                }
            }


        }
    };
//<5 and down dont do anything
//<5 up
//>5 up
//>5 down dont do anything