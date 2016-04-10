package com.project.songer1993.sentire;

/**
 * Created by songer1993 on 10/04/2016.
 */
public class VibrationPattern extends Pattern{
    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    public VibrationPattern(){
        super();
    }

    public VibrationPattern(String Id){
        super();
        mId = Id;
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


    @Override
    public boolean equals(Object o) {
        return o instanceof VibrationPattern && ((VibrationPattern) o).mId == mId;
    }
}
