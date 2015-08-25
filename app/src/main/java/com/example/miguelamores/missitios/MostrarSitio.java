package com.example.miguelamores.missitios;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.miguelamores.data.SQLHelper;

import java.util.ArrayList;

public class MostrarSitio extends Activity {
    SQLiteDatabase data;
    ListView ver;
    String miItem;
    SQLHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_sitio);

        helper = new SQLHelper(this);
        data = helper.getWritableDatabase();
        ver = (ListView)findViewById(R.id.listView);

        Cursor cursor;
        cursor = data.query("sitio", new String[]{"nombre, _id"}, null, null, null, null, null);
        final ArrayList<String> sitios = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do {
                sitios.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        final ArrayAdapter<String> sitiosAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sitios);
        ver.setAdapter(sitiosAdapter);

        ver.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MostrarSitio.this, "Has seleccionado el sitio "+ sitios.get(position), Toast.LENGTH_LONG).show();
                miItem = parent.getAdapter().getItem(position).toString();
                registerForContextMenu(ver);
                return false;
            }
        });



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuetiqueta, menu);
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle(ver.getAdapter().getItem(info.position).toString());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcVer:

                //Toast.makeText(MostrarSitio.this, "Modificacion", Toast.LENGTH_LONG).show();
                Intent irDetalle = new Intent(MostrarSitio.this, MostrarSitioDetalle.class);
                irDetalle.putExtra("nombre2",miItem);
                startActivity(irDetalle);
                return true;
            case R.id.opcEliminar:
                helper = new SQLHelper(MostrarSitio.this);
                data = helper.getWritableDatabase();
                data.delete("sitio","nombre = ?", new String[]{miItem});
                Toast.makeText(MostrarSitio.this, "Sitio eliminado!", Toast.LENGTH_LONG).show();
                return true;
            default:
                //return super.onContextItemSelected(item);
        }
        //return super.onContextItemSelected(item);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mostrar_sitio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
