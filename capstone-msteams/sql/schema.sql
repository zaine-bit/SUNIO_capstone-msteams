-- Run this in phpMyAdmin's SQL tab, on a database named msteams_db.
-- Create the database first (New -> name it msteams_db -> Create),
-- then open it, click the SQL tab, paste this whole file, and click Go.

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'TEACHER') NOT NULL
);

CREATE TABLE assignments (
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    due_date DATE,
    teacher_id INT NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES users(user_id)
);

CREATE TABLE submissions (
    submission_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    assignment_id INT NOT NULL,
    file_url VARCHAR(255),
    grade DOUBLE NULL,
    FOREIGN KEY (student_id) REFERENCES users(user_id),
    FOREIGN KEY (assignment_id) REFERENCES assignments(assignment_id)
);

CREATE TABLE channels (
    channel_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    channel_id INT NOT NULL,
    author_id INT NOT NULL,
    content TEXT NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (channel_id) REFERENCES channels(channel_id),
    FOREIGN KEY (author_id) REFERENCES users(user_id)
);

-- Seed one channel so the Class Channel screen has somewhere to post to.
INSERT INTO channels (name) VALUES ('General');

-- Optional: seed one teacher and one assignment so there's something to see
-- right after setup, before you've registered your own accounts.
-- Password below is the plain word "password" (not hashed -- see README note).
INSERT INTO users (name, email, password, role) VALUES ('Prof. Santos', 'santos@school.edu', 'password', 'TEACHER');
INSERT INTO assignments (title, description, due_date, teacher_id) VALUES
    ('UML Class Diagram', 'Submit your class diagram PDF.', '2026-08-15', 1);
