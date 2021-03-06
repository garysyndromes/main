package seedu.address.logic.commands.notes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_NOTE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_NOTE;
import static seedu.address.testutil.academics.TypicalAssessments.getTypicalAcademics;
import static seedu.address.testutil.admin.TypicalDates.getTypicalAdmin;
import static seedu.address.testutil.event.TypicalEvents.getTypicalEventHistory;
import static seedu.address.testutil.notes.TypicalNotes.N_DESC_JANE;
import static seedu.address.testutil.notes.TypicalNotes.N_DESC_KELVIN;
import static seedu.address.testutil.notes.TypicalNotes.getTypicalNotesManager;
import static seedu.address.testutil.student.TypicalStudents.getTypicalTeaPet;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.notes.NotesEditCommand.EditNotesDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.notes.Notes;
import seedu.address.model.student.Student;
import seedu.address.model.student.TeaPet;
import seedu.address.testutil.notes.EditNotesDescriptorBuilder;
import seedu.address.testutil.notes.NotesBuilder;
import seedu.address.testutil.student.StudentBuilder;

public class NotesEditCommandTest {
    private Model model = new ModelManager(getTypicalTeaPet(), getTypicalAcademics(), getTypicalAdmin(),
            getTypicalNotesManager(), getTypicalEventHistory(), new UserPrefs());

    /**
     * In order for a note to exist, the corresponding student tagged in the note must be in the class-list.
     * This method creates a student with name similar to the one in the note, simply for testing purposes.
     */
    public static Student createStudentStubForModel(Notes note) {
        Student student = new StudentBuilder().withName(note.getStudent()).build();
        return student;
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Notes editedNote = new NotesBuilder().build();
        EditNotesDescriptor descriptor = new EditNotesDescriptorBuilder(editedNote).build();
        NotesEditCommand editCommand = new NotesEditCommand(INDEX_FIRST_NOTE, descriptor);

        String expectedMessage = String.format(NotesEditCommand.MESSAGE_SUCCESS, editedNote);

        Student stubStudent = createStudentStubForModel(editedNote);
        model.addStudent(stubStudent);

        Model expectedModel = new ModelManager(new TeaPet(model.getTeaPet()), model.getAcademics(),
                model.getAdmin(), model.getNotesManager(), getTypicalEventHistory(), new UserPrefs());

        expectedModel.setNote(model.getFilteredNotesList().get(0), editedNote);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws CommandException {
        Notes firstNoteInList = model.getFilteredNotesList().get(0);
        Notes editedNote = new NotesBuilder().build().setStudent(firstNoteInList.getStudent());

        //Adds a student stub to the model
        Student studentStub = createStudentStubForModel(editedNote);
        model.addStudent(studentStub);

        //Student's name in note is not edited
        EditNotesDescriptor descriptor = new EditNotesDescriptorBuilder()
                .withContent(editedNote.getContent())
                .withPriority(editedNote.getPriority()).build();
        NotesEditCommand editCommand = new NotesEditCommand(INDEX_FIRST_NOTE, descriptor);

        CommandResult commandResult = editCommand.execute(model);

        String expectedMessage = String.format(NotesEditCommand.MESSAGE_SUCCESS, editedNote);

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
    }

    @Test
    public void equals() {
        final NotesEditCommand standardCommand = new NotesEditCommand(INDEX_FIRST_NOTE, N_DESC_KELVIN);

        // same values -> returns true
        EditNotesDescriptor copyDescriptor = new EditNotesDescriptor(N_DESC_KELVIN);
        NotesEditCommand commandWithSameValues = new NotesEditCommand(INDEX_FIRST_NOTE, copyDescriptor);

        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new NotesExportCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new NotesEditCommand(INDEX_SECOND_NOTE, N_DESC_KELVIN)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new NotesEditCommand(INDEX_FIRST_NOTE, N_DESC_JANE)));
        assertFalse(standardCommand.equals(new NotesEditCommand(INDEX_SECOND_NOTE, N_DESC_KELVIN)));


    }


}
