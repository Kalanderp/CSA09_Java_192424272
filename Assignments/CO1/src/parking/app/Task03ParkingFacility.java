import java.util.Arrays;
public class Task03ParkingFacility {
    private final String[][] slots;
    public Task03ParkingFacility(int[] slotsPerFloor) {
        slots = new String[slotsPerFloor.length][];
        for (int f=0; f<slotsPerFloor.length; f++) slots[f] = new String[slotsPerFloor[f]];
    }
    public int[] allocate(String vehicleNo) {
        for (int f=0; f<slots.length; f++) {
            for (int s=0; s<slots[f].length; s++) {
                if (slots[f][s] != null) continue;
                slots[f][s] = vehicleNo;
                return new int[]{f,s};
            }
        }
        return null;
    }
    public boolean release(int floor, int slot) {
        if (floor < 0 || floor >= slots.length) return false;
        if (slot < 0 || slot >= slots[floor].length) return false;
        if (slots[floor][slot] == null) return false;
        slots[floor][slot] = null;
        return true;
    }
    public static void main(String[] args) {
        Task03ParkingFacility p = new Task03ParkingFacility(new int[]{2,1});
        System.out.println(Arrays.toString(p.allocate("TN01AB1111")));
        System.out.println(Arrays.toString(p.allocate("TN02CD2222")));
        System.out.println(Arrays.toString(p.allocate("TN03EF3333")));
        System.out.println(Arrays.toString(p.allocate("TN04GH4444")));
        System.out.println(p.release(0,1)+" "+p.release(0,1)+" "+p.release(5,0));
        System.out.println(Arrays.toString(p.allocate("TN04GH4444")));
    }
}
