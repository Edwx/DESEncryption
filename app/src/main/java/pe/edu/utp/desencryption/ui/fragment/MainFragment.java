package pe.edu.utp.desencryption.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.edu.utp.desencryption.R;
import pe.edu.utp.desencryption.ui.MainActivity;

/**
 * Fragmento para la sección de "Inicio"
 */
public class MainFragment extends Fragment {
    public MainFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        this.getActivity().setTitle("Inicio");

        if (MainActivity.shareActionProvider != null) MainActivity.shareActionProvider.setShareIntent(null);
        return view;
    }
}
