package seedu.address.logic.commands.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.model.student.exceptions.StudentNotFoundException;

/**
 * Finds and lists all students in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class StudentFindCommand extends StudentCommand {

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all students whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final NameContainsKeywordsPredicate predicate;

    public StudentFindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws StudentNotFoundException {
        requireNonNull(model);
        model.updateFilteredStudentList(predicate);
        if (model.getFilteredStudentList().size() == 0) {
            model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
            throw new StudentNotFoundException();
        }
        return new CommandResult(
                String.format(Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW, model.getFilteredStudentList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StudentFindCommand // instanceof handles nulls
                && predicate.equals(((StudentFindCommand) other).predicate)); // state check
    }
}
