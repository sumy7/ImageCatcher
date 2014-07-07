package com.example.imagecatcher;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class WidthHeightPickerDialog extends Dialog {
	private Context context;
	private OnWidthHeightChangedListener listener;
	private int Width;
	private int Height;
	private SeekBar widthseek;
	private SeekBar heightseek;
	private Button okButton;
	private TextView widthtext;
	private TextView heighttext;

	public WidthHeightPickerDialog(Context context,
			OnWidthHeightChangedListener listener) {
		super(context);
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("对话框");
		setContentView(R.layout.widthheightpicker);
		widthseek = (SeekBar) findViewById(R.id.widthseek);
		widthtext = (TextView) findViewById(R.id.widthtext);
		heightseek = (SeekBar) findViewById(R.id.heightseek);
		heighttext = (TextView) findViewById(R.id.heighttext);
		okButton = (Button) findViewById(R.id.okbutton);
		widthseek.setMax(100);
		widthseek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// Toast.makeText(context, ""+penWidth,
				// Toast.LENGTH_LONG).show();
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				Width = progress;
				widthtext.setText("" + Width);
			}
		});
		heightseek.setMax(100);
		heightseek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				Height = progress;
				heighttext.setText("" + Height);

			}
		});
		okButton.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.widthChanged(Width, Height);
				WidthHeightPickerDialog.this.dismiss();
			}
		});
	}

	public interface OnWidthHeightChangedListener {
		void widthChanged(int Width, int Height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	public void setWidth(int Width) {
		this.Width = Width;
		widthseek.setProgress(Width);
	}

	public void setHeight(int Height) {
		this.Height = Height;
		heightseek.setProgress(Height);
	}

	public void setMaxWidth(int maxWidth) {
		widthseek.setMax(maxWidth);
	}

	public void setMaxHeight(int maxHeight) {
		heightseek.setMax(maxHeight);
	}

}
