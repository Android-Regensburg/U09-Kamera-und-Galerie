package de.ur.mi.android.imagetaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_LOAD_IMAGE = 2;

    private String currentPhotoPath;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        Button cameraButton = findViewById(R.id.cameraButton);
        Button galleryButton = findViewById(R.id.galleryButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureWithCamera();
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImageFromGallery();
            }
        });
    }

    private void takePictureWithCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Nur wenn Datei erfolgreich erzeugt wurde, wird der eigentliche Intent gestartet
            if (photoFile != null) {
                //Erzeugen der URI für die Datei mit Hilfe des File Providers, um die Datei für andere Apps zugänglich zu machen bzw. zu übergeben.
                Uri photoURI = FileProvider.getUriForFile(this,
                        "de.ur.mi.android.fileprovider",
                        photoFile);
                // Neben der URI erwartet der Intent noch das EXTRA_OUTPUT, um das Bild in Originalgöße speichern zu können
                // Ohne EXTRA_OUTPUT wird das Bild lediglich als Thumbnail zurückgegeben
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }


    private void loadImageFromGallery() {
        Intent loadImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(loadImageIntent, REQUEST_LOAD_IMAGE);
    }

    /*
    Verarbeitung der Ergebnisse der zuvor gestellten Anfrage. Anhand des request codes wird die Art der Anfrage unterschieden,
    durch den result code wird jeweils der Erfolg der Anfrage überprüft.
    Anders als im Fall einer Anfrage an die Galerie, wird für die Erstellung des Bitmap-Objekts bei
    der Nutzung der Kamera nicht auf die Daten zugegriffen, die durch das Intent-Objekt an die Methode übergeben werden.
    Stattdessen wird über den entsprechenden Dateipfad auf die Bilddatei zugegriffen, die vor Aussenden
    des Kamera-Intents erstellt wurde.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ACTIVITY RESULT", "reqCode: " + requestCode + ", resCode: " + resultCode);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, getBitmapOptions());
            imageView.setImageBitmap(bitmap);
        } else if (requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri), null, getBitmapOptions());
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    Erzeugung eines BitmapFactory.Options-Objekts, in dem der Skalierungsfaktor für die spätere Erstellung eines Bitmap-Onjekts gesetzt wird.
    Normalerweise werden Bitmaps in ihrer Originalgröße im Speicher gehalten, auch wenn sie im UI letztendlich kleiner angezeigt werden.
    Um den Speicherverbrauch nur os hoch wie nötig zu halten, werden die Bitmap-Objekte im weiteren Verlauf mithilfe des hier festgelegten
    Sklaierungsfaktors in einer Größe erzeugt, die der letztendlichen Darstellungsgröße in der App entspricht.
     */
    private BitmapFactory.Options getBitmapOptions() {
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        return bmOptions;
    }
}
