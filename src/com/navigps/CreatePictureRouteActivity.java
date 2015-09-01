package com.navigps;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.navigps.R.id;
import com.navigps.providers.PreferencesProvider;
import com.navigps.services.UsersService;
import com.navigps.tools.Globals;
import com.navigps.tools.TransparentProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

public class CreatePictureRouteActivity extends Activity {
	private TransparentProgressDialog progressDialog;
	private PreferencesProvider preferencesProvider;
	
	private ImageView ivTraceImage;
	private Button btnSavePicture;
	private Button btnSkipPicture;
	
	
	private String picturePath = null;
	private String upLoadServerUri = null;
	private int serverResponseCode = 0;
	private String routeId = null;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_create_picture);
		ivTraceImage = (ImageView) findViewById(id.selectImage);
		btnSavePicture = (Button) findViewById(id.btnNewPicture);
		btnSkipPicture = (Button) findViewById(id.btnSkipPicture);

		ivTraceImage.setOnClickListener(traceImageListener);
		btnSavePicture.setOnClickListener(savePictureListener);
		btnSkipPicture.setOnClickListener(skipPictureListener);
		
		routeId = getIntent().getStringExtra(Globals.DEFINED_ROUTE_MAP.TAG_ROUTE_ID);
		upLoadServerUri = "http://www.navigps.esy.es/menu/UploadToServer.php";
		preferencesProvider = new PreferencesProvider(this);
		progressDialog = new TransparentProgressDialog(this, R.drawable.progress);
	}
	
	private OnClickListener traceImageListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, 1);
		}
	};
	
	private OnClickListener savePictureListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (!isNetworkOnline()) {
				Toast.makeText(getBaseContext(), "Uruchom dane mobilne", Toast.LENGTH_SHORT).show();
			} else {
				new SaveDataDescription().execute();
			}
			
		}
	};
	
	private OnClickListener skipPictureListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			finish();			
		}
	};
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			ivTraceImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

		}
	}
	
	private boolean isNetworkOnline() {
    	return preferencesProvider.isNetworkOnline();
    }
	
	private class SaveDataDescription extends AsyncTask<String, String, Boolean> {
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
	    }

		protected Boolean doInBackground(String... args) {
			return true;
		}

		protected void onPostExecute(Boolean saved) {
			if(saved && picturePath != null){
				new Thread(new Runnable() {
	                public void run() {
	               	 uploadFile(picturePath);                            
	                }
	              }).start(); 
			}
		}
	}
	
	public int uploadFile(String sourceFileUri) {
    	//String fileName = sourceFileUri;
    	
    	//fileName += "_navigps_" + String.valueOf(UsersService.getInstance().getUser().getUserId()) + "_" + routeId;
    	
    	HttpURLConnection conn = null;
    	DataOutputStream dos = null;  
    	String lineEnd = "\r\n";
    	String twoHyphens = "--";
    	String boundary = "*****";
    	int bytesRead, bytesAvailable, bufferSize;
    	byte[] buffer;
    	int maxBufferSize = 1 * 1024 * 1024; 
    	File sourceFile = new File(sourceFileUri); 
      
    	if (!sourceFile.isFile()) {
    		progressDialog.dismiss(); 
            Log.e("uploadFile", "Source File not exist :"+picturePath);
        
	        runOnUiThread(new Runnable() {
	            public void run() {
	            }
	        }); 
        
	        return 0;
       
    	} else {
	        try { 
	         
	           // open a URL connection to the Servlet
	            FileInputStream fileInputStream = new FileInputStream(sourceFile);
	            URL url = new URL(upLoadServerUri);
	            
	            // Open a HTTP  connection to  the URL
	            conn = (HttpURLConnection) url.openConnection(); 
	            conn.setDoInput(true); // Allow Inputs
	            conn.setDoOutput(true); // Allow Outputs
	            conn.setUseCaches(false); // Don't use a Cached Copy
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	            conn.setRequestProperty("uploaded_file", sourceFileUri); 
	            
	            dos = new DataOutputStream(conn.getOutputStream());
	  
	            dos.writeBytes(twoHyphens + boundary + lineEnd); 
	            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
	                                + sourceFileUri + "\"" + lineEnd);
	            
	            dos.writeBytes(lineEnd);
	  
	            // create a buffer of  maximum size
	            bytesAvailable = fileInputStream.available(); 
	  
	            bufferSize = Math.min(bytesAvailable, maxBufferSize);
	            buffer = new byte[bufferSize];
	  
	            // read file and write it into form...
	            bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
	              
	            while (bytesRead > 0) {
	             
	              dos.write(buffer, 0, bufferSize);
	              bytesAvailable = fileInputStream.available();
	              bufferSize = Math.min(bytesAvailable, maxBufferSize);
	              bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
	              
	             }
	  
	            // send multipart form data necesssary after file data...
	            dos.writeBytes(lineEnd);
	            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	  
	            // Responses from the server (code and message)
	            serverResponseCode = conn.getResponseCode();
	            String serverResponseMessage = conn.getResponseMessage();
	             
	            Log.i("uploadFile", "HTTP Response is : " 
	              + serverResponseMessage + ": " + serverResponseCode);
	            
	            if(serverResponseCode == 200){
	             
	                runOnUiThread(new Runnable() {
	                     public void run() {
	                         Toast.makeText(CreatePictureRouteActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
	                     }
	                 });                
	            }    
	            
	            //close the streams //
	            fileInputStream.close();
	            dos.flush();
	            dos.close();
	             
	       } catch (MalformedURLException ex) {
	        
	    	   progressDialog.dismiss();  
	           ex.printStackTrace();
	           
	           runOnUiThread(new Runnable() {
	               public void run() {
	                   Toast.makeText(CreatePictureRouteActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
	               }
	           });
	           
	           Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
	       } catch (Exception e) {
	        
	    	   progressDialog.dismiss();  
	           e.printStackTrace();
	           
	           runOnUiThread(new Runnable() {
	               public void run() {
	                   Toast.makeText(CreatePictureRouteActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
	               }
	           });
	           Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);  
	       }
	       progressDialog.dismiss();   
	       
	       return serverResponseCode; 
	       
	       } // End else block 
     }
}
