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

public class DecryptFragment extends Fragment {

    Button btnDecrypt;
    Button btnCopy;
    Button btnClear;
    EditText txtCipherText;
    EditText txtMessage;
    Encryption myEncryptor;
    Switch swType;
    NiftyDialogBuilder customDialog, customDialog2;
    String originalMessage="";
    public DecryptFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_decrypt, container, false);

        customDialog = NiftyDialogBuilder.getInstance(DecryptFragment.this.getActivity());
        customDialog2 = NiftyDialogBuilder.getInstance(DecryptFragment.this.getActivity());

        swType = (Switch) myFragmentView.findViewById(R.id.swType);

        btnDecrypt = (Button) myFragmentView.findViewById(R.id.btnDecrypt);
        btnDecrypt.setOnClickListener(new OnClickListenerDecrypt());

        btnCopy = (Button) myFragmentView.findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(new OnClickListenerCopy());

        btnClear = (Button) myFragmentView.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new OnClickListenerClear());

        txtCipherText = (EditText) myFragmentView.findViewById(R.id.txtCipherText);
        txtMessage = (EditText) myFragmentView.findViewById(R.id.txtMessage);
        txtMessage.setKeyListener(null);

        init();

        return myFragmentView;
    }

    private void init() {
        txtCipherText.setText("");
        txtMessage.setText("");
        originalMessage = "";

        if (MainActivity.shareActionProvider != null) MainActivity.shareActionProvider.setShareIntent(null);

        btnCopy.setVisibility(View.GONE);
        txtMessage.requestFocus();
    }


    private class OnClickListenerDecrypt implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(!txtCipherText.getText().toString().trim().isEmpty()){
                String key = Utility.displayKey(DecryptFragment.this.getActivity());
                if (!key.equals("")){
                    try {
                        myEncryptor = new Encryption(swType.isChecked(), key);
                        originalMessage = myEncryptor.decrypt(txtCipherText.getText().toString()).trim();
                        txtMessage.setText(originalMessage);

                        setShareIntent();

                        btnCopy.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else Utility.alert(DecryptFragment.this.getActivity(),"Ingrese una key para decifrar su mensaje",true);
            } else {
                Utility.alert(DecryptFragment.this.getActivity(),"Ingrese mensaje a cifrar",true);
                txtMessage.requestFocus();
            }
        }
    }

    private class OnClickListenerCopy implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ClipData clip = ClipData.newPlainText("text", originalMessage);
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
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Original Message Sharing DESy3DES App");
            shareIntent.putExtra(Intent.EXTRA_TEXT, originalMessage);
            MainActivity.shareActionProvider.setShareIntent(shareIntent);
        }
    }
}
