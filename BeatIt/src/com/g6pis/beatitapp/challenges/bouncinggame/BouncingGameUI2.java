package com.g6pis.beatitapp.challenges.bouncinggame;

import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.interfaces.Factory;
import com.g6pis.beatitapp.persistence.StateDAO;

public class BouncingGameUI2 extends Activity implements SensorEventListener {
	private static final String CHALLENGE_ID = "5";
	private SensorManager sensorManager;
    private Sensor accelerometer;
    AnimatedView animatedView = null;
    ShapeDrawable red_ball = new ShapeDrawable();
    ShapeDrawable black_ball = new ShapeDrawable();
    
    public int collision_times = 0;
    public float x, x_black;
    public float y, y_black;
    public float red_radius = 150;
    public float black_radius = 30;
    
    public PointF red_center;
    public PointF black_center;
    
    private float x_max;
    private float y_max;
    
    Paint pTextCollision = new Paint();
    Paint pTextTime = new Paint();   
    
    
    
	private boolean timerRunning = false;
	private static final long INITIAL_COUNTER_VALUE = 60000; // In milliseconds
	
	private CountDownTimer timer;
	private int seconds;
	private long attemps = 0;
	private BouncingGame bouncingGame;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.editActionBar();
        bouncingGame = (BouncingGame) Factory.getInstance().getIDataManager().getChallenge(CHALLENGE_ID);
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        System.currentTimeMillis();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        x_max = 500;
        y_max = 800;
        
        red_center = new PointF(x,y);
        pTextCollision.setTextSize(26);
        pTextTime.setTextSize(26);
        
        timer = this.createTimer();
        
        this.timer.start();
		this.timerRunning = true;
        
        animatedView = new AnimatedView(this);
        setContentView(animatedView);
    }
    
	public CountDownTimer createTimer(){
		CountDownTimer timer = new CountDownTimer(INITIAL_COUNTER_VALUE, 60) {
			
			public void onTick(long millisUntilFinished) {
				seconds = (int) (millisUntilFinished / 1000);
			}

			public void onFinish() {
				if (timerRunning) {
					bouncingGame.finishChallenge();
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
		//bouncingGame.reset();
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
			//bouncingGame.reset();
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Customize ActionBar
	public void editActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bouncing)));
	}

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

	private void stopTimer() {
		timerRunning = false;
/*		time = g_millis - MAX;
		textViewResult.setVisibility(View.VISIBLE);
		if (Math.abs(time) < TOLERANCE) {
			bouncingGame.setSucceed_times(bouncingGame.getSucceed_times() + 1);
			// Show performance
			textViewResult.setText(getResources().getString(R.string.success));
			textViewResult.setBackgroundColor(getResources().getColor(R.color.verde));
			mp_success.start();
		}
		else {
			// Show performance
			textViewResult.setText(getResources().getString(R.string.fail));
			textViewResult.setBackgroundColor(getResources().getColor(R.color.red));
			mp_fail.start();
		}
		
*/		
		//textViewResult.setVisibility(View.VISIBLE);
		//textViewResult.setText(Double.toString(time));
		timer.cancel();

		completeChallenge();
	}
	
    public PointF relocateBall(boolean first) {
    	PointF red = new PointF();
    	Random r = new Random();
    	
    	red.x = r.nextInt((int) x_max);
    	red.y = r.nextInt((int) y_max);
    	
    	if (first == false && existCollision (red.x, red.y, red_radius, black_center.x, black_center.y, black_radius)) {
			return relocateBall(false);
    	}
        
    	return red;
    }
    
    public boolean existCollision (float x3, float y3, float radius_a, float x2, float y2, float radius_b) {
    	if (radius_a < 20)
    		radius_a = 30;
    	float distance = 2* getDistance (x3, y3, radius_a, x2, y2, radius_b);
    	return (distance <= (radius_a + radius_b));
    }
    
    public float getDistance (float x3, float y3, float radius_a, float x2, float y2, float radius_b) {
    	return (float) Math.sqrt((x3 - x2) * (x3 - x2) + (y3 - y2) * (y3 - y2));
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

        	x -= 2* event.values[0];
            y += 2* event.values[1];
            
            // Stay the ball on the screen
            if (x > x_max) {
                x = x_max;
            } else if (x < 0) {
                x = 0;
            }
            if (y > y_max) {
                y = y_max;
            } else if (y < 0) {
                y = 0;
            }
            
            red_center.x = x;
            red_center.y = y;

            if (existCollision(red_center.x, red_center.y, red_radius, black_center.x, black_center.y, black_radius)) {

/*
            	System.out.println("----- Distance: " + getDistance(red_center.x, red_center.y, red_radius, black_center.x, black_center.y, black_radius));
                System.out.println("----- PosX_red: " + red_center.x);
                System.out.println("----- PosX_black: " + black_center.x);
                System.out.println("----- PosY_red: " + red_center.y);
                System.out.println("----- PosY_black: " + black_center.y);
                System.out.println("----- Radio_red: " + red_radius);
                System.out.println("----- Radio_black: " + black_radius);
                System.out.println("----------------");
*/
            	collision_times += 1;
            	System.out.println("----- Colisiones: " + collision_times);
            	bouncingGame.setSucceed_times(collision_times);
            	if (0.9 * red_radius < 10)
            		red_radius = 10;
            	else
            		red_radius = (int) (0.9 * red_radius);
            	black_center = relocateBall(false);
            }

        }
    }
    
    public void completeChallenge() {
    	sensorManager.unregisterListener(this);
		timer = null;
		
		bouncingGame.setSucceed_times(bouncingGame.getSucceed_times() + 1);
		System.out.println("----- Succeed: " + bouncingGame.getSucceed_times());
		
		//bouncingGame.setSucceed_times(getSucceed_times());
		bouncingGame.finishChallenge();
		
		setAttemps(getAttemps()+1);
		

		StateDAO db = new StateDAO(getApplicationContext());
		db.updateState(Factory.getInstance().getIDataManager().getState(CHALLENGE_ID));
		bouncingGame.reset();
		
		// Activity Challenge Finished
		Intent finished = new Intent(this, BouncingGameFinished.class);

		startActivity(finished);
		this.finish();
	}
    
    public long getAttemps() {
		return attemps;
	}

	public void setAttemps(long attemps) {
		this.attemps = attemps;
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
            red_ball.getPaint().setColor(Color.RED);
            red_ball.setBounds((int) x, (int) y, (int) (x + red_radius), (int) (y + red_radius));
        }

        @Override
        protected void onDraw(Canvas canvas) {
        	black_ball.setBounds((int) black_center.x, (int) black_center.y, (int) (black_center.x + black_radius), (int) (black_center.y + black_radius));
            black_ball.draw(canvas);
            
            canvas.drawText("Colisiones: " + collision_times, 500, 500, pTextCollision);
            canvas.drawText("Tiempo: " + seconds, 500, 700, pTextTime);
        	
            red_ball.setBounds((int) x, (int) y, (int) (x + red_radius), (int) (y + red_radius));
            red_ball.draw(canvas);
            invalidate();
        }
    }

}

