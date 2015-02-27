package com.mw.crm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.mw.crm.extra.MyApp;

//import android.view.ViewGroup.LayoutParams;

public class CRMActivity extends Activity {

	TextView headerTitleTV, rightButtonTV;
	View view;

	MyApp myApp;

	public void findThings() {
		headerTitleTV = (TextView) findViewById(R.id.title_header_TV);
		rightButtonTV = (TextView) findViewById(R.id.right_button_TV);
		view = (View) findViewById(R.id.aa);
	}

	private void setTypeface() {
		headerTitleTV.setTypeface(myApp.getTypefaceBoldSans());
		rightButtonTV.setTypeface(myApp.getTypefaceBoldSans());
	}

	int viewWidth, viewHeight;

	public void initView(String title, String title2) {
		setTypeface();
		headerTitleTV.setText(title);
		if (title2 != null) {
			rightButtonTV.setText(title2);
		} else {
			rightButtonTV.setVisibility(View.GONE);
		}

		/** To get header title in center of view **/
//		ViewTreeObserver viewTreeObserver = rightButtonTV.getViewTreeObserver();
//		if (viewTreeObserver.isAlive()) {
//			viewTreeObserver
//					.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//						@Override
//						public void onGlobalLayout() {
//							rightButtonTV.getViewTreeObserver()
//									.removeOnGlobalLayoutListener(this);
//							viewWidth = rightButtonTV.getWidth();
//							viewHeight = rightButtonTV.getHeight();
//						}
//					});
//		}
//		System.out.println("width  : " + viewWidth + "height  : " + viewHeight);
//		
//		int width = rightButtonTV.getLayoutParams().width;
//		int height = rightButtonTV.getLayoutParams().height;
//		System.out.println("width  : " + width + "height  : " + height);
//
//		int width2 = rightButtonTV.getMeasuredWidth();
//		int height2 = rightButtonTV.getMeasuredHeight();
//		System.out.println("width  : " + width2 + "height  : " + height2);
//
//		view.setLayoutParams(new LayoutParams(width, height));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initThings();
	}

	private void initThings() {
		myApp = (MyApp) getApplicationContext();

	}

	public void onBack(View view) {
		hideKeyboard(view);
		finish();
	}

	public void onRightButton(View view) {

	}

	public TextView getRightButtonTV() {
		return rightButtonTV;
	}

	public void setRightButtonTV(TextView rightButtonTV) {
		this.rightButtonTV = rightButtonTV;
	}

	public void onHome(View view) {
		Intent intent = new Intent(this, MenuActivity2.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

	public void hideKeyboard(View view) {
		View view2 = null;
		if (view == null) {
			view2 = view;
		} else {
			view2 = this.getCurrentFocus();
		}
		if (view2 != null) {
			InputMethodManager inputManager = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view2.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
