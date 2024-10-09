//Prototype implementation of CarTest class
//Mandatory assignment 3
//Course 02158 Concurrent Programming, DTU, Fall 2023

//Hans Henrik Lovengreen     Oct 26, 2023


public class CarTest extends Thread {

    CarTestingI cars;
    int testno;

    public CarTest(CarTestingI ct, int no) {
        cars = ct;
        testno = no;
    }

    public void run() {
        try {
            switch (testno) { 
            case 0:
                // Demonstration of startAll/stopAll.
                // Should let the cars go one round (unless very fast)
                cars.startAll();
                sleep(3000);
                cars.stopAll();
                break;
            case 1:
                // Demonstration of startAll/stopAll.
                // Should let the cars go one round (unless very fast)
                cars.startAll();
                cars.removeCar(1);
                cars.restoreCar(1);
                cars.removeCar(1);
                cars.restoreCar(1);
                cars.removeCar(1);
                cars.restoreCar(1);
                cars.removeCar(1);
                cars.restoreCar(1);
                sleep(3000);
                cars.stopAll();
                cars.println("case");
                break;
            case 2:
                // Demonstration of startAll/stopAll.
                // Should let the cars go one round (unless very fast)
                cars.startAll();
                for(int i=0; i<=10; i++){
                    sleep(100);
                    cars.restoreCar(1);
                    cars.removeCar(1);
                }
                    cars.restoreCar(1);
                sleep(3000);
                cars.stopAll();
                cars.println("case");
                break;
                case 3:
                    // Demonstration of startAll/stopAll.
                    // Should let the cars go one round (unless very fast)
                    cars.startAll();
                    cars.restoreCar(1);
                    cars.removeCar(1);
                    cars.restoreCar(1);
                    cars.removeCar(1);
                    cars.restoreCar(1);
                    cars.removeCar(1);
                    cars.restoreCar(1);
                    sleep(3000);
                    cars.stopAll();
                    cars.println("case");
                    break;
            case 4:
                cars.startAll();
                cars.removeCar(1);
                cars.restoreCar(1);
                sleep(3000);
                cars.stopAll();
                cars.println("case");
            case 5:
                cars.startAll();
                cars.restoreCar(1);
                cars.removeCar(1);
                sleep(3000);
                cars.stopAll();
                cars.println("case");
                case 19:
                // Demonstration of speed setting.
                // Will result in jumpy movement though.
                cars.println("Setting high speeds");
                for (int i = 1; i < 9; i++) {
                    cars.setSpeed(i,20.0);
                    cars.setVariation(i,20);
                };
                break;

            default:
                cars.println("Test " + testno + " not available");
            }

            cars.println("Test ended");

        } catch (Exception e) {
            System.err.println("Exception in test: "+e);
        }
    }

}



