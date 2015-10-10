package com.mettwurst.skatdb;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MySimpleCursorAdapter extends SimpleCursorAdapter {
    private Context mContext;
    private Context appContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;
    private int[] mTo;

    public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.layout=layout;
        this.mContext = context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
        this.mTo = to;
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        final int spielerPunkteIndex = cr.getColumnIndex(DBContract.Entry.COL_PUNKTE_SP1);
        for (int offset = 0; offset < 5; offset++) {
            int delta = cr.getInt(spielerPunkteIndex + offset);
            if (delta == 0) {
                TextView textView = (TextView) view.findViewById(mTo[1 + offset]);
                if (textView != null) {
                    textView.setText("-");
                }
            }
        }

        final int spielerzahl = cursor.getInt(cursor.getColumnIndex(DBContract.Entry.COL_SPIELERZAHL));
        if (spielerzahl > 3) {
            final int geber = cursor.getInt(cr.getColumnIndex(DBContract.Entry.COL_GEBER));
            TextView tvGeber = (TextView) view.findViewById(mTo[geber]);
            tvGeber.setText("x");
        }

        final int solist = cr.getInt(cr.getColumnIndex(DBContract.Entry.COL_SOLIST));
        TextView tvSolist = (TextView) view.findViewById(mTo[solist]);
        tvSolist.setTypeface(null, Typeface.BOLD);
    }
}
