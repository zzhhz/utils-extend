package com.zzh.lib.utils.extend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

public class HImageGetter {
    public static final int REQUEST_CODE_GET_IMAGE_FROM_CAMERA = 16542;
    public static final int REQUEST_CODE_GET_IMAGE_FROM_ALBUM = REQUEST_CODE_GET_IMAGE_FROM_CAMERA + 1;

    private Activity mActivity;
    private File mCameraImageDir;
    private File mCameraImageFile;

    private Callback mCallback;

    public HImageGetter(Activity activity) {
        mActivity = activity;
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private File getCameraImageDir() {
        if (mCameraImageDir == null) {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (dir == null) {
                dir = mActivity.getCacheDir();
            }
            mCameraImageDir = dir;
        }
        if (!mCameraImageDir.exists()) {
            mCameraImageDir.mkdirs();
        }
        return mCameraImageDir;
    }

    /**
     * 跳转到系统相册获取图片
     */
    public void getImageFromAlbum() {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mActivity.startActivityForResult(intent, REQUEST_CODE_GET_IMAGE_FROM_ALBUM);
        } catch (Exception e) {
            if (mCallback != null) {
                mCallback.onError(String.valueOf(e));
            }
        }
    }

    /**
     * 打开相机拍照
     */
    public void getImageFromCamera() {
        if (getCameraImageDir() == null) {
            if (mCallback != null) {
                mCallback.onError("获取缓存目录失败");
            }
            return;
        }
        try {
            mCameraImageFile = newFileUnderDir(getCameraImageDir(), ".jpg");
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraImageFile));
            mActivity.startActivityForResult(intent, REQUEST_CODE_GET_IMAGE_FROM_CAMERA);
        } catch (Exception e) {
            if (mCallback != null) {
                mCallback.onError(String.valueOf(e));
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mCallback == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_GET_IMAGE_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    scanFile(mActivity, mCameraImageFile);
                    mCallback.onResultFromCamera(mCameraImageFile);
                }
                break;
            case REQUEST_CODE_GET_IMAGE_FROM_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        mCallback.onError("从相册获取图片失败(intent为空)");
                        return;
                    }
                    Uri uri = data.getData();
                    if (uri == null) {
                        mCallback.onError("从相册获取图片失败(intent数据为空)");
                        return;
                    }
                    String path = null;
                    try {
                        path = getDataColumn(mActivity, uri, null, null);
                    } catch (Exception e) {
                        mCallback.onError("从相册获取图片失败:" + e);
                        return;
                    }

                    if (TextUtils.isEmpty(path)) {
                        mCallback.onError("从相册获取图片失败(路径为空)");
                        return;
                    }
                    mCallback.onResultFromAlbum(new File(path));
                }
                break;
            default:
                break;
        }
    }

    private static File newFileUnderDir(File dir, String ext) {
        if (dir == null) {
            return null;
        }
        if (ext == null) {
            ext = "";
        }
        long current = System.currentTimeMillis();
        File file = new File(dir, String.valueOf(current + ext));
        while (file.exists()) {
            current++;
            file = new File(dir, String.valueOf(current + ext));
        }
        return file;
    }

    private static void scanFile(Context context, File file) {
        if (file == null || !file.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public interface Callback {
        void onResultFromAlbum(File file);

        void onResultFromCamera(File file);

        void onError(String desc);
    }
}
