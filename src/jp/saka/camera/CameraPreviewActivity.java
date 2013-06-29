package jp.saka.camera;

import java.util.List;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.widget.TextView;
import android.util.Log;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.graphics.ImageFormat;
import android.widget.Toast;
import android.content.res.Configuration;

public class CameraPreviewActivity extends Activity
{
	private SurfaceView mCameraPrevSV = null;
	private Camera mCamera = null;

	private static final int CameraId = 1;
	private static final int PreviewWidth = 640;
	private static final int PreviewHeight = 480;

	private int getDisplayOrientation(int cameraId) {

		CameraInfo info = new android.hardware.Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);

		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		int degrees = rotation * 90;

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}

		return result;
	}

	protected boolean isPortrait() {
		return (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("sakalog", "onCreate");

		super.onCreate(savedInstanceState);

		setContentView(R.layout.camera_preview);

		mCameraPrevSV = (SurfaceView)findViewById(R.id.CameraPrevSV);

		ViewGroup.LayoutParams layoutParams = mCameraPrevSV.getLayoutParams();

		if (isPortrait()) {
			layoutParams.height = PreviewWidth/2;
			layoutParams.width = PreviewHeight/2;
		} else {
			layoutParams.height = PreviewHeight/2;
			layoutParams.width = PreviewWidth/2;
		}

		mCameraPrevSV.setLayoutParams(layoutParams);

		SurfaceHolder holder = mCameraPrevSV.getHolder();

		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		holder.addCallback(
				new SurfaceHolder.Callback() {
					public void surfaceCreated(SurfaceHolder holder) {
						Log.d("sakalog", "Camera preview surface created.");
					}

					public void surfaceDestroyed(SurfaceHolder holder) {
						Log.d("sakalog", "Camera preview surface destroyed.");

						if (mCamera != null) {
							try {
								mCamera.stopPreview();
								mCamera.setPreviewCallbackWithBuffer(null);
							} catch (Exception e) {
								e.printStackTrace();
								return;
							}
						}
					}

					public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
						Log.d("sakalog", "Camera preview surface changed. format=" + format + " width=" + width + " height=" + height);

						if (mCamera != null) {
							try {
								mCamera.stopPreview();
								mCamera.setPreviewCallbackWithBuffer(null);

								Camera.Parameters params = mCamera.getParameters();

								params.setPreviewSize(PreviewWidth, PreviewHeight);

								int degree = getDisplayOrientation(CameraId);

								int bufferSize = (PreviewWidth * PreviewHeight * ImageFormat.getBitsPerPixel(params.getPreviewFormat())) / 8;

								mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
									public void onPreviewFrame(byte[] data, Camera camera) {
										if (data != null) {
											Log.d("sakalog", "data: " + data.length + "B");
											camera.addCallbackBuffer(data);
										} else {
											Log.d("sakalog", "data is null.");
										}
									}
								});

								mCamera.addCallbackBuffer(new byte[bufferSize]);
								mCamera.addCallbackBuffer(new byte[bufferSize]);
								mCamera.setParameters(params);
								mCamera.setDisplayOrientation(degree);
								mCamera.setPreviewDisplay(holder);
								mCamera.startPreview();

							} catch (Exception e) {
								e.printStackTrace();
								return;
							}
						}
					}
				});
	}

	@Override
	public void onStart()
	{
		Log.d("sakalog", "onStart");

		super.onStart();
	}

	@Override
	public void onStop()
	{
		Log.d("sakalog", "onStop");

		super.onStop();
	}

	@Override
	public void onPause()
	{
		Log.d("sakalog", "onPause");

		super.onPause();

		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public void onResume()
	{
		Log.d("sakalog", "onResume");

		super.onResume();

		// CameraはonResumeでopenし、onPause()でreleaseする。
		try {
			mCamera = Camera.open(CameraId);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void onDestroy()
	{
		Log.d("sakalog", "onDestroy");

		super.onDestroy();
	}
}



