package ru.alxr.contacts.features.contacts;

import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import ru.alxr.contacts.R;
import ru.alxr.contacts.features.details.ContactImpl;
import ru.alxr.contacts.features.details.IContact;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    ContactsAdapter(LayoutInflater inflater, Callback callback) {
        mLayoutInflater = inflater;
        mCallback = callback;
    }

    private final LayoutInflater mLayoutInflater;
    private final Callback mCallback;

    private Cursor mCursor;

    public interface Callback {
        void onContactClick(IContact contact);
    }

    private IContact getContact(int position) {
        mCursor.moveToPosition(position);
        String name = mCursor.getString(mCursor.getColumnIndex("display_name"));
        String phone = mCursor.getString(mCursor.getColumnIndex("data1"));
        String email = mCursor.getString(mCursor.getColumnIndex("account_name"));
        String account = mCursor.getString(mCursor.getColumnIndex("account_name"));
        String image;
        try{
            image = mCursor.getString(mCursor.getColumnIndex("photo_uri"));
        }catch (Exception e){
            image = null;
        }
        //todo more fields if required

        return new ContactImpl
                .Builder()
                .setName(name)
                .setEmail(email)
                .setAccount(account)
                .setPhone(phone)
                .setImage(image)
                .get();
    }

    void setCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            Log.d("ContactsAdapter", String.format("CURSOR HAS %s RECORDS AND %s columns", cursor.getCount(), cursor.getColumnCount()));
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String name = cursor.getColumnName(i);
                int type = cursor.getType(i);

                //if (type != Cursor.FIELD_TYPE_STRING) continue;
                Log
                        .d(
                                "ContactsAdapter",
                                String
                                        .format(
                                                "%d. %s : %s VALUE=[%s]",
                                                i,
                                                name,
                                                getDataType(type),
                                                cursor.getString(i)
                                        )
                        );
            }
        }
    }

    private String getDataType(int type) {
        switch (type) {
            case Cursor.FIELD_TYPE_NULL:
                return "FIELD_TYPE_NULL";
            case Cursor.FIELD_TYPE_INTEGER:
                return "FIELD_TYPE_INTEGER";
            case Cursor.FIELD_TYPE_FLOAT:
                return "FIELD_TYPE_FLOAT";
            case Cursor.FIELD_TYPE_STRING:
                return "FIELD_TYPE_STRING";
            case Cursor.FIELD_TYPE_BLOB:
                return "FIELD_TYPE_BLOB";
            default:
                return "Unknown";
        }
    }

    private int getItemCount(@Nullable Cursor cursor) {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int nameIndex = mCursor.getColumnIndex("display_name");
        String name = mCursor.getString(nameIndex);
        String accountName = mCursor.getString(mCursor.getColumnIndex("account_name"));
        int basePhoneIndex = mCursor.getColumnIndex("data1");
        String phone = mCursor.getString(basePhoneIndex);
        holder.setName(String.format("%s (%s)", name, accountName)).setPhone(phone);
    }

    @Override
    public int getItemCount() {
        return getItemCount(mCursor);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameView;
        private TextView mPhoneView;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.name);
            mPhoneView = itemView.findViewById(R.id.phone);
            itemView.setOnClickListener(this);
        }

        ContactViewHolder setName(String value) {
            mNameView.setText(value);
            return this;
        }

        void setPhone(String value) {
            mPhoneView.setText(value);
        }

        @Override
        public void onClick(View v) {
            mCallback.onContactClick(getContact(getAdapterPosition()));
        }
    }

}