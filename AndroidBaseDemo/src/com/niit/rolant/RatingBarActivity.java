package com.niit.rolant;

import com.cellcom.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

//评分组件RatingBar、ImageView图片、 ImageButton图片按钮
public class RatingBarActivity extends Activity {

	private RatingBar ratingBar;
	private ImageButton imageButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating_bar);
		setTitle("评分组件RatingBar、ImageView图片、 ImageButton图片按钮");
		ratingBar=(RatingBar)findViewById(R.id.rating_bar);
		imageButton=(ImageButton)findViewById(R.id.imageButton);
		
		imageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(RatingBarActivity.this, "单击图片按钮!!", Toast.LENGTH_LONG).show();
			}
		});
	}
}
