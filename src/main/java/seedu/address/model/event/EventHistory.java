package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.StringUtil.calculateStringSimilarityPercentage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.util.Pair;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.components.VEvent;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.EventUtil;
import seedu.address.model.event.exceptions.DuplicateVEventException;
import seedu.address.model.event.exceptions.VEventNotFoundException;


/**
 * Event history stores the history of events and provides mappings from events to vevents.
 */
public class EventHistory extends EventUtil implements ReadOnlyEvents, ReadOnlyVEvents, Iterable<VEvent> {
    private final ObservableList<VEvent> vEvents = FXCollections.observableArrayList();
    private final ObservableList<VEvent> vEventsUnmodifiableList = FXCollections.unmodifiableObservableList(vEvents);

    public EventHistory(){}

    /**
     * Creates a new list of VEvents using readOnlyEvents in {@code readOnlyEvents}
     */
    public EventHistory(ReadOnlyEvents readOnlyEvents) {
        this();
        resetDataWithReadOnlyEvents(readOnlyEvents);
    }

    /**
     * Creates a new list of VEvents using events in {@code events}
     */
    public EventHistory(List<Event> events) {
        this();
        resetDataWithEvents(events);
    }

    /**
     * Retruns all Events from VEvents observable list
     * @return
     */
    public List<Event> getEvents() {
        return vEventsToEventsMapper(this.vEvents);
    }

    /**
     * Maps VEvents to events list
     */
    public List<Event> vEventsToEventsMapper(List<VEvent> vEvents) {
        ArrayList<Event> list = new ArrayList<>();
        for (VEvent vEvent : vEvents) {
            list.add(vEventToEventMapper(vEvent));
        }
        return list;
    }

    /**
     * Maps events to list of VEvents
     */
    public ArrayList<VEvent> eventsToVEventsMapper(List<Event> events) {
        ArrayList<VEvent> list = new ArrayList<>();
        for (Event e : events) {
            VEvent vEvent = eventToVEventMapper(e);
            list.add(vEvent);
        }
        return list;
    }

    /**
     * Maps readOnlyEvents to list of VEvents
     */
    public List<VEvent> readOnlyEventsToVEventsMapper(ReadOnlyEvents readOnlyEvents) {
        return eventsToVEventsMapper(readOnlyEvents.getEvents());
    }

    /**
     * Resets the data of {@code EventHistory} with {@code data} that are read only
     */
    public void resetDataWithReadOnlyEvents(ReadOnlyEvents data) {
        requireNonNull(data);
        setVEvents(readOnlyEventsToVEventsMapper(data));
    }

    /**
     * Resets the data of {@code EventHistory} with {@code data}
     */
    public void resetDataWithEvents(List<Event> data) {
        requireNonNull(data);
        setVEvents(eventsToVEventsMapper(data));
    }

    /**
     * Sets the data of EventHistory
     */
    public void setVEvents(List<VEvent> vEvents) {
        requireNonNull(vEvents);
        if (!isVEventsUnique(vEvents)) {
            throw new DuplicateVEventException();
        }
        this.vEvents.setAll(vEvents);
    }

