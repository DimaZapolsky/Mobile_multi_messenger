package ru.sirius.january.mmm;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.sirius.january.mmm.data.abstracts.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    ArrayList<Message> messages;

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

    public MessageAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        holder.message.setText(messages.get(position).getText());
        if (messages.get(position).isMine()) {
            holder.message.setGravity(Gravity.RIGHT);
        }
        else {
            holder.message.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
