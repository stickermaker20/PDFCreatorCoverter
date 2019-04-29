package vehicalsregisteration.com.pdfcreatorcoverter.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import vehicalsregisteration.com.pdfcreatorcoverter.R;
import vehicalsregisteration.com.pdfcreatorcoverter.db.DatabaseHelper;
import vehicalsregisteration.com.pdfcreatorcoverter.interfaces.OnPDFCreatedInterface;
import vehicalsregisteration.com.pdfcreatorcoverter.model.ImageToPDFOptions;
import vehicalsregisteration.com.pdfcreatorcoverter.model.TextToPDFOptions;
import vehicalsregisteration.com.pdfcreatorcoverter.util.Constants;
import vehicalsregisteration.com.pdfcreatorcoverter.util.FileUtils;
import vehicalsregisteration.com.pdfcreatorcoverter.util.PDFUtils;
import vehicalsregisteration.com.pdfcreatorcoverter.util.PageSizeUtils;
import vehicalsregisteration.com.pdfcreatorcoverter.util.PermissionsUtils;
import vehicalsregisteration.com.pdfcreatorcoverter.util.StringUtils;

import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.DEFAULT_BORDER_WIDTH;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.DEFAULT_COMPRESSION;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.DEFAULT_IMAGE_BORDER_TEXT;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.DEFAULT_PAGE_COLOR;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.DEFAULT_PAGE_SIZE;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.DEFAULT_PAGE_SIZE_TEXT;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.DEFAULT_QUALITY_VALUE;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.STORAGE_LOCATION;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.DialogUtils.createAnimationDialog;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.DialogUtils.createOverwriteDialog;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.StringUtils.getDefaultStorageLocation;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.StringUtils.getSnackbarwithAction;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.StringUtils.showSnackbar;

public class CreatePdfFragment extends Fragment implements OnPDFCreatedInterface {

