package com.sd.utils_extend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zzh.lib.utils.extend.HShakeListener;


public class ShakeListenerActivity extends AppCompatActivity
{
    public static final String TAG = ShakeListenerActivity.class.getSimpleName();

    private HShakeListener mShakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mShakeListener = new HShakeListener(this);
        mShakeListener.setCallback(new HShakeListener.Callback()
        {
            @Override
            public void onShake()
            {
                Log.i(TAG, "onShake");
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mShakeListener.start();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mShakeListener.stop();
    }
}
