package vehicalsregisteration.com.pdfcreatorcoverter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import vehicalsregisteration.com.pdfcreatorcoverter.R;

import static com.facebook.ads.AudienceNetworkAds.TAG;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.BUNDLE_DATA;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.EXTRACT_IMAGES;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.REMOVE_PAGES;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.REORDER_PAGES;

public class MorePDFFragment  extends Fragment {

    LinearLayout remove_pages,rearrange_pages,extract_images,zip_to_pdf;
    Bundle bundle;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    InterstitialAd mInterstitialAd1;
    com.facebook.ads.InterstitialAd interstitialAd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_morepdf, container, false);
        remove_pages=view.findViewById(R.id.remove_pages);
        rearrange_pages=view.findViewById(R.id.rearrange_pages);
        extract_images=view.findViewById(R.id.extract_images);
        zip_to_pdf=view.findViewById(R.id.zip_to_pdf);
        bundle = new Bundle();
        fragmentManager = getFragmentManager();
        interstitialAd = new com.facebook.ads.InterstitialAd(getActivity(), "interstitial");
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
        remove_pages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, REMOVE_PAGES);
                fragment.setArguments(bundle);
                try {
                    if (fragment != null && fragmentManager != null)
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        rearrange_pages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, REORDER_PAGES);
                fragment.setArguments(bundle);
                try {
                    if (fragment != null && fragmentManager != null)
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        extract_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, EXTRACT_IMAGES);
                fragment.setArguments(bundle);
                try {
                    if (fragment != null && fragmentManager != null)
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        zip_to_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ZipToPdfFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;

    }

}
