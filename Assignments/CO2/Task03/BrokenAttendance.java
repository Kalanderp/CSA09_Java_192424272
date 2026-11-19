import java.util.HashSet;
import java.util.Set;

// Demonstration only: equals() is overridden but hashCode() is not.
class BrokenStudent {
    final String regNo;
    final String name;

    BrokenStudent(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BrokenStudent)) return false;
        return regNo.equals(((BrokenStudent) obj).regNo);
    }

    // Intentionally missing hashCode() for the experiment.
}

public class BrokenAttendance {
    public static void main(String[] args) {
        Set<BrokenStudent> scans = new HashSet<>();
        scans.add(new BrokenStudent("192311001", "Arun"));
        scans.add(new BrokenStudent("192311002", "Divya"));
        scans.add(new BrokenStudent("192311001", "Arun")); // duplicate registration
        System.out.println("Set size with broken contract: " + scans.size());
        System.out.println("Note: typically 3 on standard JVMs because identity hash codes differ; "
                + "the exact bucket collision pattern is implementation-dependent.");
    }
}
