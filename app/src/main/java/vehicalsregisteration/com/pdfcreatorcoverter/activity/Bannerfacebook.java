package vehicalsregisteration.com.pdfcreatorcoverter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;

import vehicalsregisteration.com.pdfcreatorcoverter.R;

public class Bannerfacebook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbnative);
         NativeBannerAd nativeBannerAd;


        nativeBannerAd = new NativeBannerAd(this, getString(R.string.fb_native_banner_ads_app_id));
        nativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

                // Native ad finished downloading all assets

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression

            }
        });
        // load the ad
        nativeBannerAd.loadAd();

    }
    }
