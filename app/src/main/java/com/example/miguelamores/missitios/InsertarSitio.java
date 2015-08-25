package com.example.miguelamores.missitios;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.miguelamores.data.SQLHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class InsertarSitio extends Activity {

    SQLiteDatabase data;
    EditText titulo, direccion, email, telefono, valoracion, comentario;
    Button guardar;
    /**
     * Constantes para identificar la acci—n realizada (tomar una fotograf’a
     * o bien seleccionarla de la galer’a)
     */

    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;

    /**
     * Variable que define el nombre para el archivo donde escribiremos
     * la fotograf’a de tama–o completo al tomarla.
     */
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_sitio);

        titulo = (EditText)findViewById(R.id.tituloEditText);
        direccion = (EditText)findViewById(R.id.direccionEditText);
        email = (EditText)findViewById(R.id.emailEditText);
        telefono = (EditText)findViewById(R.id.telefonoEditText);
        valoracion = (EditText)findViewById(R.id.valoracionEditText);
        comentario = (EditText)findViewById(R.id.comentariosEditText);

        name = Environment.getExternalStorageDirectory() + "/"+getCode()+ ".jpg";
        Button tomarFoto = (Button)findViewById(R.id.btnFull);
        Button buscarFoto = (Button)findViewById(R.id.btnGall);
        tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Para todos los casos es necesario un intent, si accesamos la c‡mara con la acci—n
                 * ACTION_IMAGE_CAPTURE, si accesamos la galer’a con la acci—n ACTION_PICK.
                 * En el caso de la vista previa (thumbnail) no se necesita m‡s que el intent,
                 * el c—digo e iniciar la actividad. Por eso inicializamos las variables intent y
                 * code con los valores necesarios para el caso del thumbnail, as’ si ese es el
                 * bot—n seleccionado no validamos nada en un if.
                 */
                Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                int code = TAKE_PICTURE;

                /**
                 * Si es fotograf’a completa, necesitamos un archivo donde
                 * guardarla
                 */
                Uri output = Uri.fromFile(new File(name));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                startActivityForResult(intent, code);
            }
        });

        buscarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                int code = SELECT_PICTURE;
                startActivityForResult(intent, code);
            }
        });

        guardar = (Button)findViewById(R.id.btnGuardar);
        SQLHelper helper = new SQLHelper(InsertarSitio.this);
        data = helper.getWritableDatabase();
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(titulo.getText().toString() == ""){
                    Toast.makeText(InsertarSitio.this,"Ingrese al menos el nombre del sitio", Toast.LENGTH_LONG).show();

                }
                else
                {
                    ContentValues a = new ContentValues();
                    a.put("nombre", titulo.getText().toString());
                    a.put("foto", name);
                    a.put("direccion", direccion.getText().toString());
                    a.put("email", email.getText().toString());
                    a.put("telefono", telefono.getText().toString());
                    a.put("valoracion", valoracion.getText().toString());
                    a.put("comentarios", comentario.getText().toString());
                    data.insert("sitio", null, a);
                    Toast.makeText(InsertarSitio.this,"El lugar "+titulo.getText().toString()+" ha sido ingresado", Toast.LENGTH_LONG).show();
                    startService(new Intent(InsertarSitio.this, Servicio.class));

                }


            }
        });

    }

    /**
     60  * Metodo privado que genera un codigo unico segun la hora y fecha del sistema
     61  * @return photoCode
     62  * */
    @SuppressLint("SimpleDateFormat")
    private String getCode()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date() );
        String photoCode = "pic_" + date;
        return photoCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        /**
         * Se revisa si la imagen viene de la c‡mara (TAKE_PICTURE) o de la galer’a (SELECT_PICTURE)
         */
        if (requestCode == TAKE_PICTURE) {
            /**
             * Si se reciben datos en el intent tenemos una vista previa (thumbnail)
             */

            /**
             * A partir del nombre del archivo ya definido lo buscamos y creamos el bitmap
             * para el ImageView
             */
            ImageView iv = (ImageView)findViewById(R.id.imgView);
            iv.setImageBitmap(BitmapFactory.decodeFile(name));
            /**
             * Para guardar la imagen en la galer’a, utilizamos una conexi—n a un MediaScanner
             */
            new MediaScannerConnection.MediaScannerConnectionClient() {
                private MediaScannerConnection msc = null; {
                    msc = new MediaScannerConnection(getApplicationContext(), this); msc.connect();
                }
                public void onMediaScannerConnected() {
                    msc.scanFile(name, null);
                }
                public void onScanCompleted(String path, Uri uri) {
                    msc.disconnect();
                }
            };

            /**
             * Recibimos el URI de la imagen y construimos un Bitmap a partir de un stream de Bytes
             */
        } else if (requestCode == SELECT_PICTURE){
            Uri selectedImage = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                ImageView iv = (ImageView)findViewById(R.id.imgView);
                iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {}
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.insertar_sitio, menu);
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
