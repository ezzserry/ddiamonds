package com.ddiamonds;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class TryIt extends AppCompatActivity implements OnTouchListener {

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    static String TAG = "main";
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int DRAW = 3;
    int mode = NONE;
    float newRot = 0f;
    float deg = 0f;
    //Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    RelativeLayout relativeLayout;
    //	private Camera camera;
//	private int cameraId = 0;
    int TAKE_PHOTO_CODE = 0;
    private ImageView img_product;
    private ImageView img_pic;
//	private Uri outputFileUri;

    Activity activity = null;
    Button btn_pick;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int CAMERA_PIC_REQUEST = 2;
//	byte[] byteArray;

    String product_url;
    MediaScannerConnection msConn;

    ImageView img_home, img_pre;
//    ProgressBar itemProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.try_it);

        activity = this;

        if (getIntent().getExtras() == null) {
            return;
        }
        img_home = (ImageView) findViewById(R.id.img_home);
        img_pre = (ImageView) findViewById(R.id.img_pre);

        img_pre.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                finish();
            }
        });

        img_home.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(TryIt.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        product_url = getIntent().getExtras().getString("product_url");

        img_pic = (ImageView) findViewById(R.id.img_pic);
        img_product = (ImageView) findViewById(R.id.img_product);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        btn_pick = (Button) findViewById(R.id.btn_pick);
//        itemProgressBar = (ProgressBar) findViewById(R.id.newProgressBar);
//        ImageLoader imageLoader = new ImageLoader(getApplicationContext());
//        imageLoader.DisplayImage(product_url, img_product);

        Picasso.with(this).load(product_url).into(img_product, new Callback() {
            @Override
            public void onSuccess() {
                img_product.setVisibility(View.VISIBLE);
//                itemProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                img_product.setVisibility(View.VISIBLE);
//                itemProgressBar.setVisibility(View.GONE);
                img_product.setImageResource(R.drawable.image_default_grid);
            }
        });

        img_product.setOnTouchListener(this);

        registerForContextMenu(btn_pick);
        btn_pick.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                activity.openContextMenu(v);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mal_photo_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_camera) {
            Intent camera_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, CAMERA_PIC_REQUEST);
        } else if (item.getItemId() == R.id.menu_gallery) {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        ImageView imageView = (ImageView) findViewById(R.id.img_photo);

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK && null != data) {
            //Toast.makeText(Malfunction.this, "tacke photo", Toast.LENGTH_LONG).show();

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            img_pic.setImageBitmap(thumbnail);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//             byteArray = stream.toByteArray();

        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
//            options.inJustDecodeBounds = true;

            Bitmap yourSelectedImage = BitmapFactory.decodeFile(picturePath, options);

            img_pic.setImageBitmap(yourSelectedImage);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            byteArray = stream.toByteArray();

            // Toast.makeText(Malfunction.this, "Image array length: "+ byteArray.length, Toast.LENGTH_SHORT).show();

        }
    }


    // Limit zoomable/pannable image
    private float[] matrixValues = new float[9];
    private float maxZoom;
    private float minZoom;
    private float height;
    private float width;
    private RectF viewRect;

    /////////************ touch events functions **************////////////////////
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            init();
        }
    }

    private void init() {
        maxZoom = 4;
        minZoom = 0.25f;
        height = img_product.getDrawable().getIntrinsicHeight() + 20;
        width = img_product.getDrawable().getIntrinsicWidth() + 20;
        viewRect = new RectF(0, 0, img_product.getWidth() + 20, img_product.getHeight() + 20);
    }

    /////////************touch events for image Moving, panning and zooming   ***********///
    public boolean onTouch(View v, MotionEvent event) {

        // Dump touch event to log
//		    dumpEvent(event);
        // Handle touch events here
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                deg = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(TAG, "mode=NONE");
                mode = NONE;
//		         Log.d(TAG, "mode=NONE");
//		         savedMatrix.set(matrix);
//		         matrix.postRotate(90);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAW) {
                    onTouchEvent(event);
                }
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x,
                            event.getY() - start.y);
                } else if (mode == ZOOM && event.getPointerCount() == 2) {
                    float newDist = spacing(event);
                    matrix.set(savedMatrix);
                    if (newDist > 10f) {
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
//		            if (lastEvent!=null){
                    newRot = rotation(event);
                    float r = newRot - deg;
                    matrix.postRotate(r, img_product.getMeasuredWidth() / 2, img_product.getMeasuredHeight() / 2);
//		            }
                }
                break;
        }
        img_product.setImageMatrix(matrix);
        return true; // indicate event was handled
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);

        return (float) Math.toDegrees(radians);
    }

    //Determine the space between the first two fingers
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // Calculate the mid point of the first two fingers
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public static Bitmap rotate(Bitmap src, float degree) {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);

        // return new bitmap rotated using matrix
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public void takePic(View view) {
        relativeLayout.setDrawingCacheEnabled(true);
        relativeLayout.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(relativeLayout.getDrawingCache());
        relativeLayout.setDrawingCacheEnabled(false); // clear drawing cache

        savePhoto(b);
    }

    public void savePhoto(Bitmap bmp) {
        if (isStoragePermissionGranted()) {
            File imageFileFolder = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
            imageFileFolder.mkdir();
            FileOutputStream out = null;
            Calendar c = Calendar.getInstance();
            String date = fromInt(c.get(Calendar.MONTH))
                    + fromInt(c.get(Calendar.DAY_OF_MONTH))
                    + fromInt(c.get(Calendar.YEAR))
                    + fromInt(c.get(Calendar.HOUR_OF_DAY))
                    + fromInt(c.get(Calendar.MINUTE))
                    + fromInt(c.get(Calendar.SECOND));
            File imageFileName = new File(imageFileFolder, date.toString() + ".jpg");
            try {
                out = new FileOutputStream(imageFileName);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                scanPhoto(imageFileName.toString());

                Toast.makeText(getApplicationContext(), "Photo was saved successfully to " + getString(R.string.app_name) + " folder", Toast.LENGTH_LONG).show();

                out = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (bmp != null && !bmp.isRecycled()) {
                bmp.recycle();
                bmp = null;
            }
        } else
            Toast.makeText(getApplicationContext(), "Failed to save photo ", Toast.LENGTH_LONG).show();

    }

    public String fromInt(int val) {
        return String.valueOf(val);
    }


    public void scanPhoto(final String imageFileName) {
        msConn = new MediaScannerConnection(TryIt.this, new MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
                msConn.scanFile(imageFileName, null);
                Log.i(" obj  in Photo Utility", "connection established");
            }

            public void onScanCompleted(String path, Uri uri) {
                msConn.disconnect();
                Log.i("obj in Photo Utility", "scan completed");
            }
        });
        msConn.connect();
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }
}
