package com.example.phototagger.common;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.example.phototagger.R;

import java.io.File;

public class S3Utils {
    private final static String BUCKET_NAME = "photo-tagger-bony";
    private final static String TAG = S3Utils.class.getSimpleName();


    public static void uploadFile(final Context context, File file) {
        AWSMobileClient.getInstance().initialize(context).execute();

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(context)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();

        TransferObserver uploadObserver = transferUtility.upload(file.getName(), file);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    Toast.makeText(context, context.getString(R.string.uploadCompleted), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d(TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                ex.printStackTrace();
            }

        });
    }

    public static void deleteFile(File file) {
        new DeleteTask().execute(file);
    }

    private static class DeleteTask extends AsyncTask<File, Void, Void> {

        private AmazonS3 s3Client;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            s3Client = new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider());
        }

        @Override
        protected Void doInBackground(File... files) {
            try {
                s3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, files[0].getName()));
            } catch (AmazonServiceException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG, "S3 object deleting is requested");
        }
    }
}
