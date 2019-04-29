package vehicalsregisteration.com.pdfcreatorcoverter.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.afollestad.materialdialogs.MaterialDialog;

import vehicalsregisteration.com.pdfcreatorcoverter.R;
import vehicalsregisteration.com.pdfcreatorcoverter.adapter.ViewFilesAdapter;
import vehicalsregisteration.com.pdfcreatorcoverter.db.DatabaseHelper;
import vehicalsregisteration.com.pdfcreatorcoverter.interfaces.MergeFilesListener;

import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.MASTER_PWD_STRING;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.STORAGE_LOCATION;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.Constants.appName;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.DialogUtils.createAnimationDialog;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.DialogUtils.createOverwriteDialog;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.StringUtils.getDefaultStorageLocation;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.StringUtils.getSnackbarwithAction;
import static vehicalsregisteration.com.pdfcreatorcoverter.util.StringUtils.showSnackbar;

public class MergeHelper implements MergeFilesListener {
    private MaterialDialog mMaterialDialog;
    private Activity mActivity;
    private FileUtils mFileUtils;
    private boolean mPasswordProtected = false;
    private String mPassword;
    private String mHomePath;
    private Context mContext;
    private ViewFilesAdapter mViewFilesAdapter;
    private SharedPreferences mSharedPrefs;

    public MergeHelper(Activity activity, ViewFilesAdapter viewFilesAdapter) {
        mActivity = activity;
        mFileUtils = new FileUtils(mActivity);
        mHomePath = PreferenceManager.getDefaultSharedPreferences(mActivity)
                .getString(STORAGE_LOCATION,
                        getDefaultStorageLocation());
        mContext = mActivity;
        mViewFilesAdapter = viewFilesAdapter;
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
    }

    public void mergeFiles() {
        String[] pdfpaths = mViewFilesAdapter.getSelectedFilePath().toArray(new String[0]);
        String masterpwd = mSharedPrefs.getString(MASTER_PWD_STRING, appName);
        new MaterialDialog.Builder(mActivity)
                .title(R.string.creating_pdf)
                .content(R.string.enter_file_name)
                .input(mContext.getResources().getString(R.string.example), null, (dialog, input) -> {
                    if (StringUtils.isEmpty(input)) {
                        showSnackbar(mActivity, R.string.snackbar_name_not_blank);
                    } else {
                        if (!mFileUtils.isFileExist(input + mContext.getResources().getString(R.string.pdf_ext))) {
                            new MergePdf(input.toString(), mHomePath, mPasswordProtected,
                                    mPassword, this, masterpwd).execute(pdfpaths);
                        } else {
                            MaterialDialog.Builder builder = createOverwriteDialog(mActivity);
                            builder.onPositive((dialog12, which) -> new MergePdf(input.toString(),
                                    mHomePath, mPasswordProtected, mPassword,
                                    this, masterpwd).execute(pdfpaths))
                                    .onNegative((dialog1, which) -> mergeFiles()).show();
                        }
                    }
                })
                .show();
    }
    @Override
    public void resetValues(boolean isPDFMerged, String path) {
        mMaterialDialog.dismiss();
        if (isPDFMerged) {
            getSnackbarwithAction(mActivity, R.string.pdf_merged)
                    .setAction(R.string.snackbar_viewAction, v -> mFileUtils.openFile(path)).show();
            new DatabaseHelper(mActivity).insertRecord(path,
                    mActivity.getString(R.string.created));
        } else
            showSnackbar(mActivity, R.string.file_access_error);
        mViewFilesAdapter.updateDataset();
    }

    @Override
    public void mergeStarted() {
        mMaterialDialog = createAnimationDialog(mActivity);
        mMaterialDialog.show();
    }
}
