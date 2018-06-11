package com.example.littleprince.Upload;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.littleprince.Upload.MyMultipartEntity.ProgressListener;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class HttpUpload extends AsyncTask<Void, Integer, Void> {
	
	private Context context;
	private String filePath;
	
	private HttpClient client;
	
	private ProgressDialog pd;
	private long totalSize;
	
	private static final String url = "http://39.106.150.248:3000/upload";
	
	public HttpUpload(Context context, String filePath) {
		super();
		this.context = context;
		this.filePath = filePath;
	}

	@Override
	protected void onPreExecute() {
		//Set timeout parameters
		int timeout = 10000;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
		HttpConnectionParams.setSoTimeout(httpParameters, timeout);

		//We'll use the DefaultHttpClient
		client = new DefaultHttpClient(httpParameters);
		
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("图片上传中...");
		pd.setCancelable(false);
		pd.show();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			File file = new File(filePath);

			//Create the POST object
			HttpPost post = new HttpPost(url);
			
			//Create the multipart entity object and add a progress listener
			//this is a our extended class so we can know the bytes that have been transfered
			MultipartEntity entity = new MyMultipartEntity(new ProgressListener()
			{
				@Override
				public void transferred(long num)
				{
					//Call the onProgressUpdate method with the percent completed
					publishProgress((int) ((num / (float) totalSize) * 100));
					Log.d("DEBUG", num + " - " + totalSize);
				}
			});
			//Add the file to the content's body
			ContentBody cbFile = new FileBody( file, "image/jpeg" );
			entity.addPart("source", cbFile);
			
			// 填充字段
	        entity.addPart("userid", new StringBody("u30018512", Charset.forName("UTF-8")));
	        entity.addPart("username", new StringBody("中文不乱码", Charset.forName("UTF-8")));
			
			//After adding everything we get the content's lenght
			totalSize = entity.getContentLength();
			
			//We add the entity to the post request
			post.setEntity(entity);
			
			//Execute post request
		    HttpResponse response = client.execute( post );
			int statusCode = response.getStatusLine().getStatusCode();
		    
		    if(statusCode == HttpStatus.SC_OK){
		    	//If everything goes ok, we can get the response
				String fullRes = EntityUtils.toString(response.getEntity());
				Log.d("DEBUG", fullRes);
				
			} else {
				Log.d("DEBUG", "HTTP Fail, Response Code: " + statusCode);
			}
			
		} catch (ClientProtocolException e) {
			// Any error related to the Http Protocol (e.g. malformed url)
			e.printStackTrace();
		} catch (IOException e) {
			// Any IO error (e.g. File not found)
			e.printStackTrace();
		}

		
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		//Set the pertange done in the progress dialog
		pd.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(Void result) {
		//Dismiss progress dialog
		pd.dismiss();
	}

}
