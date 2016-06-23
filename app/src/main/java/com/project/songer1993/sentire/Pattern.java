package com.project.songer1993.sentire;

/**
 * Created by songer1993 on 10/04/2016.
 */
public class Pattern {


    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("name")
    private String mName;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("emotion")
    private String mEmotion;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("type")
    private String mType;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("value")
    private String mValue;


    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("score")
    private int mScore;


    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("score_happy")
    private int mScoreHappy;


    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("score_sad")
    private int mScoreSad;


    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("score_fearful")
    private int mScoreFearful;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("score_angry")
    private int mScoreAngry;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("score_neutral")
    private int mScoreNeutral;

    /**
     * ToDoItem constructor
     */
    public Pattern() {

    }

    @Override
    public String toString() {
        return getValue();
    }

    /**
     * Initializes a new ToDoItem
     *
     * @param emotion
     *            The item text
     * @param id
     *            The item id
     */
    public Pattern(String id, String name, String emotion, String type, String value, int score, int scoreHappy, int scoreSad, int scoreFearful, int scoreAngry, int scoreNeutral) {
        this.setName(name);
        this.setEmotion(emotion);
        this.setType(type);
        this.setValue(value);
        this.setScore(score);
        this.setScoreHappy(scoreHappy);
        this.setScoreSad(scoreSad);
        this.setScoreFearful(scoreFearful);
        this.setScoreAngry(scoreAngry);
        this.setScoreNeutral(scoreNeutral);

    }




    /**
     * Returns the item id
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the item id
     *
     * @param name
     *            id to set
     */
    public final void setName(String name) {
        mName = name;
    }

    /**
     * Returns the item text
     */
    public String getEmotion() {
        return mEmotion;
    }

    /**
     * Sets the item text
     *
     * @param emotion
     *            text to set
     */
    public final void setEmotion(String emotion) {
        mEmotion = emotion;
    }

    /**
     * Returns the item text
     */
    public String getType() {
        return mType;
    }

    /**
     * Sets the item text
     *
     * @param type
     *            text to set
     */
    public final void setType(String type) {
        mType = type;
    }

    /**
     * Returns the item text
     */
    public String getValue() {
        return mValue;
    }

    /**
     * Sets the item text
     *
     * @param value
     *            text to set
     */
    public final void setValue(String value) {
        mValue = value;
    }

    /**
     * Returns the item text
     */
    public int getScore() {
        return mScore;
    }

    /**
     * Sets the item text
     *
     * @param score
     *            text to set
     */
    public final void setScore(int score) {
        mScore = score;
    }

    /**
     * Returns the item text
     */
    public int getScoreHappy() {
        return mScoreHappy;
    }

    /**
     * Sets the item text
     *
     * @param scoreHappy
     *            text to set
     */
    public final void setScoreHappy(int scoreHappy) {
        mScoreHappy = scoreHappy;
    }

    /**
     * Returns the item text
     */
    public int getScoreSad() {
        return mScoreSad;
    }

    /**
     * Sets the item text
     *
     * @param scoreSad
     *            text to set
     */
    public final void setScoreSad(int scoreSad) {
        mScoreSad = scoreSad;
    }

    /**
     * Returns the item text
     */
    public int getScoreFearful() {
        return mScoreFearful;
    }

    /**
     * Sets the item text
     *
     * @param scoreFearful
     *            text to set
     */
    public final void setScoreFearful(int scoreFearful) {
        mScoreFearful = scoreFearful;
    }


    /**
     * Returns the item text
     */
    public int getScoreAngry() {
        return mScoreAngry;
    }

    /**
     * Sets the item text
     *
     * @param scoreAngry
     *            text to set
     */
    public final void setScoreAngry(int scoreAngry) {
        mScoreAngry = scoreAngry;
    }

    /**
     * Returns the item text
     */
    public int getScoreNeutral() {
        return mScoreNeutral;
    }

    /**
     * Sets the item text
     *
     * @param scoreFearful
     *            text to set
     */
    public final void setScoreNeutral(int scoreFearful) {
        mScoreFearful = scoreFearful;
    }

}