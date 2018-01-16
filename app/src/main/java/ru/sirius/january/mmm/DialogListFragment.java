package ru.sirius.january.mmm;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class DialogListFragment extends Fragment {

    interface OnItemClickListenner {
        void onItemClicked(Dialog dialog);
    }

    interface OnAddDialog {
        void onAddDialog(Dialog dialog);
    }

    public DialogListFragment() {}

    private ArrayList<Dialog> dialogs;
    private OnItemClickListenner callback;
    private DialogsAdapter dialogsAdapter;

    void addDialog(Dialog dialog) { dialogsAdapter.addDialog(dialog); }

    public static DialogListFragment newInstance(ArrayList<Dialog> dialogs, OnItemClickListenner callback) {
        DialogListFragment dialogListFragment = new DialogListFragment();
        dialogListFragment.dialogs = dialogs;
        dialogListFragment.callback = callback;
        return dialogListFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.dialogs_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dialogsAdapter = new DialogsAdapter(dialogs, callback);
        recyclerView.setAdapter(dialogsAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_list, container, false);
    }

}