    /**
     * Checks if the vEvent list have all unique vEvent.
     */
    public boolean isVEventsUnique(List<VEvent> vEvents) {
        Set<VEvent> set = new HashSet<VEvent>(vEvents);
        int listSize = vEvents.size();
        int setSize = set.size();
        if (setSize < listSize) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Adds a new VEvent to the VEvent list
     */
    public void addVEvent(VEvent vEvent) {
        requireNonNull(vEvent);
        boolean bool = contains(vEvent);
        if (contains(vEvent)) {
            throw new DuplicateVEventException();
        } else {
            vEvents.add(vEvent);
        }

    }

    /**
     * Deletes the VEvent at the {@code index} of the list
     */
    public VEvent deleteVEvent(Index index) {
        requireNonNull(index);
        return vEvents.remove(index.getZeroBased());
    }

    /**
     * Returns the VEvent object
     */
    public VEvent getVEvent(Index index) {
        requireNonNull(index);
        return vEvents.get(index.getZeroBased());
    }

    /**
     * Replaces the VEvent at {@param index}
     */
    public void setVEvent(Index index, VEvent vEvent) {
        requireNonNull(index);
        VEvent selectedVEvent = vEvents.get(index.getZeroBased());
        if (!isEqualEvent(selectedVEvent, vEvent) && contains(vEvent)) {
            throw new DuplicateVEventException();
        } else {
            vEvents.set(index.getZeroBased(), vEvent);
        }

    }

    /**
     * Returns the unmodifiable list
     */
    public ObservableList<VEvent> getVEvents() {
        return this.vEventsUnmodifiableList;
    }

    /**
     * Returns true if list contains the VEvent of {@code vEvent}
     */
    public boolean contains(VEvent vEventCheck) {
        requireNonNull(vEventCheck);
        return vEvents.stream().anyMatch(vEvent -> isEqualEvent(vEvent, vEventCheck));
    }


    /**
     * Searches the list of vEvents and return the VEvents which have the same eventName as the parameter entered.
     *
     * @return List of pair of Indexes and VEvents which equal to eventName
     */
    public List<Pair<Index, VEvent>> searchVEvents(String eventName) {
        requireNonNull(eventName);

        List<Pair<Index, VEvent>> resultIndexList = new ArrayList<>();
        for (int i = 0; i < vEvents.size(); i++) {
            VEvent currentVEvent = vEvents.get(i);
            if (currentVEvent.getSummary().getValue().equalsIgnoreCase(eventName)) {
                resultIndexList.add(new Pair<Index, VEvent>(Index.fromZeroBased(i), currentVEvent));
            }
        }
        return resultIndexList;
    }

    /**
     * Search for the event with the most similar name to the event name specified in the parameter.
     * @param eventName eventName to be found
     * @return a vEvent object that is closest to the event name specified
     * @throws VEventNotFoundException if there are no VEvents that is remotely close to the event name specified.
     */
    public Pair<Index, VEvent> searchMostSimilarVEventName(String eventName) throws VEventNotFoundException {
        requireNonNull(eventName);

        if (vEvents.isEmpty()) {
            throw new VEventNotFoundException();
        }
        VEvent mostSimilarVEvent = vEvents.get(0);
        Integer mostSimilarIndex = 0;
        double highestSimilarityPercentage =
                calculateStringSimilarityPercentage(mostSimilarVEvent.getSummary().getValue(), eventName);

        for (int i = 1; i < vEvents.size(); i++) {
            VEvent currentEvent = vEvents.get(i);
            double eventNameSimilarity =
                    calculateStringSimilarityPercentage(currentEvent.getSummary().getValue(), eventName);
            if (eventNameSimilarity > highestSimilarityPercentage) {
                mostSimilarIndex = i;
                mostSimilarVEvent = currentEvent;
                highestSimilarityPercentage = eventNameSimilarity;
            }
        }

        return new Pair<Index, VEvent>(Index.fromZeroBased(mostSimilarIndex), mostSimilarVEvent);
    }

    /**
     * Return the list of all pairs of indexes and VEvents that exists.
     *
     * @return List of all pairs of Indexes and VEvents
     */
    public List<Pair<Index, VEvent>> getAllVEventsWithIndex() {

        List<Pair<Index, VEvent>> resultIndexList = new ArrayList<>();
        for (int i = 0; i < vEvents.size(); i++) {
            VEvent currentVEvent = vEvents.get(i);
            resultIndexList.add(new Pair<Index, VEvent>(Index.fromZeroBased(i), currentVEvent));
        }
        return resultIndexList;
    }



    /**
     * Returns true if both students have the same identity and data fields.
     * This defines a stronger notion of equality between two students.
     */
    @Override
    public boolean equals (Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EventHistory)) {
            return false;
        }

        EventHistory otherEventHistory = (EventHistory) other;
        ObservableList<VEvent> myVEventList = this.getVEvents();
        ObservableList<VEvent> otherVEventList = otherEventHistory.getVEvents();

        if (myVEventList.size() != otherVEventList.size()) {
            return false;
        }

        for (int i = 0; i < myVEventList.size(); i++) {
            VEvent myVEvent = myVEventList.get(i);
            VEvent otherVEvent = otherVEventList.get(i);
            if (!myVEvent.getSummary().equals(otherVEvent.getSummary())
                    || !myVEvent.getDateTimeStart().equals(otherVEvent.getDateTimeStart())
                    || !myVEvent.getDateTimeEnd().equals(otherVEvent.getDateTimeEnd())
                    || !myVEvent.getCategories().equals(otherVEvent.getCategories())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<VEvent> iterator() {
        return vEvents.iterator();
    }

}
