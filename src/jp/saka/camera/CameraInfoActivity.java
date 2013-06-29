package jp.saka.camera;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;
import android.util.Log;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.graphics.ImageFormat;

public class CameraInfoActivity extends Activity
{
	private TextView mCameraInfoTV;

	private void showCameraInfo()
	{
		try {
			int numOfCameras = Camera.getNumberOfCameras();
			mCameraInfoTV.append("Number of Cameras: " + numOfCameras + "\n");
			for (int i=0; i<numOfCameras; i++) {
				mCameraInfoTV.append("\n");
				CameraInfo info = new CameraInfo();
				Camera.getCameraInfo(i, info);
				if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
					mCameraInfoTV.append("Camera[" + i + "] Facing: Back \n");
				} else if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
					mCameraInfoTV.append("Camera[" + i + "] Facing: Front \n");
				}
				mCameraInfoTV.append("Camera[" + i + "] Orientation: " + info.orientation + "\n");
				Camera cam = Camera.open(0);
				Camera.Parameters params = cam.getParameters();
				List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
				List<Integer> previewFormats = params.getSupportedPreviewFormats();
				List<int[]> previewFpsRange = params.getSupportedPreviewFpsRange();
				List<Integer> previewFrameRates = params.getSupportedPreviewFrameRates();
				for (Integer format : previewFormats) {
					String formatName = "";
					switch (format) {
						case ImageFormat.JPEG:
							formatName = "";
							break;
						case ImageFormat.NV16:
							formatName = "NV16";
							break;
						case ImageFormat.NV21:
							formatName = "NV21";
							break;
						case ImageFormat.RGB_565:
							formatName = "RGB_565";
							break;
						case ImageFormat.UNKNOWN:
							formatName = "UNKNOWN";
							break;
						case ImageFormat.YUY2:
							formatName = "YUY2";
							break;
						case ImageFormat.YV12:
							formatName = "YV12";
							break;
					}
					mCameraInfoTV.append("Camera[" + i + "] Preview Format: " + formatName + "\n");
				}
				for (int[] range : previewFpsRange) {
					if (range.length == 2) {
						mCameraInfoTV.append("Camera[" + i + "] Preview FPS Range: min=" + range[0] + " max=" + range[1] + "\n");
					}
				}
				for (Integer framerate : previewFrameRates) {
					mCameraInfoTV.append("Camera[" + i + "] Preview Frame Rate: " + framerate + "\n");
				}
				for (Camera.Size size : previewSizes) {
					mCameraInfoTV.append("Camera[" + i + "] Preview Size: height=" + size.height + " width=" + size.width + "\n");
				}
				cam.release();
			}
		} catch (RuntimeException e) {

		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_info);
		mCameraInfoTV = (TextView) findViewById(R.id.CameraInfoTV);
		showCameraInfo();
	}
}



