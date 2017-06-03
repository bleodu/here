package com.kenfestoche.smartcoder.kenfestoche;

import com.plumillonforge.android.chipview.Chip;

/**
 * Created by smartcoder on 11/04/2017.
 */

public class Tag implements Chip {
    private String mName;
    private int mType = 0;

    public Tag(String name, int type) {
        this(name);
        mType = type;
    }

    public Tag(String name) {
        mName = name;
    }

    @Override
    public String getText() {
        return mName;
    }

    public int getType() {
        return mType;
    }




}