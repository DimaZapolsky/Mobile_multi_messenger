package ru.sirius.january.mmm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import ru.sirius.january.mmm.data.abstracts.Dialog;

public class DialogViewFragment extends Fragment {

    private static final String CHAT_ID_TAG = "CHAT";

    private RecyclerView messagesView;

    private int chatId;
    private Dialog dialog;

    public DialogViewFragment() {}

    public static DialogViewFragment newInstance(int chatId) {
        DialogViewFragment fragment = new DialogViewFragment();
        Bundle args = new Bundle();
        args.putInt(CHAT_ID_TAG, chatId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        messagesView = view.findViewById(R.id.dialog_messages_view);

        MessageAdapter adapter = new MessageAdapter(dialog.getMessages());
        messagesView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_view, container, false);
    }
}
