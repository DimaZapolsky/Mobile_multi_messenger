package ru.sirius.january.mmm;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.store.StorageManager;

public class DialogActivity extends AppCompatActivity {

    private ImageView iconView;
    private Button sendButton;
    private Button addContentButton;
    private EditText messageEdit;
    private TextView nameView;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        getSupportFragmentManager().beginTransaction().add(R.id.dialog_messages_view, DialogViewFragment.newInstance(1727)).addToBackStack(null).commit();

        dialog = GeneralManager.getInstance(null).getDialogByPosition(0); /// fix later
        iconView = findViewById(R.id.dialog_icon_view);
        sendButton = findViewById(R.id.send_button);
        addContentButton = findViewById(R.id.add_content_button);
        messageEdit = findViewById(R.id.type_message);
        nameView = findViewById(R.id.dialog_name_view);

        iconView.setImageBitmap(StorageManager.getInstance(null).getCachedBitmap(dialog.getImageKey()));
        nameView.setText(dialog.getName());
    }
}
