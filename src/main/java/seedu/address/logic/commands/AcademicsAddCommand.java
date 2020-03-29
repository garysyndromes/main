package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.academics.Assessment;
import seedu.address.model.academics.Exam;
import seedu.address.model.academics.Homework;
import seedu.address.model.student.Student;

/**
 * Adds an academic progress report to address book.
 */
public class AcademicsAddCommand extends AcademicsCommand {

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": creates a new assessment\n"
            + "Parameters:\n"
            + "desc/[DESCRIPTION]\n"
            + "type/[TYPE: homework/exam]\n"
            + "date/[DATE]\n"
            + "Example: academics name/CS2103T assignment 1 type/homework date/2020-03-04\n";

    public static final String MESSAGE_SUCCESS = "Added assessment:\n%1$s";
    public static final String MESSAGE_DUPLICATE_ASSESSMENT = "This assessment already exists in the academics list";

    private final Assessment toAdd;

    public AcademicsAddCommand(String description, String type, String date) throws CommandException {
        switch (type) {
        case "homework":
            toAdd = new Homework(description, date);
            break;
        case "exam":
            toAdd = new Exam(description, date);
            break;
        default:
            throw new CommandException(Messages.MESSAGE_INVALID_ASSESSMENT_TYPE);
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasAssessment(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ASSESSMENT);
        }
        List<Student> lastShownList = model.getFilteredStudentList();
        toAdd.setStudents(lastShownList);
        model.addAssessment(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AcademicsAddCommand // instanceof handles nulls
                && toAdd.equals(((AcademicsAddCommand) other).toAdd));
    }
}
