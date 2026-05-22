package com.example.exercice1;

public class GradingCalculator {
    public int Score;
    public int AttendancePercentage;

    public GradingCalculator(int score, int attendancePercentage) {
        Score = score;
        AttendancePercentage = attendancePercentage;
    }

    public char getGrade() {
        return computeGrade();
    }

    char computeGrade() {
        if (Score > 90 && AttendancePercentage > 70) return 'A';
        else if (Score > 80 && AttendancePercentage > 60) return 'B';
        else if (Score > 60 && AttendancePercentage > 60) return 'C';
        else return 'F';
    }

    private char stubGrade() {
        return 'X';
    }
}
