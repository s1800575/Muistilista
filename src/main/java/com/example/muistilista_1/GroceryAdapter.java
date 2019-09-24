package com.example.muistilista_1;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    // Cursor curson esittää / tuo tiedon tietokannasta
    public GroceryAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView countText;

        public GroceryViewHolder(View itemView) {
            super(itemView);

            // nämä viittaavat lisattavat_tuottee.xml -textiview-kenttiin

            nameText = itemView.findViewById(R.id.textview_name_item);
            countText = itemView.findViewById(R.id.textview_amount_item);
        }
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        // ja tämä esittää lisattava_tuotteet layoutin

        View view = inflater.inflate(R.layout.lisattavat_tuotteet, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(TuotteetTauluun.TuotteenLisays.COLUMN_NIMI));
        int amount = mCursor.getInt(mCursor.getColumnIndex(TuotteetTauluun.TuotteenLisays.COLUMN_MAARA));

        // tällä haetaan pyyhkäistävän tiedon id
        long id = mCursor.getLong(mCursor.getColumnIndex(TuotteetTauluun.TuotteenLisays._ID));

        holder.nameText.setText(name);
        holder.countText.setText(String.valueOf(amount+"kpl "));

        // poimii näytettävän id:n
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}