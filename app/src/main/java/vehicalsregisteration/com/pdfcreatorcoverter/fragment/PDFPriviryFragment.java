package vehicalsregisteration.com.pdfcreatorcoverter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import vehicalsregisteration.com.pdfcreatorcoverter.R;

import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.ADD_IMAGES;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.ADD_PWD;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.BUNDLE_DATA;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.REMOVE_PWd;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.DialogUtils.ADD_WATERMARK;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.DialogUtils.ROTATE_PAGES;

public class PDFPriviryFragment extends Fragment {

    LinearLayout add_password,remove_password,rotate_pages,add_watermark,add_images;
    Bundle bundle;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pdfprivacy, container, false);
        add_password= view.findViewById(R.id.add_password);
        remove_password= view.findViewById(R.id.remove_password);
        rotate_pages= view.findViewById(R.id.rotate_pages);
        add_watermark= view.findViewById(R.id.add_watermark);
        add_images= view.findViewById(R.id.add_images);
        bundle = new Bundle();
        fragmentManager = getFragmentManager();

        add_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, ADD_PWD);
                fragment.setArguments(bundle);
                try {
                    if (fragment != null && fragmentManager != null)
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        remove_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, REMOVE_PWd);
                fragment.setArguments(bundle);
                try {
                    if (fragment != null && fragmentManager != null)
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rotate_pages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ViewFilesFragment();
                bundle.putInt(BUNDLE_DATA, ROTATE_PAGES);
                fragment.setArguments(bundle);
                try {
                    if (fragment != null && fragmentManager != null)
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        add_watermark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ViewFilesFragment();
                bundle.putInt(BUNDLE_DATA, ADD_WATERMARK);
                fragment.setArguments(bundle);
                try {
                    if (fragment != null && fragmentManager != null)
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        add_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new AddImagesFragment();
                bundle.putString(BUNDLE_DATA, ADD_IMAGES);
                fragment.setArguments(bundle);
                try {
                    if (fragment != null && fragmentManager != null)
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

}
