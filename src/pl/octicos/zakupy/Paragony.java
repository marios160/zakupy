package pl.octicos.zakupy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Paragony extends Activity {

	private File plik;
	private File paragony;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paragony);
		File f = Environment.getExternalStorageDirectory();
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Zakupy"+File.separator );
        if (!folder.exists()) {
            folder.mkdir();
        }
        this.paragony = folder;
       
	}
	
	public void zdjecie(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
        plik = new File(paragony.getAbsolutePath(), timeStamp + ".jpg");
        Uri uriSavedImage = Uri.fromFile(plik);
        
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	public void galeria(View view) {
		
		Intent intent = new Intent();  
		intent.setAction(android.content.Intent.ACTION_VIEW);  
		intent.setType("image/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.paragony, menu);
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
