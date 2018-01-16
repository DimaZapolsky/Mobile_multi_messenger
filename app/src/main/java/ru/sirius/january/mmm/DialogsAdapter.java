package ru.sirius.january.mmm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.sirius.january.mmm.store.StorageManager;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.DialogHolder> {

    class DialogHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            callback.onItemClicked(dialog);
        }

        private Dialog dialog;
        private TextView message;
        private TextView name;
        private ImageView icon;
        private DialogListFragment.OnItemClickListenner callback;

        public DialogHolder(View itemView, final DialogListFragment.OnItemClickListenner callback) {
            super(itemView);
            message = itemView.findViewById(R.id.last_message_view);
            name = itemView.findViewById(R.id.name_view);
            icon = itemView.findViewById(R.id.chat_image_view);
            this.callback = callback;
            itemView.setOnClickListener(this);
        }
    }

    private ArrayList<Dialog> dialogs;
    private DialogListFragment.OnItemClickListenner callback;

    public DialogsAdapter(ArrayList<Dialog> dialogs, DialogListFragment.OnItemClickListenner callback) {
        this.dialogs = dialogs;
        this.callback = callback;
    }

    public void addDialog(Dialog dialog) {
        dialogs.add(dialog);
        notifyItemChanged(dialogs.size() - 1);
    }

    @Override
    public DialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DialogHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_item, parent, false), callback);
    }

    @Override
    public void onBindViewHolder(DialogHolder holder, int position) {
        Dialog dialog = dialogs.get(position);
        Message last_message = dialog.getMessages().get(dialog.getMessages().size() - 1);
        holder.message.setText(last_message.getText());
        holder.name.setText(dialog.getName());
        holder.icon.setImageBitmap(StorageManager.getInstance(null).getCachedBitmap(dialog.getImageKey()));
        holder.dialog = dialog;
    }

    @Override
    public int getItemCount() {
        return dialogs.size();
    }
}
