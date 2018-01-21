package ru.sirius.january.mmm;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
        private TextView timeView;
        private ImageView messengerTypeView;
        private CardView unreadMessagesCntCardView;
        private TextView unreadMessegesCntView;
        private DialogListFragment.OnItemClickListener callback;

        public DialogHolder(View itemView, final DialogListFragment.OnItemClickListener callback) {
            super(itemView);
            message = itemView.findViewById(R.id.last_message_view);
            name = itemView.findViewById(R.id.name_view);
            icon = itemView.findViewById(R.id.chat_image_view);
            timeView = itemView.findViewById(R.id.time_view);
            messengerTypeView = itemView.findViewById(R.id.messenger_type_view);
            unreadMessagesCntCardView = itemView.findViewById(R.id.unread_messages_cnt_card_view);
            unreadMessegesCntView = itemView.findViewById(R.id.cnt_of_unread_view);
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

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
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
        holder.unreadMessegesCntView.setText(String.valueOf(dialog.getUnread()));
        holder.unreadMessagesCntCardView.setCardBackgroundColor(R.color.vk_light_color);

        if (dialog.getMessengerType() != null && dialog.getMessengerType().equals("telegram")) {
            holder.messengerTypeView.setImageBitmap(BitmapFactory.decodeResource(holder.itemView.getContext().getResources(), R.drawable.telegram_ico));
        } else {
            holder.messengerTypeView.setImageBitmap(BitmapFactory.decodeResource(holder.itemView.getContext().getResources(),
                    R.drawable.vk_ico));
        }
        long t = (dialog.getLast().getDateTime().getTime() + 3600 * 3) % (24 * 3600);
        long h = t / 3600;
        long m = t % 3600 / 60;
        holder.timeView.setText(h + ":" + (m < 10 ? "0" : "") + m);
        if (dialog.getUnread() == 0) {
            holder.unreadMessagesCntCardView.setVisibility(View.INVISIBLE);
        }
        else {
            holder.unreadMessagesCntCardView.setVisibility(View.VISIBLE);
            holder.unreadMessegesCntView.setText(String.valueOf(dialog.getUnread()));
        }
        //holder.icon.setImageBitmap(StorageManager.getInstance(null).getCachedBitmap(dialog.getImageKey()));
        Picasso.with(holder.itemView.getContext()).load(dialog.getImageKey()).into(holder.icon);
        holder.dialog = dialog;
    }

    @Override
    public int getItemCount() {
        return GeneralManager.getInstance(null).getDialogsNumber();
    }
}
