package com.example.game.LevelTwo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.game.AbstractCanvasActivity;
import com.example.game.Login.LoginAndroidMapDatabase;
import com.example.game.R;

import java.util.Random;

public abstract class AbstractLevelTwoView extends AbstractCanvasActivity {
    private Paint groundPaint = new Paint();
    private Paint pointPaint = new Paint();
    private Paint pausePaint = new Paint();
    private Paint timerDisplayPaint = new Paint();
    private final int vAdjustment = 600 + 700;
    private final int runningAnimationDelay = 3;
    private final int jumpingAnimationDelay = 4;

    private Bitmap cactus;
    private Bitmap background;
    private Bitmap hearts;
    private Bitmap[] horseRunningSpritesArray = new Bitmap[7];
    private Bitmap[] horseJumpingSpritesArray = new Bitmap[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setCurrView();
        setContentView(currView);


        //Setting default paint values and bitmaps
        defaultPaintValues();
        defaultBitmaps();
    }

    public abstract void setCurrView();

    private void defaultPaintValues() {
        pointPaint.setTypeface(Typeface.DEFAULT_BOLD);

        groundPaint.setColor(Color.YELLOW);
        pointPaint.setColor(Color.WHITE);
        timerDisplayPaint.setColor(Color.WHITE);

        pausePaint.setStrokeWidth(3);

    }

    private void defaultBitmaps() {
        cactus = BitmapFactory.decodeResource(getResources(), R.drawable.cactus_1);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.level_2_bg);
        hearts = BitmapFactory.decodeResource(getResources(), R.drawable.pixelheart);
        populateHorseRunningBitmapArray();
        populateHorseJumpBitmapArray();
    }

    private void populateHorseRunningBitmapArray() {
        horseRunningSpritesArray[0] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_run_00);
        horseRunningSpritesArray[1] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_run_01);
        horseRunningSpritesArray[2] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_run_02);
        horseRunningSpritesArray[3] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_run_03);
        horseRunningSpritesArray[4] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_run_04);
        horseRunningSpritesArray[5] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_run_05);
        horseRunningSpritesArray[6] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_run_06);
    }

    private void populateHorseJumpBitmapArray() {
        horseJumpingSpritesArray[0] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_jump_00);
        horseJumpingSpritesArray[1] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_jump_01);
        horseJumpingSpritesArray[2] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_jump_02);
        horseJumpingSpritesArray[3] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_jump_03);
        horseJumpingSpritesArray[4] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_jump_04);
        horseJumpingSpritesArray[5] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_jump_05);
        horseJumpingSpritesArray[6] = BitmapFactory.decodeResource(getResources(), R.drawable.horse_jump_06);
    }

    void drawHeart(Point location) {
        Rect dst = new Rect(location.x, location.y - 50, location.x + 50, location.y);
        super.getCanvas().drawBitmap(hearts, null, dst, null);
    }

    void drawPoints(Point location, int size, int numPoints) {
        Intent prev = getIntent();
        int sprite = prev.getIntExtra("spriteID", R.drawable.cowboy_yellow);
        if (sprite == R.drawable.clown) {
            Random randomGenerator = new Random();
            int red = randomGenerator.nextInt(256);
            int green = randomGenerator.nextInt(256);
            int blue = randomGenerator.nextInt(256);

            int randomColour = Color.rgb(red, green, blue);
            pointPaint.setColor(randomColour);
        }
        pointPaint.setTextSize(size);
        super.getCanvas().drawText("Points:" + numPoints, location.x, location.y, pointPaint);
    }

    void drawTimerDisplay(Point location, int size, int secondsLeft) {
        timerDisplayPaint.setTextSize(size);
        super.getCanvas().drawText("Time Left:" + secondsLeft, location.x, location.y, timerDisplayPaint);
    }

    private int runningSpriteIndex = 0;
    private int jumpingSpriteIndex = 0;

    void drawPlayer(Point location, int isJumping) {
        Rect source = new Rect(location.x, location.y - 144 + vAdjustment, location.x + 190, location.y + vAdjustment);

        if (isJumping == 1) {
            if (jumpingSpriteIndex / jumpingAnimationDelay >= horseRunningSpritesArray.length - 1) {
                //holds bitmap at the last frame of the jump animation sequence until player lands
                super.getCanvas().drawBitmap(horseJumpingSpritesArray[horseRunningSpritesArray.length - 1], null, source, null);
            } else {
                super.getCanvas().drawBitmap(horseJumpingSpritesArray[jumpingSpriteIndex / jumpingAnimationDelay], null, source, null);
            }
            //increment the jumping sprite index while player is jumping
            jumpingSpriteIndex++;
        } else {
            super.getCanvas().drawBitmap(horseRunningSpritesArray[runningSpriteIndex / runningAnimationDelay], null, source, null);
            //once player is not jumping, reset index of jumping animation to zero
            jumpingSpriteIndex = 0;
        }

        runningSpriteIndex++;
        //reset running animation index to 0 after it reaches the last one
        if (runningSpriteIndex == horseRunningSpritesArray.length * runningAnimationDelay) {
            runningSpriteIndex = 0;
        }
    }

    void drawObstacle(Point location) {
        Rect source = new Rect(location.x, location.y - 87 + vAdjustment, location.x + 100, location.y + 13 + vAdjustment);
        super.getCanvas().drawBitmap(cactus, null, source, null);
    }

    void drawBackground(Point location) {
        Rect source = new Rect(location.x, location.y, location.x + 1920, location.y + 1900);
        super.getCanvas().drawBitmap(background, null, source, null);

    }

    void drawPauseButton() {
        pausePaint.setColor(Color.WHITE);
        super.getCanvas().drawRect(1000, 30, 1070, 100, pausePaint);
        pausePaint.setColor(Color.BLACK);
        super.getCanvas().drawRect(1010, 45, 1030, 85, pausePaint);
        super.getCanvas().drawRect(1040, 45, 1060, 85, pausePaint);
    }

    void drawGround() {
        Rect dst = new Rect(0, 1810, 2000, 2000);
        getCanvas().drawRect(dst, groundPaint);
    }

    void storeReplay(String dataToStore) {
        //Store replay info here into database
        LoginAndroidMapDatabase db = LoginAndroidMapDatabase.getSingleton(this);
        db.save("$replay", dataToStore);
    }
}
