package com.pedoran.pwpbsesi13.TextToPdf;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pedoran.pwpbsesi13.BuildConfig;
import com.pedoran.pwpbsesi13.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class ITextLibraryExample extends AppCompatActivity {
    private static final String TAG = "PdfCreatorActivity";
    private EditText mContentEditText;
    private Button mCreateButton;
    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_text_library_example);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContentEditText = findViewById(R.id.edit_text_content);
        mCreateButton = findViewById(R.id.button_create);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContentEditText.getText().toString().isEmpty()) {
                    mContentEditText.setError("Isi Tulisan Terlebih Dahulu");
                    mContentEditText.requestFocus();
                    return;
                }
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {
        int hasilWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasilWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOkCancel("You Need to Allow Access to Storage", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        }
                    });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOkCancel(String message,DialogInterface.OnClickListener okListener){
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK",okListener)
                .setNegativeButton("Keluar",null)
                .create()
                .show();
    }

    private void createPdf() throws DocumentException, FileNotFoundException {
        File dlFolder = new File(Environment.getExternalStorageDirectory()+"/Download");
        if(!dlFolder.exists()){
            dlFolder.mkdir();
            Log.i(TAG,"Buat Folder Tag Baru");
        }
        pdfFile = new File(dlFolder.getAbsolutePath(),"PWPBSesi13-generated.pdf");
        OutputStream outputStream = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document,outputStream);
        document.open();
        document.add(new Paragraph(mContentEditText.getText().toString()));
        document.close();
        previewPdf();
    }

    private void previewPdf(){
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent,PackageManager.MATCH_DEFAULT_ONLY);
        if(list.size() > 0){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
//            Uri uri = Uri.fromFile(pdfFile);
            Uri uri = FileProvider.getUriForFile(this, "com.pedoran.pwpbsesi13.provider",pdfFile);
            intent.setDataAndType(uri,"application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Download Aplikasi Pdf Viewer Untuk Melihat Hasil Generate!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
