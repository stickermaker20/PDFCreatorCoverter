package vehicalsregisteration.com.pdfcreatorcoverter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import vehicalsregisteration.com.pdfcreatorcoverter.R;

import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.BUNDLE_DATA;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.COMPRESS_PDF;

public class PDFEditorFragment extends Fragment {

    LinearLayout merge_pdf,split_pdf,invert_pdf,compress_pdf,remove_duplicates_pages_pdf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pdfeditor, container, false);
        merge_pdf= view.findViewById(R.id.merge_pdf);
        split_pdf= view.findViewById(R.id.split_pdf);
        invert_pdf= view.findViewById(R.id.invert_pdf);
        compress_pdf= view.findViewById(R.id.compress_pdf);
        remove_duplicates_pages_pdf= view.findViewById(R.id.remove_duplicates_pages_pdf);

        merge_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MergeFilesFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        split_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SplitFilesFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        invert_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new InvertPdfFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        compress_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle();
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, COMPRESS_PDF);
                fragment.setArguments(bundle);
                try {
                    if (fragment != null && fragmentManager != null)
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        remove_duplicates_pages_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RemoveDuplicatePagesFragment();
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
