package com.msteams.service;

import com.msteams.model.User;

/**
 * Abstraction for saving/loading/clearing the logged-in user's session.
 * Controllers depend on this interface, not on FileSessionManager directly,
 * which is the Dependency Inversion Principle in practice: if the session
 * mechanism ever changed (e.g. to a database-backed session, or an in-memory
 * one for tests), only the implementation would need to change, not every
 * controller that uses it.
 */
public interface ISessionManager {

    void saveSession(User user);

    User loadSession();

    void clearSession();

    boolean hasActiveSession();
}
