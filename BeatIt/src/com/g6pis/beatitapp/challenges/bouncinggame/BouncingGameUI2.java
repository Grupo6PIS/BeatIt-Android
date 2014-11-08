package com.g6pis.beatitapp.challenges.bouncinggame;

import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.interfaces.Factory;
import com.g6pis.beatitapp.persistence.StateDAO;

public class BouncingGameUI2 extends Activity implements SensorEventListener {
	private static final String CHALLENGE_ID = "5";
	private SensorManager sensorManager;
    private Sensor accelerometer;
    AnimatedView animatedView = null;
    ShapeDrawable red_ball = new ShapeDrawable();
    ShapeDrawable black_ball = new ShapeDrawable();
    
    private float x;
    private float y;
    private static final float FIRST_RED_RADIUS = 150;
    private static final float FIRST_BLACK_RADIUS = 30;
    private float red_radius = FIRST_RED_RADIUS;
    private float black_radius = FIRST_BLACK_RADIUS;
    private PointF red_center;
    private PointF black_center;
    private int x_min;
    private int x_max;
    private int y_min;
    private int y_max;
    private float decrease_radius_rate;
    private boolean timerRunning = false;
    private boolean ok = false;
    private int seconds;
    private int actionBar_height;
    private Paint pRect = new Paint();
    private Paint pTextCollision = new Paint();
    private Paint pTextTime = new Paint();
    private Rect rect;
	private CountDownTimer timer;
	private BouncingGame bouncingGame;
	private Vibrator v;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        // Get instance of Vibrator from current Context
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
        this.editActionBar();
        bouncingGame = (BouncingGame) Factory.getInstance().getIDataManager().getChallenge(CHALLENGE_ID);
        
        decrease_radius_rate = bouncingGame.getDecrease_radius_rate();
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        System.currentTimeMillis();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        actionBar_height = 60;
        x_max = (int) (metrics.widthPixels * 0.78 + FIRST_RED_RADIUS);
        y_max = (int) (metrics.heightPixels * 0.78 + FIRST_RED_RADIUS);
        x_min = 20;
        y_min = actionBar_height + 40;
        x = x_min;
        y = y_min;
        
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		Typeface tf = Typeface.create("Roboto",Typeface.NORMAL);
        
        red_center = new PointF(x,y);
        pTextCollision.setTextSize(40);
        pTextCollision.setColor(Color.WHITE);
        pTextCollision.setTypeface(tf);
        
        pTextTime.setTextSize(40);
        pTextTime.setColor(Color.WHITE);
        pTextTime.setTypeface(tf);
        
        pRect.setColor(getResources().getColor(R.color.bouncing));
        rect = new Rect(0, 0, metrics.widthPixels, actionBar_height + 30);
        
        timer = this.createTimer();
        this.timer.start();
		this.timerRunning = true;
        
