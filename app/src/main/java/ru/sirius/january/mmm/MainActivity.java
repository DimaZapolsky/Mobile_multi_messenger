package ru.sirius.january.mmm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.vk.sdk.VKAccessToken;

import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.store.StorageManager;
import ru.sirius.january.mmm.vk.VKHelper;
import ru.sirius.january.mmm.vk.VkAuthActivity;

public class MainActivity extends AppCompatActivity {

    private void setChooseAccountScreen() {
        setContentView(R.layout.choose_social);
        final Intent authVK = new Intent(this, VkAuthActivity.class);
        findViewById(R.id.vk_auth_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(authVK);
            }
        });
    }

    /*private ArrayList<Dialog> getTestDialogs() {

        ArrayList<Message> messages = new ArrayList<>();
        Message msg = new Message();
        msg.setText("Hello hahahha i am stupid");
        messages.add(msg);

        ArrayList<Message> messages1 = new ArrayList<>();
        Message msg1 = new Message();
        msg1.setText("Hi azazza keklollolkek");
        messages1.add(msg1);

        String KEY1 = "image1CAHCHE";
        try {
            StorageManager.getInstance(null).cacheBitmap(KEY1,
                    BitmapFactory.decodeStream(getAssets().open("image3.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String KEY2 = "image2CAHCHE";
        try {
            StorageManager.getInstance(null).cacheBitmap(KEY2,
                    BitmapFactory.decodeStream(getAssets().open("russia.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }


        Dialog dialog = new Dialog(messages, "DK 47", KEY1);
        Dialog dialog1 = new Dialog(messages1, "KATAL", KEY2);

        ArrayList<Dialog> dialogs = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            dialogs.add(dialog);
            dialogs.add(dialog1);
        }

        return dialogs;
    }*/


    DialogListFragment dialogListFragment;

    private void setMainScreen() {
        getSupportActionBar().show();
        setContentView(R.layout.activity_main);
        findViewById(R.id.add_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialogListFragment = DialogListFragment.newInstance(new DialogListFragment.OnItemClickListener() {
            @Override
            public void onItemClicked(Dialog dialog) {
                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                intent.putExtra(DialogActivity.DIALOG_TAG, dialog.getUniqueID());
                startActivity(intent);
            }
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.dialogs_rv, dialogListFragment).commit();;
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKAccessToken vkAccessToken = VKAccessToken.tokenFromFile(getApplicationContext()
                .getCacheDir().getAbsolutePath() + "/vkToken");
        if (vkAccessToken == null) {
            setChooseAccountScreen();
        } else {
            try {
                GeneralManager.getInstance(getApplicationContext()).startVKTracking();
            } catch (VKHelper.VKUnauthorizedException e) {
                e.printStackTrace();
            }
            setMainScreen();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StorageManager.getInstance(getApplicationContext());
        try {
            GeneralManager.getInstance(getApplicationContext()).startVKTracking();
            GeneralManager.getInstance(getApplicationContext()).setDialogsCallback(dialogListFragment);
        } catch (VKHelper.VKUnauthorizedException e) {
            e.printStackTrace();
        }
        getSupportActionBar().hide();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GeneralManager.getInstance(getApplication()).stopVKTracking();
        GeneralManager.getInstance(getApplicationContext()).setDialogsCallback(null);
    }
}
