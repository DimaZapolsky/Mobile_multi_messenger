package ru.sirius.january.mmm;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.data.abstracts.Message;
import ru.sirius.january.mmm.store.StorageManager;

public class DialogActivity extends AppCompatActivity {

    private ImageView iconView;
    private Button sendButton;
    private Button addContentButton;
    private EditText messageEdit;
    private TextView nameView;
    private Dialog dialog;

    public static final String DIALOG_TAG = "DIALOG_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        getSupportActionBar().hide();
        int id = 0;
        id = getIntent().getIntExtra(DIALOG_TAG, -1);
        getSupportFragmentManager().beginTransaction().add(R.id.dialog_messages_view, DialogViewFragment.newInstance(id)).commit();

        dialog = GeneralManager.getInstance(null).getChatByID(id); /// fix later
        iconView = findViewById(R.id.dialog_icon_view);
        sendButton = findViewById(R.id.send_button);
        addContentButton = findViewById(R.id.add_content_button);
        messageEdit = findViewById(R.id.type_message);
        nameView = findViewById(R.id.dialog_name_view);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageEdit.getText().toString().length() > 0) {
                    GeneralManager.getInstance(null).sendMessage(dialog, messageEdit.getText().toString(), null);
                    messageEdit.setText("");
                }
            }
        });
        Picasso.with(getApplicationContext()).load(dialog.getImageKey()).into(iconView);
        //iconView.setImageBitmap(StorageManager.getInstance(null).getCachedBitmap(dialog.getImageKey()));
        //Picasso.with(getApplicationContext()).load("http://is1.mzstatic.com/image/thumb/Purple5/v4/12/a1/d1/12a1d127-6f0e-23a5-0b21-cc3b442d3e8c/source/1200x630bb.jpg").into(iconView);
        nameView.setText(dialog.getName());
    }
}
