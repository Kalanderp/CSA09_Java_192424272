public class ScannerStations {
    static class StationRunnable implements Runnable {
        private final int stationId;
        private final int scripts;
        StationRunnable(int stationId, int scripts) {
            this.stationId = stationId;
            this.scripts = scripts;
        }
        public void run() {
            try {
                Thread.sleep(100L * stationId);
                System.out.println("Station " + stationId + " scanned " + scripts + " scripts");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Station " + stationId + " interrupted");
            }
        }
    }

    static class StationThread extends Thread {
        private final int stationId;
        private final int scripts;
        StationThread(int stationId, int scripts) {
            this.stationId = stationId;
            this.scripts = scripts;
        }
        public void run() {
            try {
                Thread.sleep(100L * stationId);
                System.out.println("Thread station " + stationId + " scanned " + scripts + " scripts");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] stations = new Thread[4];
        int total = 0;
        for (int i = 0; i < 4; i++) {
            stations[i] = new Thread(new StationRunnable(i + 1, 25));
            stations[i].start();
            System.out.println("Started station " + (i + 1) + "; alive=" + stations[i].isAlive());
            total += 25;
        }
        for (Thread station : stations) station.join();
        System.out.println("All stations completed. Total scripts: " + total);
    }
}
