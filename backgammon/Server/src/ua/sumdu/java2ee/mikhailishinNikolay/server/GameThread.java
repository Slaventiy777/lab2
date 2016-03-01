package ua.sumdu.java2ee.mikhailishinNikolay.server;

public class GameThread extends Thread {
    @Override
    public void run() {
        try {
            //1 sec
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //тут
    }
}
