package com.example.miguelamores.missitios;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguelamores.data.SQLHelper;

public class MostrarSitioDetalle extends Activity {

    TextView nombre, id, direccion, email, telefono, valoracion, comentarios;
    ImageView iv;
    SQLiteDatabase data;
    String imgPath;
    Button compartir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_sitio_detalle);

        nombre = (TextView)findViewById(R.id.nombreTextView);
        direccion = (TextView)findViewById(R.id.direccionTextView);
        email = (TextView)findViewById(R.id.emailTextView);
        telefono = (TextView)findViewById(R.id.telefonoTextView);
        valoracion = (TextView)findViewById(R.id.valoracionTextView);
        comentarios = (TextView)findViewById(R.id.comentarioTextView);
        iv = (ImageView)findViewById(R.id.imgView);
        Intent obtener = getIntent();
        obtener.getExtras().getString("nombre2");

        SQLHelper helper = new SQLHelper(this);
        data = helper.getWritableDatabase();
        Cursor cursor;
        cursor = data.query("sitio", new String[]{"*"},"nombre = ?", new String[]{obtener.getExtras().getString("nombre2")},null,null,null);
        if(cursor.moveToFirst()){
            do {
                nombre.setText(cursor.getString(1));

                imgPath = cursor.getString(2);
                //iv.setImageURI(Uri.fromFile(new File(cursor.getString(2))));
                iv.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(2)));
                direccion.setText("Direccion: " + cursor.getString(3));
                email.setText("Email: " + cursor.getString(4));
                telefono.setText("Telefono: " + cursor.getString(5));
                valoracion.setText("Valoracion: " + cursor.getString(6));
                comentarios.setText("Comentarios: " + cursor.getString(7));

            }
            while (cursor.moveToNext());
        }
        cursor.close();

        compartir = (Button)findViewById(R.id.compartirButton);
        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCompartir = new Intent(Intent.ACTION_SEND);
                Uri uriToImage = Uri.parse(imgPath);
                intentCompartir.putExtra(Intent.EXTRA_STREAM, uriToImage);
                intentCompartir.setType("image/jpeg");
                intentCompartir.setType("text/plain");
                intentCompartir.putExtra(Intent.EXTRA_SUBJECT, "Has visitado un nuevo lugar!");
                intentCompartir.putExtra(Intent.EXTRA_TEXT,  "Lugar: " + nombre.getText().toString() +
                        "\n" + direccion.getText().toString() +
                        "\n" + email.getText().toString() +
                        "\n" + telefono.getText().toString() +
                        "\n" + valoracion.getText().toString() +
                        "\n" + comentarios.getText().toString());

                startActivity(Intent.createChooser(intentCompartir , "Título de la Ventana"));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.btConfig:
                Toast.makeText(getApplicationContext(), "Has escogido modificar el sitio",Toast.LENGTH_SHORT).show();
                Intent irModificar = new Intent(MostrarSitioDetalle.this, ModificarSitio.class);
                irModificar.putExtra("nombre", nombre.getText());
                irModificar.putExtra("foto", imgPath);
                irModificar.putExtra("direccion", direccion.getText());
                irModificar.putExtra("email", email.getText());
                irModificar.putExtra("telefono", telefono.getText());
                irModificar.putExtra("valoracion", valoracion.getText());
                irModificar.putExtra("comentarios", comentarios.getText());
                startActivity(irModificar);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
