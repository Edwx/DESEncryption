package pe.edu.utp.desencryption.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.ArrayList;

import pe.edu.utp.desencryption.R;
import pe.edu.utp.desencryption.app.Drawer;
import pe.edu.utp.desencryption.app.DrawerAdapter;
import pe.edu.utp.desencryption.ui.fragment.DecryptFragment;
import pe.edu.utp.desencryption.ui.fragment.EncryptFragment;
import pe.edu.utp.desencryption.ui.fragment.MainFragment;
import pe.edu.utp.desencryption.util.Utility;


public class MainActivity extends AppCompatActivity {
    public static ShareActionProvider shareActionProvider;
    private Toolbar mToolbar;
    private ListView mDrawerList;
    private ArrayList<Drawer> list;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    NiftyDialogBuilder customDialog, customDialog2;

    private Fragment InicioFragment;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Handler mHandler;

    private boolean mShouldFinish = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        customDialog = NiftyDialogBuilder.getInstance(this);
        customDialog2 = NiftyDialogBuilder.getInstance(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mTitle = mDrawerTitle = getTitle();
        mDrawerList = (ListView) findViewById(R.id.list_view);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        prepareNavigationDrawerItems();

        View headerView = getLayoutInflater().inflate(R.layout.nav_header_main, mDrawerList, false);
        mDrawerList.addHeaderView(headerView);
        mDrawerList.setAdapter(new DrawerAdapter(this, list));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        int color = getResources().getColor(R.color.material_grey_100);
        mDrawerList.setBackgroundColor(color);
        mDrawerList.getLayoutParams().width = (int) getResources().getDimension(R.dimen.drawer_width);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mHandler = new Handler();

        if (savedInstanceState == null) {
            int position = 1;
            selectItem(position, list.get(position-1).getId());
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mShouldFinish && !mDrawerLayout.isDrawerOpen(mDrawerList)) {
            Toast.makeText(getApplicationContext(), "Presiona nuevamente retroceder para salir.", Toast.LENGTH_SHORT).show();
            mShouldFinish = true;
            mDrawerLayout.openDrawer(mDrawerList);
        } else if (!mShouldFinish && mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            if (mDrawerList.getCheckedItemPosition()>1) {
                selectItem(1, list.get(0).getId());
                mDrawerLayout.openDrawer(GravityCompat.START);
                mShouldFinish = false;
            }
            else {
                super.onBackPressed();
            }
        }
    }

    private void prepareNavigationDrawerItems() {
        list = new ArrayList<>();
        list.add(new Drawer(0, "", "Home", R.drawable.ic_home_black));
        list.add(new Drawer(1, "", "Encrypt", R.drawable.ic_assignment_black));
        list.add(new Drawer(2, "", "Decrypt", R.drawable.ic_assignment_black));
        list.add(new Drawer(3, "", "Exit", R.drawable.ic_menu_logout_black));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.encrypt, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) return true;
        else if (id == R.id.action_key){
            customDialog
                    .withTitle(null)
                    .withMessageColor("#FFFFFFFF")
                    .withDialogColor(getResources().getColor(R.color.colorPrimary))
                    .withDuration(300)
                    .withEffect(Effectstype.Slidetop)
                    .isCancelableOnTouchOutside(true)
                    .setCustomView(R.layout.text_key, this);

            final TextInputEditText txtKey = (TextInputEditText) customDialog.findViewById(R.id.txtKey);
            txtKey.setText(Utility.displayKey(MainActivity.this));

            customDialog.findViewById(R.id.btnAceptar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtKey.getText().toString().length() > 0) {
                        if (txtKey.getText().toString().length() > 23) {
                            Utility.updateKey(txtKey.getText().toString());
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

    private void logout() {
        customDialog2
                .withTitle("DES y 3DES")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#FFFFFF")                              //def
                .withMessage("Desea salir de la aplicación?")                     //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                .withDialogColor(getResources().getColor(R.color.colorPrimary))                               //def  | withDialogColor(int resid)
                .withDuration(300)                                          //def
                .withEffect(Effectstype.Slidetop)                                         //def Effectstype.Slidetop
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .setCustomView(R.layout.text_close, this);         //.setCustomView(View or ResId,context)

        customDialog2.findViewById(R.id.btnAceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog2.dismiss();
                finish();
            }
        });

        customDialog2.findViewById(R.id.btnCancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog2.dismiss();
            }
        });

        customDialog2.isCancelable(false).show();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) { // because we have header, we skip clicking on it
                selectItem(position, list.get(position-1).getId());
            }

        }
    }

    private void selectItem(int position, int drawerTag) {
        if (position < 1) { // because we have header, we skip clicking on it
            return;
        }

        getFragmentByDrawerTag(drawerTag);

        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void getFragmentByDrawerTag(int drawerTag) {
        InicioFragment = null;
        if (drawerTag == 0) {
            InicioFragment = new MainFragment();
            commitFragment(InicioFragment);
        }
        else if (drawerTag == 1){
            InicioFragment = new EncryptFragment();
            commitFragment(InicioFragment);
        }
        else if (drawerTag == 2) {
            InicioFragment = new DecryptFragment();
            commitFragment(InicioFragment);
        }
        else if (drawerTag == 3){
            logout();
        }
        else {
            InicioFragment = new Fragment();
            commitFragment(InicioFragment);
        }

        mShouldFinish = false;
    }

    public void commitFragment(Fragment fragment) {

        mHandler.post(new CommitFragmentRunnable(fragment));
    }

    private class CommitFragmentRunnable implements Runnable {

        private Fragment fragment;

        public CommitFragmentRunnable(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void run() {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
