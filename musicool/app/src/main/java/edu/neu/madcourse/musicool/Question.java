package edu.neu.madcourse.musicool;

public class Question {
    private String qId;
    private String answer;
    private int imageId;
    //todo: set up index
    private int index;

    public Question(String answer, int imageId) {
        this.answer = answer;
        this.imageId = imageId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
