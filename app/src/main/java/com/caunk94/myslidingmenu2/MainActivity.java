package com.caunk94.myslidingmenu2;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DBManager dm;
    private Context context;
    Cursor cursor;
    private SimpleCursorAdapter adapter;
    //private CustomAdapter customAdapter;
    private final static String TAG= MainActivity.class.getName().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //untuk menampilkan icon search
        SearchView searchView = (SearchView)menu.findItem(R.id.item_search).getActionView();
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "onQueryTextSubmit ");
                cursor=dm.getStudentListByKeyword(s);
                if (cursor==null){
                    Toast.makeText(MainActivity.this, "No records found!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, cursor.getCount() + " records found!",Toast.LENGTH_LONG).show();
                }


                adapter.setFilterQueryProvider(new FilterQueryProvider() {
                    public Cursor runQuery(CharSequence constraint) {
                        return dm.getStudentListByKeyword(constraint.toString());
                    }
                });
                adapter.swapCursor(cursor);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "onQueryTextChange ");
                cursor=dm.getStudentListByKeyword(s);
                if (cursor!=null){
                    adapter.swapCursor(cursor);
                }
                return false;
            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //coding untuk proses data

    @Override
    protected void onResume() {
        super.onResume();
        displayData();//untuk merefresh tampilan data yang ada di listview secara otomatis
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dm.close();
    }

    private void initial(){
        dm = new DBManager(this);
        dm.Open();
    }

    public void OnclickInsert(View v){

        Intent intent = new Intent(this, DataProcurement.class);
        intent.putExtra("STATE", 0);
        startActivity(intent);
    }

    private void displayData(){
        String []from = {
                DBHelper.COLUMN_ID,
                DBHelper.COLUMN_IDPAYMENT,
                DBHelper.COLUMN_TANGGALPAYMENT,
                DBHelper.COLUMN_SUPPLIER,
                DBHelper.COLUMN_TOTALBAYAR
        };
        int []to ={R.id.textId,
                R.id.textIdPaymnt,
                R.id.textTanggalPymn,
                R.id.textSuplier,
                R.id.textTotalB};
        Cursor c= dm.getListSiswaAsCursor();
        adapter = new SimpleCursorAdapter(this, R.layout.item_data, c, from, to);
        ListView listview = (ListView)findViewById(R.id.listView);
        listview.setAdapter(adapter);
    }

    private AdapterView.OnItemLongClickListener itemLongClick = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int pos, long id) {
            final long _id = id;
            final Dialog dlg = new Dialog(context);
            dlg.setTitle("Aksi");
            dlg.setContentView(R.layout.optionlayout);
            Button buttonEdit = (Button)dlg.findViewById(R.id.buttonEdit);
            Button buttonDelete = (Button)dlg.findViewById(R.id.buttonHapus);

            android.view.View.OnClickListener editListener = new android.view.View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DataProcurement.class);
                    intent.putExtra("ID", _id);
                    intent.putExtra("STATE", 1);
                    dlg.hide();
                    startActivity(intent);
                }
            };
            buttonEdit.setOnClickListener(editListener);

            android.view.View.OnClickListener deleteListener = new android.view.View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder confirm = new AlertDialog.Builder(context);
                    confirm.setTitle("Konfirmasi Hapus Data");
                    confirm.setMessage("APakah Pan Di Hapus ?");
                    confirm.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dm.delete(_id);
                            displayData();
                        }
                    });
                    confirm.setNegativeButton("Ora", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.cancel();
                        }
                    });
                    dlg.hide();
                    confirm.show();
                }
            };

            buttonDelete.setOnClickListener(deleteListener);

            dlg.show();
            return true;
        }
    };



}
