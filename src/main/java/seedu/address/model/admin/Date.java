package seedu.address.model.admin;

import java.time.LocalDate;
import java.util.List;

import seedu.address.model.student.Student;

/**
 * Represents a Date of the admin details of the class.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Date {

    private LocalDate date;
    private List<Student> students;

    /**
     * Every entry field must be present and not null.
     *
     * @param date description of assessment.
     */
    public Date(LocalDate date, List<Student> students) {
        this.date = date;
        this.students = students;
    }

    /**
     * Returns the date of the admin detail.
     *
     * @return date of admin detai;.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns true if both dates have the same date.
     * This defines a weaker notion of equality between two dates.
     */
    public boolean isSameDate(Date otherDate) {
        if (otherDate == this) {
            return true;
        }
        return otherDate != null
                && otherDate.getDate().equals(getDate());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        String fullDate = date.toString();
        String studentString = "";
        for (Student student : students) {
            studentString += student.adminToString() + " ";
        }
        studentString.trim();
        String dateString = fullDate + " " + studentString;
        builder.append(dateString);
        return builder.toString();
    }
}

