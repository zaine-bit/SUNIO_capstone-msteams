package com.msteams.service;

import com.msteams.model.User;

import java.io.*;

/**
 * Implements session management using Java Serialization.
 * On a successful login, the logged-in User object is serialized to
 * session.dat. Every screen that needs to know who's logged in reads
 * that file back in (rather than relying on an in-memory static field),
 * which is what actually "validates and maintains the session while
 * navigating the system." On logout, the file is deleted.
 */
public class FileSessionManager implements ISessionManager {

    private static final String SESSION_FILE = "session.dat";

    @Override
    public void saveSession(User user) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User loadSession() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (User) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void clearSession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public boolean hasActiveSession() {
        return new File(SESSION_FILE).exists();
    }
}
