package jp.saka.camera;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;

public class CameraActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button b;
		b = (Button)findViewById(R.id.StartCameraInfoActivityButton);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), jp.saka.camera.CameraInfoActivity.class);
				startActivity(intent);
			}
		});
		b = (Button)findViewById(R.id.StartCameraPreviewActivityButton);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), jp.saka.camera.CameraPreviewActivity.class);
				startActivity(intent);
			}
		});

	}
}


