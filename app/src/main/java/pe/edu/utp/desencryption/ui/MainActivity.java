package pe.edu.utp.desencryption.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import pe.edu.utp.desencryption.R;
import pe.edu.utp.desencryption.app.Encryption;
import pe.edu.utp.desencryption.app.Utility;

public class MainActivity extends AppCompatActivity {

    Button btnCrypt;
    Button btnDecrypt;
    Button btnClear;
    EditText txtMessage;
    EditText txtKey;
    EditText txtCrypt;
    EditText txtDecrypt;
    Encryption myEncryptor;
    Switch swType;
    NiftyDialogBuilder customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getTitle());
        }

        customDialog = NiftyDialogBuilder.getInstance(this);

        swType = (Switch) findViewById(R.id.swType);

        btnCrypt = (Button) findViewById(R.id.btnCrypt);
        btnCrypt.setOnClickListener(new OnClickListenerCrypt());

        btnDecrypt = (Button) findViewById(R.id.btnDecrypt);
        btnDecrypt.setOnClickListener(new OnClickListenerDecrypt());
        btnDecrypt.setEnabled(false);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new OnClickListenerClear());

        txtMessage = (EditText) findViewById(R.id.txtMessage);
        //txtKey = (EditText) findViewById(R.id.txtKey);
        txtCrypt = (EditText) findViewById(R.id.txtCrypt);
        txtCrypt.setKeyListener(null);
        txtDecrypt = (EditText) findViewById(R.id.txtDecrypt);
        txtDecrypt.setKeyListener(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_key) {

            customDialog
                    .withTitle(null)                                  //.withTitle(null)  no title
                    //.withTitleColor("#FFFFFF")                                  //def
                    //.withDividerColor("#FFFFFF")                              //def
                    //.withMessage("Si descalifica al astablecimiento, esta ficha se guardará y no podrá modificarla.")                     //.withMessage(null)  no Msg
                    .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                    .withDialogColor(getResources().getColor(R.color.colorPrimary))                               //def  | withDialogColor(int resid)
                    .withDuration(300)                                          //def
                    .withEffect(Effectstype.Slideleft)                                         //def Effectstype.Slidetop
                    .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                    .setCustomView(R.layout.text_key, this);         //.setCustomView(View or ResId,context)


            final TextInputEditText txtKey = (TextInputEditText) customDialog.findViewById(R.id.txtKey);
            txtKey.setText(Utility.myStaticEncryptionKey);

            customDialog.findViewById(R.id.btnAceptar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtKey.getText().toString().length() > 0) {
                        if (txtKey.getText().toString().length() > 24) {
                            Utility.myStaticEncryptionKey = txtKey.getText().toString();
                            reload();
                            customDialog.dismiss();
                        }
                        else Utility.alert(MainActivity.this, "Caractéres mínimos: 24", true);
                    }
                    else Utility.alert(MainActivity.this, "Ingrese key", true);
                }
            });

            customDialog.findViewById(R.id.btnCancelar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog.dismiss();
                }
            });

            customDialog.isCancelable(false).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class OnClickListenerCrypt implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(!txtMessage.getText().toString().trim().isEmpty()){
                try {
                    myEncryptor = new Encryption(swType.isChecked());
                    txtCrypt.setText(myEncryptor.encrypt(txtMessage.getText().toString()).trim());
                    btnDecrypt.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class OnClickListenerDecrypt implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            txtDecrypt.setText(myEncryptor.decrypt(txtCrypt.getText().toString()));
        }
    }

    private class OnClickListenerClear implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            reload();
        }
    }

    private void reload() {
        txtCrypt.setText("");
        txtDecrypt.setText("");

        btnDecrypt.setEnabled(false);
        txtMessage.requestFocus();
    }
}
