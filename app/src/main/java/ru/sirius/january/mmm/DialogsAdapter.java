package ru.sirius.january.mmm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.data.abstracts.Message;
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
        private DialogListFragment.OnItemClickListener callback;

        public DialogHolder(View itemView, final DialogListFragment.OnItemClickListener callback) {
            super(itemView);
            message = itemView.findViewById(R.id.last_message_view);
            name = itemView.findViewById(R.id.name_view);
            icon = itemView.findViewById(R.id.chat_image_view);
            this.callback = callback;
            itemView.setOnClickListener(this);
        }
    }

    private DialogListFragment.OnItemClickListener callback;

    public DialogsAdapter(DialogListFragment.OnItemClickListener callback) {
        this.callback = callback;
    }

    public void OnDialogUpdate(int num) {
        notifyItemChanged(num);
    }

    @Override
    public DialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DialogHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_item, parent, false), callback);
    }

    @Override
    public void onBindViewHolder(DialogHolder holder, int position) {
        Dialog dialog = GeneralManager.getInstance(null).getDialogByPosition(position);
        Message last_message = dialog.getLast();
        String message = last_message.getText();
        if (message.length() > 40) {
            message = message.substring(0, 37) + "...";
        }
        holder.message.setText(message);
        holder.name.setText(dialog.getName());
        holder.icon.setImageBitmap(StorageManager.getInstance(null).getCachedBitmap(dialog.getImageKey()));
        holder.dialog = dialog;
    }

    @Override
    public int getItemCount() {
        return GeneralManager.getInstance(null).getDialogsNumber();
    }
}
