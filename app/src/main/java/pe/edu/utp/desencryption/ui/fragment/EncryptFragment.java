package pe.edu.utp.desencryption.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import pe.edu.utp.desencryption.R;
import pe.edu.utp.desencryption.app.Encryption;
import pe.edu.utp.desencryption.ui.MainActivity;
import pe.edu.utp.desencryption.util.Utility;

public class EncryptFragment extends Fragment {

    Button btnEncrypt;
    Button btnCopy;
    Button btnClear;
    EditText txtMessage;
    EditText txtCipherText;
    Encryption myEncryptor;
    Switch swType;
    NiftyDialogBuilder customDialog, customDialog2;
    String cipherMessage="";
    public EncryptFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_encrypt, container, false);

        this.getActivity().setTitle("Cifrar");

        customDialog = NiftyDialogBuilder.getInstance(EncryptFragment.this.getActivity());
        customDialog2 = NiftyDialogBuilder.getInstance(EncryptFragment.this.getActivity());

        swType = (Switch) myFragmentView.findViewById(R.id.swType);

        btnEncrypt = (Button) myFragmentView.findViewById(R.id.btnEncrypt);
        btnEncrypt.setOnClickListener(new OnClickListenerCrypt());

        btnCopy = (Button) myFragmentView.findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(new OnClickListenerCopy());

        btnClear = (Button) myFragmentView.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new OnClickListenerClear());

        txtMessage = (EditText) myFragmentView.findViewById(R.id.txtMessage);
        txtCipherText = (EditText) myFragmentView.findViewById(R.id.txtCipherText);
        txtCipherText.setKeyListener(null);

        init();

        return myFragmentView;
    }

    private void init() {
        txtMessage.setText("");
        txtCipherText.setText("");
        cipherMessage = "";
        clearClipBoard();

        if (MainActivity.shareActionProvider != null) MainActivity.shareActionProvider.setShareIntent(null);

        btnCopy.setVisibility(View.GONE);
        txtMessage.requestFocus();
    }


    private class OnClickListenerCrypt implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(!txtMessage.getText().toString().trim().isEmpty()){
                String key = Utility.displayKey(EncryptFragment.this.getActivity());
                if (!key.equals("")){
                    try {
                        myEncryptor = new Encryption(swType.isChecked(), Utility.displayKey(EncryptFragment.this.getActivity()));
                        cipherMessage = myEncryptor.encrypt(txtMessage.getText().toString()).trim();
                        txtCipherText.setText(cipherMessage);

                        setShareIntent();

                        btnCopy.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else Utility.alert(EncryptFragment.this.getActivity(),"Ingrese una key para cifrar su mensaje",true);
            } else {
                Utility.alert(EncryptFragment.this.getActivity(),"Ingrese mensaje a cifrar",true);
                txtMessage.requestFocus();
            }
        }
    }

    private class OnClickListenerCopy implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ClipData clip = ClipData.newPlainText("text", cipherMessage);
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(clip);

            Utility.alert(getActivity(),"Texto copiado a portapapeles", false);
        }
    }

    private class OnClickListenerClear implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            init();
            clearClipBoard();
        }
    }

    private void clearClipBoard(){
        ClipData clip = ClipData.newPlainText("text", "");
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clip);
    }

    private void setShareIntent() {
        if(MainActivity.shareActionProvider != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Msg Crypted Sharing DESy3DES App");
            shareIntent.putExtra(Intent.EXTRA_TEXT, cipherMessage);
            MainActivity.shareActionProvider.setShareIntent(shareIntent);
        }
    }
}
