package com.hanna.model;

public class Grade {
    private String subject;
    private double prelimGrade;
    private double midtermGrade;
    private double finalGrade;

    public Grade() {

    }

    public Grade(String subject, double prelimGrade, double midtermGrade, double finalGrade) {
        this.subject = subject;
        this.prelimGrade = prelimGrade;
        this.midtermGrade = midtermGrade;
        this.finalGrade = finalGrade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getPrelimGrade() {
        return prelimGrade;
    }

    public void setPrelimGrade(double prelimGrade) {
        this.prelimGrade = prelimGrade;
    }

    public double getMidtermGrade() {
        return midtermGrade;
    }

    public void setMidtermGrade(double midtermGrade) {
        this.midtermGrade = midtermGrade;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    @Override
    public String toString() {
        return String.format("""
                Subject: %s
                Prelim Grade: %.2f
                Midterm Grade: %.2f
                Final Grade: %.2f
                """, subject, prelimGrade,
                midtermGrade, finalGrade);
    }

}
