package com.kenfestoche.smartcoder.kenfestoche.model;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.kenfestoche.smartcoder.kenfestoche.R;
import com.kenfestoche.smartcoder.kenfestoche.Tag;
import com.plumillonforge.android.chipview.ChipViewAdapter;

/**
 * Created by smartcoder on 11/04/2017.
 */

public class MainChipViewAdapter extends ChipViewAdapter {

        public MainChipViewAdapter(Context context) {
            super(context);
        }


        @Override
        public int getLayoutRes(int position) {
            Tag tag = (Tag) getChip(position);
            return R.layout.chip_close;

        }

        @Override
        public int getBackgroundColor(int position) {
            Tag tag = (Tag) getChip(position);
            return getColor(R.color.gris);

        }

        @Override
        public int getBackgroundColorSelected(int position) {
            return 0;
        }

        @Override
        public int getBackgroundRes(int position) {
            return 0;
        }

        @Override
        public void onLayout(View view, int position) {
            Tag tag = (Tag) getChip(position);


        }

}
