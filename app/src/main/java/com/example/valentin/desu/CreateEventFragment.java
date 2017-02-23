package com.example.valentin.desu;


import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment {

    final Calendar dateTime = Calendar.getInstance();
    Context context;

    public Button btnDate;
    private DatePickerDialog datePickerDialog;
    public String date;

    public Button btnTime;
    private TimePickerDialog timePickerDialog;
    public String time;

    public  ImageView btnPhoto;
    public static int OPEN_IMAGE = 1;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create_event, container, false);

        context = getActivity();
        btnDate = (Button)view.findViewById(R.id.button_Date);
        btnTime= (Button)view.findViewById(R.id.button_Time);
        btnPhoto = (ImageView)view.findViewById(R.id.imageViewPhoto);

        return view;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityCreated(Bundle savedInstanceState) {
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = dateTime.get(Calendar.YEAR);
                int month = dateTime.get(Calendar.MONTH);
                int day = dateTime.get(Calendar.DAY_OF_MONTH);

                datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                        NumberFormat f = new DecimalFormat("00");

                        btnDate.setText(f.format(dayOfMonth)+"/"+f.format(month)+"/"+year);
                    }
                },year,month,day);

                datePickerDialog.show();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = dateTime.get(Calendar.HOUR_OF_DAY);
                int minute = dateTime.get(Calendar.MINUTE);

                timePickerDialog=new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        btnTime.setText(hour +":"+minute);
                    }
                },hour,minute,true);

                timePickerDialog.show();
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, OPEN_IMAGE);
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_IMAGE && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bmp = null;
            try
            {
                bmp = getBitmapFromUri(selectedImage);
                btnPhoto.setImageBitmap(bmp);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            //btnPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    // récupere l'image
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
