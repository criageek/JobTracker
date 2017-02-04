package com.rdfmobileapps.jobtracker;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class RDTopButtons extends LinearLayout {
	private OnRDTBClickedListener onRDTBClickedListener = null;
	private Vibrator vibe = null;
	
	private Button mSaveButton;
	private Button mCancelButton;
	private Button mCustomButton;

	private boolean mVibrateOnClick = true;
	private boolean mEditing = false;
	private String mCustomButtonText = "";

	public RDTopButtons(Context context) {
	    super(context);
	    if ( !isInEditMode() ) {
	    	this.vibe = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
	    }
	}

	public RDTopButtons(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    if ( !isInEditMode() ) {
	    	this.vibe = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
	    }
	    LayoutInflater inflater = (LayoutInflater)context.getSystemService
	    		(Context.LAYOUT_INFLATER_SERVICE);
	    LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.rd_top_buttons, this);
	    setupButtons(layout);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.com_rdfmobileapps_jobtracker_RDTopButtons);
        boolean showButton = array.getBoolean(R.styleable.com_rdfmobileapps_jobtracker_RDTopButtons_saveButtonVisible,	false);
        showSaveButton(showButton);
        showButton = array.getBoolean(R.styleable.com_rdfmobileapps_jobtracker_RDTopButtons_cancelButtonVisible, false);
        showCancelButton(showButton);
        showButton = array.getBoolean(R.styleable.com_rdfmobileapps_jobtracker_RDTopButtons_customButtonVisible, false);
        showCustomButton(showButton);
        mVibrateOnClick = array.getBoolean(R.styleable.com_rdfmobileapps_jobtracker_RDTopButtons_vibrateOnClick, mVibrateOnClick);
		String buttonText = array.getString(R.styleable.com_rdfmobileapps_jobtracker_RDTopButtons_customButtonText);
		setCustomButtonText(buttonText);
        array.recycle();
	}

	public RDTopButtons(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    if ( !isInEditMode() ) {
	    	this.vibe = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
	    }
	    LayoutInflater inflater = (LayoutInflater)context.getSystemService
	    		(Context.LAYOUT_INFLATER_SERVICE);
	    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.rd_top_buttons, this, true);
	    setupButtons(layout);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.com_rdfmobileapps_jobtracker_RDTopButtons);
        boolean showButton = array.getBoolean(R.styleable.com_rdfmobileapps_jobtracker_RDTopButtons_saveButtonVisible,	false);
        showSaveButton(showButton);
        showButton = array.getBoolean(R.styleable.com_rdfmobileapps_jobtracker_RDTopButtons_cancelButtonVisible, false);
        showCancelButton(showButton);
        showButton = array.getBoolean(R.styleable.com_rdfmobileapps_jobtracker_RDTopButtons_customButtonVisible, false);
        showCustomButton(showButton);
		mVibrateOnClick = array.getBoolean(R.styleable.com_rdfmobileapps_jobtracker_RDTopButtons_vibrateOnClick, mVibrateOnClick);
        array.recycle();
	}

	private void setupButtons(LinearLayout pLayout) {
		if ( pLayout != null ) {
			this.mSaveButton = (Button)pLayout.findViewById(R.id.rdtb_save);
			if ( this.mSaveButton != null ) {
				this.mSaveButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						saveButtonClicked();
					}
				});
			}
			this.mCancelButton = (Button)pLayout.findViewById(R.id.rdtb_cancel);
			if ( this.mCancelButton != null ) {
				this.mCancelButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						cancelButtonClicked();
					}
				});
			}
			this.mCustomButton = (Button)pLayout.findViewById(R.id.rdtb_custom);
			if ( this.mCustomButton != null ) {
				this.mCustomButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						customButtonClicked();
					}
				});
			}
		}
	}
	
	public interface OnRDTBClickedListener {
	    public abstract void onCancelClick();
	    public abstract void onSaveClick();
	    public abstract void onCustomClick();
	}
	
	public void setOnRDTBClickedListener(OnRDTBClickedListener listener) {
		onRDTBClickedListener = listener;
	}
	
	public void cancelButtonClicked() {
		if ( mVibrateOnClick ) {
			vibe.vibrate(RDConstants.BUTTON_VIBRATE_DURATION);
		}
	    if ( onRDTBClickedListener != null ) {
	    	onRDTBClickedListener.onCancelClick();
	    }
	}
	
	public void customButtonClicked() {
		if ( mVibrateOnClick ) {
			vibe.vibrate(RDConstants.BUTTON_VIBRATE_DURATION);
		}
	    if ( onRDTBClickedListener != null ) {
	    	onRDTBClickedListener.onCustomClick();
	    }
	}
	
	public void saveButtonClicked() {
		if ( mVibrateOnClick ) {
			vibe.vibrate(RDConstants.BUTTON_VIBRATE_DURATION);
		}
	    if ( onRDTBClickedListener != null ) {
	    	onRDTBClickedListener.onSaveClick();
	    }
	}
	
	public void showCancelButton(boolean pShow) {
		if ( this.mCancelButton != null ) {
			if ( pShow ) {
				this.mCancelButton.setVisibility(View.VISIBLE);
			} else {
				this.mCancelButton.setVisibility(View.INVISIBLE);
			}
		}
	}

	public void showSaveButton(boolean pShow) {
		if ( this.mSaveButton != null ) {
			if ( pShow ) {
				this.mSaveButton.setVisibility(View.VISIBLE);
			} else {
				this.mSaveButton.setVisibility(View.INVISIBLE);
			}
		}
	}

	public void showCustomButton(boolean pShow) {
		if ( this.mCustomButton != null ) {
			if ( pShow ) {
				this.mCustomButton.setVisibility(View.VISIBLE);
			} else {
				this.mCustomButton.setVisibility(View.INVISIBLE);
			}
		}
	}

	private void setButtonsVisibility() {
		if (mEditing) {
			mSaveButton.setVisibility(View.VISIBLE);
			mCancelButton.setVisibility(View.VISIBLE);
		} else {
			mSaveButton.setVisibility(View.INVISIBLE);
			mCancelButton.setVisibility(View.INVISIBLE);
		}
	}

	public void setVibrateOnClick(boolean pVibrate) {
		mVibrateOnClick = pVibrate;
	}
	
	public boolean getVibrateOnClick() {
		return mVibrateOnClick;
	}

	public void setCustomButtonText(String pText) {
		mCustomButton.setText(pText);
	}

	public void setSaveButtonVisible(boolean pVisible) {
		if (pVisible) {
			mSaveButton.setVisibility(View.VISIBLE);
		} else {
			mSaveButton.setVisibility(View.INVISIBLE);
		}
	}

	public void setCancelButtonVisible(boolean pVisible) {
		if (pVisible) {
			mCancelButton.setVisibility(View.VISIBLE);
		} else {
			mCancelButton.setVisibility(View.INVISIBLE);
		}
	}

	public void setCustomButtonVisible(boolean pVisible) {
		if (pVisible) {
			mCustomButton.setVisibility(View.VISIBLE);
		} else {
			mCustomButton.setVisibility(View.INVISIBLE);
		}
	}

	public void setEditing(boolean pEditing) {
		mEditing = pEditing;
		setButtonsVisibility();
	}

}
