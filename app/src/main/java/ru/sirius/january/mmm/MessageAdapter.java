package ru.sirius.january.mmm;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.data.abstracts.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    Dialog dialog = null;

    public void onMessageUpdate(int pos) {
        notifyItemChanged(pos);
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        private TextView message;

        public MessageHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.one_message_view);
        }
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view, parent, false));
    }

    public MessageAdapter(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        Message message = GeneralManager.getInstance(null).getMessageByPosition(position);
        holder.message.setText(message.getText());
        if (message.isMine()) {
            holder.message.setGravity(Gravity.RIGHT);
        }
        else {
            holder.message.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public int getItemCount() {
        return GeneralManager.getInstance(null).getMessagesNumber();
    }
}
