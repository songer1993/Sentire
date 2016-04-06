package com.project.songer1993.sentire;

/**
 * Created by songer1993 on 05/04/2016.
 */
/**
 * Represents an item in a Vibration Patterns list
 */
public class LightPattern{

    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("emotion")
    private String mEmotion;

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
     * ToDoItem constructor
     */
    public LightPattern() {

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
    public LightPattern(String id, String emotion, String type, String value, int score) {
        this.setId(id);
        this.setEmotion(emotion);
        this.setValue(value);
        this.setScore(score);
    }

    /**
     * Returns the item id
     */
    public String getId() {
        return mId;
    }

    /**
     * Sets the item id
     *
     * @param id
     *            id to set
     */
    public final void setId(String id) {
        mId = id;
    }

    /**
     * Returns the item text
     */
    public String getmEmotion() {
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


    @Override
    public boolean equals(Object o) {
        return o instanceof LightPattern && ((LightPattern) o).mId == mId;
    }
}