    LinearLayout text_to_pdf,images_to_pdf,qr_to_pdf,barcode_to_pdf,excel_to_pdf;
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE_RESULT = 1;
    private final String mTempFileName = "scan_result_temp.txt";
    private ImageToPDFOptions mPdfOptions;
    private SharedPreferences mSharedPreferences;
    private Activity mActivity;
    private MaterialDialog mMaterialDialog;
    private String mPath;
    private FileUtils mFileUtils;
    private Font.FontFamily mFontFamily;
    private int mFontColor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_createpdf, container, false);
        text_to_pdf= view.findViewById(R.id.text_to_pdf);
        images_to_pdf= view.findViewById(R.id.images_to_pdf);
        qr_to_pdf= view.findViewById(R.id.qr_to_pdf);
        barcode_to_pdf= view.findViewById(R.id.barcode_to_pdf);
        excel_to_pdf= view.findViewById(R.id.excel_to_pdf);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mFontFamily = Font.FontFamily.valueOf(mSharedPreferences.getString(Constants.DEFAULT_FONT_FAMILY_TEXT,
                Constants.DEFAULT_FONT_FAMILY));
        mFontColor = mSharedPreferences.getInt(Constants.DEFAULT_FONT_COLOR_TEXT,
                Constants.DEFAULT_FONT_COLOR);
        PageSizeUtils.mPageSize = mSharedPreferences.getString(Constants.DEFAULT_PAGE_SIZE_TEXT,
                Constants.DEFAULT_PAGE_SIZE);

        getRuntimePermissions();

        text_to_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TextToPdfFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        images_to_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ImageToPdfFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        qr_to_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanner(IntentIntegrator.QR_CODE_TYPES, R.string.scan_qrcode);
            }
        });

        barcode_to_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanner(IntentIntegrator.ONE_D_CODE_TYPES, R.string.scan_barcode);
            }
        });

        excel_to_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ExceltoPdfFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result == null || result.getContents() == null)
            showSnackbar(mActivity, R.string.scan_cancelled);
        else {
            Toast.makeText(mActivity, " " + result.getContents(), Toast.LENGTH_SHORT).show();
            File mDir = mActivity.getCacheDir();
            File mTempFile = new File(mDir.getPath() + "/" + mTempFileName);
            PrintWriter mWriter;
            try {
                mWriter = new PrintWriter(mTempFile);
                mWriter.print("");
                mWriter.append(result.getContents());
                mWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Uri uri = Uri.fromFile(mTempFile);
            resultToTextPdf(uri);
        }
    }

    public void openScanner(Collection<String> scannerType, int promptId) {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(scannerType);
        integrator.setPrompt(mActivity.getString(promptId));
        integrator.setCameraId(0);
        integrator.initiateScan();
    }

    private void resultToTextPdf(Uri uri) {
        new MaterialDialog.Builder(mActivity)
                .title(R.string.creating_pdf)
                .content(R.string.enter_file_name)
                .input(getString(R.string.example), null, (dialog, input) -> {
                    if (StringUtils.isEmpty(input)) {
                        showSnackbar(mActivity, R.string.snackbar_name_not_blank);
                    } else {
                        final String inputName = input.toString();
                        if (!mFileUtils.isFileExist(inputName + getString(R.string.pdf_ext))) {
                            createPdf(inputName, uri);
                        } else {
                            MaterialDialog.Builder builder = createOverwriteDialog(mActivity);
                            builder.onPositive((dialog12, which) -> createPdf(inputName, uri))
                                    .onNegative((dialog1, which) -> resultToTextPdf(uri))
                                    .show();
                        }
                    }
                })
                .show();
    }

    private void createPdf(String mFilename, Uri uri) {
        mPath = mSharedPreferences.getString(STORAGE_LOCATION,
                getDefaultStorageLocation());
        mPath = mPath + mFilename + mActivity.getString(R.string.pdf_ext);
        try {
            PDFUtils fileUtil = new PDFUtils(mActivity);
            int fontSize = mSharedPreferences.getInt(Constants.DEFAULT_FONT_SIZE_TEXT, Constants.DEFAULT_FONT_SIZE);
            fileUtil.createPdf(new TextToPDFOptions(mFilename, PageSizeUtils.mPageSize, false,
                            "", uri, fontSize, mFontFamily, mFontColor, DEFAULT_PAGE_COLOR),
                    Constants.textExtension);
            final String finalMPath = mPath;
            getSnackbarwithAction(mActivity, R.string.snackbar_pdfCreated)
                    .setAction(R.string.snackbar_viewAction, v -> mFileUtils.openFile(finalMPath)).show();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mFileUtils = new FileUtils(mActivity);
    }

    private void resetValues() {
        mPdfOptions = new ImageToPDFOptions();
        mPdfOptions.setBorderWidth(mSharedPreferences.getInt(DEFAULT_IMAGE_BORDER_TEXT,
                DEFAULT_BORDER_WIDTH));
        mPdfOptions.setQualityString(
                Integer.toString(mSharedPreferences.getInt(DEFAULT_COMPRESSION,
                        DEFAULT_QUALITY_VALUE)));
        mPdfOptions.setPageSize(mSharedPreferences.getString(DEFAULT_PAGE_SIZE_TEXT,
                DEFAULT_PAGE_SIZE));
        mPdfOptions.setPasswordProtected(false);
    }


    @Override
    public void onPDFCreationStarted() {
        mMaterialDialog = createAnimationDialog(mActivity);
        mMaterialDialog.show();
    }

    @Override
    public void onPDFCreated(boolean success, String path) {
        mMaterialDialog.dismiss();
        if (!success) {
            showSnackbar(mActivity, R.string.snackbar_folder_not_created);
            return;
        }
        new DatabaseHelper(mActivity).insertRecord(path, mActivity.getString(R.string.created));
        getSnackbarwithAction(mActivity, R.string.snackbar_pdfCreated)
                .setAction(R.string.snackbar_viewAction, v -> mFileUtils.openFile(mPath)).show();
        mPath = path;
        resetValues();
    }

    private void getRuntimePermissions() {
        PermissionsUtils.checkRuntimePermissions(mActivity,
                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE_RESULT,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }

}
