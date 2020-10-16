package com.example.libraryutt.Details;


import com.example.libraryutt.Status;

import java.io.Serializable;

public class BorrowDetail implements Serializable {
    String promissoryNoteCode;
    String bookCode;
    String bookName;
    String userCode;
    String borrower;
    Status status;
    String borrowRead;
    String dayCreate;
    String payDay;




    public BorrowDetail() {
    }

    public BorrowDetail(String promissoryNoteCode, String bookCode, String bookName, String userCode, String borrower, Status status, String borrowRead, String dayCreate, String payDay) {
        this.promissoryNoteCode = promissoryNoteCode;
        this.bookCode = bookCode;
        this.bookName = bookName;
        this.userCode = userCode;
        this.borrower = borrower;
        this.status = status;
        this.borrowRead = borrowRead;
        this.dayCreate = dayCreate;
        this.payDay = payDay;
    }

    public String getPromissoryNoteCode() {
        return promissoryNoteCode;
    }

    public void setPromissoryNoteCode(String promissoryNoteCode) {
        this.promissoryNoteCode = promissoryNoteCode;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBorrowRead() {
        return borrowRead;
    }

    public void setBorrowRead(String borrowRead) {
        this.borrowRead = borrowRead;
    }

    public String getDayCreate() {
        return dayCreate;
    }

    public void setDayCreate(String dayCreate) {
        this.dayCreate = dayCreate;
    }

    public String getPayDay() {
        return payDay;
    }

    public void setPayDay(String payDay) {
        this.payDay = payDay;
    }
}
