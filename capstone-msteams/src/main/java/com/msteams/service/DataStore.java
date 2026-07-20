package com.msteams.service;

import com.msteams.model.Assignment;

/**
 * Holds only navigation state between screens (which assignment was
 * selected). Who's logged in now lives in session.dat via ISessionManager,
 * not here -- see FileSessionManager.
 */
public class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private Assignment selectedAssignment;

    private DataStore() {
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public Assignment getSelectedAssignment() {
        return selectedAssignment;
    }

    public void setSelectedAssignment(Assignment selectedAssignment) {
        this.selectedAssignment = selectedAssignment;
    }
}
