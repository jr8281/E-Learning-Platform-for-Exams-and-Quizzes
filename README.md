# E-Learning-Platform-for-Exams-and-Quizzes
This project is a Java Swing–based desktop application designed to simplify online learning through interactive quizzes and tests.
It enables students to take quizzes, view scores instantly, and faculty to create, manage, and evaluate quizzes using a MySQL database for data storage.
The goal is to create a smart, user-friendly environment for digital learning and assessment.

## Objectives:
1)To design a user-friendly platform for online exams and quizzes.
2)To allow faculty to create and manage quizzes efficiently.
3)To let students take tests and view instant results.
4)To integrate MySQL for secure storage of users, quizzes, questions and scores.

## Features:
👨‍🏫 Faculty Module:
Faculty login & registration.
Create and manage quizzes.
Add, update, or delete questions.
View and analyze student performance.

👩‍🎓 Student Module:
Student login & registration.
View available quizzes.
Take timed quizzes with multiple-choice questions.
Get instant score and feedback.

⚙️ General Features:
Modern Java Swing GUI with background design.
Database integration with MySQL.

## System Artitecture:
The project follows a three-tier architecture:
Presentation Layer: Java Swing GUI (Login, Dashboard, Quiz windows).
Logic Layer: Java classes handling quiz operations and scoring logic.
Database Layer: MySQL tables for storing users, quizzes, and scores.

## Software Requirements:
Language: Java (JDK 17 or later).
IDE: IntelliJ IDEA..
Database: MySQL.
Libraries: javax.swing, java.awt, java.sql .

## Database Design:
Tables used in MySQL:
1)users – stores login information for students and faculty.
2)quizzes – stores quiz titles and creator details.
3)questions – stores all quiz questions and correct answers.
4)scores – stores quiz scores and date_taken for each student.

## How to run:
Clone this repository.
Import the project into IntelliJ IDEA.
Create the MySQL database and tables.
Update the DBUtil.java file with your MySQL credentials.
Run IntroPage.java to start the application.

## GUI Design:
Login Page: User authentication for faculty & students.
Faculty Dashboard: Create, edit, and delete quizzes.
Student Dashboard: Select and attempt quizzes.
Result Page: Shows quiz results and score history.

## 🚀Future Enhancements
1)Add timer-based quizzes
2)Include leaderboard for top scores.
3)Add password recovery system.
4)Enable export of results as PDF.
5)Support for multimedia questions (images, audio, etc.).

## Conclusion:
The project successfully demonstrates Object-Oriented Programming, GUI Design, and Database Integration in Java.
It provides an efficient and scalable solution for digital learning and assessments.
