package com.lhh.apst.advancedpagerslidingtabstrip;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by Linhh on 16/2/16.
 */
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig frescoConfig = OkHttpImagePipelineConfigFactory
                .newBuilder(this, new OkHttpClient())
                .build();
        Fresco.initialize(this,frescoConfig);
    }
}
