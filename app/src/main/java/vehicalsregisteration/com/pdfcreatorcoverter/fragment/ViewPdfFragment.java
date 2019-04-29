package vehicalsregisteration.com.pdfcreatorcoverter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vehicalsregisteration.com.pdfcreatorcoverter.R;

import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.BUNDLE_DATA;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.COMPRESS_PDF;

public class ViewPdfFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_viewpdf, container, false);
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        Bundle bundle = new Bundle();
        fragment = new ViewFilesFragment();
        bundle.putString(BUNDLE_DATA, COMPRESS_PDF);
        fragment.setArguments(bundle);
        try {
            if (fragment != null && fragmentManager != null)
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;

    }

}
