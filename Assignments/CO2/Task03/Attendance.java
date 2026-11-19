import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Student {
    private final String regNo;
    private final String name;

    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;
        Student other = (Student) obj;
        return Objects.equals(regNo, other.regNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }

    @Override
    public String toString() {
        return regNo + " " + name;
    }
}

public class Attendance {
    public static void main(String[] args) {
        Set<Student> scans = new HashSet<>();
        Student sameObject = new Student("192311001", "Arun");

        // Test 1: same object scanned twice.
        scans.add(sameObject);
        scans.add(sameObject);
        System.out.println("Test 1 - same object twice: " + scans.size());

        // Test 2: two different objects with the same registration number.
        scans.add(new Student("192311002", "Divya"));
        scans.add(new Student("192311001", "Arun"));
        System.out.println("Test 2 - duplicate registration removed: " + scans.size());

        // Test 3: different registration numbers remain separate.
        scans.add(new Student("192311003", "Kiran"));
        System.out.println("Test 3 - three unique registrations: " + scans.size());
        for (Student student : scans) System.out.println(student);
    }
}