        animatedView = new AnimatedView(this);
        setContentView(animatedView);
    }
    
	public CountDownTimer createTimer(){
		CountDownTimer timer = new CountDownTimer(bouncingGame.getTime(), bouncingGame.getTime()/1000) {
			
			public void onTick(long millisUntilFinished) {
				seconds = (int) (millisUntilFinished / 1000);
			}

			public void onFinish() {
				if (timerRunning) {
					stopTimer();
				}
			}
		};
		
		return timer;
	}

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	
	@Override
	public void onBackPressed(){
		timerRunning = false;
		bouncingGame.setCollision_times(0);
		Intent home = new Intent(this, Home.class);
		startActivity(home);
		this.finish();
		super.onBackPressed();
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Customize ActionBar
	public void editActionBar() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		//actionBar.setDisplayHomeAsUpEnabled(true);
		//actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bouncing)));
	}

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
    }

	private void stopTimer() {
		timerRunning = false;
		timer.cancel();
		completeChallenge();
	}
	
    public PointF relocateBall(boolean first) {
    	PointF black = new PointF();
    	Random r = new Random();
    	ok = false;
    	
    	do {
    		black.x = r.nextInt(x_max) + x_min;
        	black.y = r.nextInt(y_max) + y_min;
        	ok = (black.x > (float) x_min) && (black.y > (float) y_min) && (black.x + black_radius < (float) x_max) && (black.y + black_radius < (float) y_max);
    	} while (!ok);
    	
    	if (first == false && existCollision (black.x, black.y, black_radius, red_center.x, red_center.y, red_radius)) {
			return relocateBall(false);
    	}
        
    	return black;
    }
    
    public boolean existCollision (float x3, float y3, float radius_a, float x2, float y2, float radius_b) {
    	float distance = getDistance (x3, y3, radius_a, x2, y2, radius_b);
    	if (radius_a < 100)
    		distance *= 2;
    	return (distance <= (radius_a + radius_b));
    }
    
    public float getDistance (float x3, float y3, float radius_a, float x2, float y2, float radius_b) {
    	return (float) Math.sqrt((x3 - x2) * (x3 - x2) + (y3 - y2) * (y3 - y2));
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

        	x = red_center.x;
        	y = red_center.y;
        	
        	x -= 2* event.values[0];
        	y += 2* event.values[1];
            
            // Stay the ball on the screen
            if (x + red_radius > x_max) {
                x = x_max - red_radius;
            } else if (x < x_min) {
                x = x_min;
            }
            if (y + red_radius > y_max) {
                y = y_max - red_radius;
            } else if (y < y_min) {
                y = y_min;
            }
            
            red_center.x = x;
            red_center.y = y;
            
            if (existCollision(red_center.x, red_center.y, red_radius, black_center.x, black_center.y, black_radius)) {
            	bouncingGame.increaseCollision_times();

            	// Vibrate for 100 milliseconds
                v.vibrate(100);
                
            	if (decrease_radius_rate * red_radius < 10)
            		red_radius = 10;
            	else
            		red_radius = (int) (decrease_radius_rate * red_radius);
            	black_center = relocateBall(false);
            }

        }
    }
    
    public void completeChallenge() {
    	sensorManager.unregisterListener(this);
		timer = null;
		
		bouncingGame.finishChallenge();
		if (Factory.getInstance().getIDataManager().getHaveToSendScore()) {
			Thread t = new Thread() {
				public void run() {

					Factory.getInstance().getIDataManager().sendScore();
					Factory.getInstance().getIDataManager().updateRanking();
				}
			};

			t.start();
		}
		StateDAO db = new StateDAO(getApplicationContext());
		db.updateState(Factory.getInstance().getIDataManager().getState(CHALLENGE_ID));
		bouncingGame.reset();
		
		// Activity Challenge Finished
		Intent finished = new Intent(this, BouncingGameFinished.class);

		startActivity(finished);
		this.finish();
		
		SharedPreferences sharedPrefs = getApplicationContext()
				.getSharedPreferences("asdasd_preferences",
						Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putBoolean("haveToSendScore", Factory.getInstance().getIDataManager().getHaveToSendScore());
		editor.commit();
	}
    
    public class AnimatedView extends ImageView {

        public AnimatedView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            
            black_center = relocateBall(true);
            black_ball = new ShapeDrawable(new OvalShape());
            black_ball.getPaint().setColor(Color.BLACK);
            black_ball.setBounds((int) black_center.x, (int) black_center.y, (int) (black_center.x + black_radius), (int) (black_center.y + black_radius));
            
            red_ball = new ShapeDrawable(new OvalShape());
            red_ball.getPaint().setColor(getResources().getColor(R.color.bouncing));
            red_ball.setBounds((int) x, (int) y, (int) (x + red_radius), (int) (y + red_radius));
        }

        @Override
        protected void onDraw(Canvas canvas) {
        	black_ball.setBounds((int) black_center.x, (int) black_center.y, (int) (black_center.x + black_radius), (int) (black_center.y + black_radius));
            black_ball.draw(canvas);
            
            canvas.drawRect(rect, pRect);
            canvas.drawText("Colisiones: " + bouncingGame.getCollision_times(), 10, actionBar_height, pTextCollision);
            canvas.drawText("Tiempo: " + seconds, x_max - FIRST_RED_RADIUS - 50, actionBar_height, pTextTime);
        	
            red_ball.setBounds((int) x, (int) y, (int) (x + red_radius), (int) (y + red_radius));
            red_ball.draw(canvas);
            invalidate();
        }
    }

}
