package de.ur.mi.android.exercises.imagetaker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    String currentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 2;
    Button takePicture;
    ImageView imageView;
    Button loadImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        setupListener();

    }
    private void setupUI() {
        imageView = findViewById(R.id.imageView);
        takePicture = findViewById(R.id.takeImage);
        loadImage = findViewById(R.id.fromGallery);
    }

    private void setupListener() {
        loadImage.setOnClickListener(this);
        takePicture.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.takeImage:
                takePicture();
                break;
            case R.id.fromGallery:
                loadFromGallery();
                break;
        }
    }

    private void takePicture(){

        //Impliziter Intent für Kameraaufnahme
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Überprüfen ob der Intent aufgelöst werden kann
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Erzeugen der Datei, in welche die Kameraaufnahme geschrieben wird
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Nur wenn Datei erfolgreich erzeugt wurde, wird der eigentliche Intent gestartet
            if (photoFile != null) {
                //Erzeugen der URI für die Datei mit Hilfe des File Providers, um die Datei für andere Apps zugänglich zu machen bzw. zu übergeben.
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                // Neben der URI erwartet der Intent noch das EXTRA_OUTPUT, um das Bild in Originalgöße speichern zu können
                // Ohne EXTRA_OUTPUT wird das Bild lediglich als Thumbnail zurückgegeben
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }

    }

    private void saveImageToGallery(Bitmap bitmap) {
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "",
                ""
        );

        // Parsen der Gallery URL in die URI
        Uri savedImageURI = Uri.parse(savedImageURL);
        imageView.setImageURI(savedImageURI);

    }

    //Gallery wird aufgerufen
    private void loadFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    private File createImageFile () throws IOException {
        // Erzeugen eines Dateinames
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Speichern des absoluten Bildpfades
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Callback, der aufgerufen wird, sobald startActivityResult abgeschlossen ist.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Kamera-Applikation wird aufgerufen
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            saveImageToGallery(bitmap);
        }

        //Mit Hilfe der Cursor Klasse wird der Pfad des ausgewählten Images ermittelt
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));


        }

    }


}
