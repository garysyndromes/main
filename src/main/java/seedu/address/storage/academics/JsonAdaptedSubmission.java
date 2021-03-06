package seedu.address.storage.academics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.academics.Submission;

/**
 * Jackson-friendly version of {@link Submission}.
 */
class JsonAdaptedSubmission {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Submissions %s field is missing!";

    private final String student;
    private final String submitted;
    private final String marked;
    private final String score;

    /**
     * Constructs a {@code JsonAdaptedSubmission} with the given details.
     */
    @JsonCreator
    public JsonAdaptedSubmission(@JsonProperty("student") String student,
                                 @JsonProperty("submitted") String submitted,
                                 @JsonProperty("marked") String marked,
                                 @JsonProperty("score") String score) {
        this.student = student;
        this.submitted = submitted;
        this.marked = marked;
        this.score = score;
    }

    /**
     * Converts a given {@code Submission} into this class for Jackson use.
     */
    public JsonAdaptedSubmission(Submission source) {
        this.student = source.getStudentName();
        this.submitted = source.hasSubmitted() ? "Submitted" : "Not Submitted";
        this.marked = source.isMarked() ? "Marked" : "Not Marked";
        this.score = Integer.toString(source.getScore());
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Submission} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public Submission toModelType() throws IllegalValueException {
        if (student == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "STUDENT NAME"));
        }
        String modelStudent = student;

        if (submitted == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "SUBMITTED"));
        }
        boolean modelSubmitted = submitted.equals("Submitted");

        if (marked == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "MARKED"));
        }
        boolean modelMarked = marked.equals("Marked");

        if (score == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "SCORE"));
        }
        int modelScore = Integer.parseInt(score);

        return new Submission(modelStudent, modelSubmitted, modelMarked, modelScore);
    }

}

