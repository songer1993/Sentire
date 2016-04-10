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
    public Pattern(String id, String name, String emotion, String type, String value, int score) {
        this.setName(name);
        this.setEmotion(emotion);
        this.setType(type);
        this.setValue(value);
        this.setScore(score);
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

}