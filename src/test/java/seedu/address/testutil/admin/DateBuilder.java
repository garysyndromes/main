package seedu.address.testutil.admin;

import java.time.LocalDate;
import java.util.List;

import seedu.address.model.admin.Date;
import seedu.address.model.student.Student;
import seedu.address.testutil.student.TypicalStudents;

/**
 * A utility class to help with building Date objects.
 */
public class DateBuilder {

    public static final String DEFAULT_DATE = "2020-03-21";
    public static final List<Student> STUDENTS = TypicalStudents.getTypicalStudents();

    private LocalDate date;
    private List<Student> students;

    public DateBuilder() {
        date = LocalDate.parse(DEFAULT_DATE);
        students = STUDENTS;
    }

    /**
     * Initializes the DateBuilder with the data of {@code datetToCopy}.
     */
    public DateBuilder(Date dateToCopy) {
        date = dateToCopy.getDate();
        students = dateToCopy.getStudents();
    }

    /**
     * Sets the {@code Date} of the {@code Date} that we are building.
     */
    public DateBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Student} that we are building.
     */
    public DateBuilder withStudents(List<Student> students) {
        this.students = students;
        return this;
    }

    /**
     * Builds a date.
     */
    public Date build() {
        return new Date(date, students);
    }
}
