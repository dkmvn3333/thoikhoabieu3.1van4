package englishvocabulary.gameloft.com.timetable;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class Timetable_Sunday extends AppCompatActivity {

    int vitri=0,id=0,idnew=0;
    String cv,tg,dd;
    //dialog them cong viec
    Dialog dialog_ThemCV;
    EditText edtThemCV_Dialog_ThemCV,edtThemTG_Dialog_ThemCV,edtThemDD_Dialog_ThemCV;
    Button btnAdd_Dialog_ThemCV,btnHuy_Dialog_ThemCV;

    //cac thanh phan trong giao dien
    ImageButton btnAddSunday_timetable;
    ListView lvSunday_timetable;
    ArrayList<CongViec> arrayListSunday_timetable;
    CongViecAdapter adapterSunday_timetable;

    //dialog option listview
    Dialog dialog_optionlv;
    ImageButton imgbtnedit_Dialog_optionlv,imgbtndelete_Dialog_optionlv,imgbtnremind_Dialog_optionlv,imgbtndocument_Dialog_optionlv;

    //dialog capnhat cong viec
    Dialog dialog_capnhatcongviec;
    EditText edtCapNhatCV_Dialog_CNCV,edtCapNhatTG_Dialog_CNCV,edtCapNhatDD_Dialog_CNCV;
    Button btnCapNhat_Dialog_CNCV,btnHuy_Dialog_CNCV;

    //dialog thme nhac nho
    Dialog dialog_themnhacnho;
    TimePicker timePicker;
    Button btnOK_dialogthemnhacnho,btnHuy_dialogthemnhacnho;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    MyDBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable__sunday);
        Create();
        Event();
    }

    private void Event() {
        //hien dialog them cong viec-->them cong viec
        btnAddSunday_timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ThemCV.show();
            }
        });
        //them cong viec vao tung dong cua list view
        btnAdd_Dialog_ThemCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv = edtThemCV_Dialog_ThemCV.getText().toString();
                tg= edtThemTG_Dialog_ThemCV.getText().toString();
                dd = edtThemDD_Dialog_ThemCV.getText().toString();
                arrayListSunday_timetable.add(new CongViec(id,cv,tg,dd));
                adapterSunday_timetable.notifyDataSetChanged();
                lvSunday_timetable.setAdapter(adapterSunday_timetable);

                db.querydata("INSERT INTO ListSunday VALUES('"+vitri+"','"+cv+"','"+tg+"','"+dd+"')");

                dialog_ThemCV.dismiss();

                edtThemCV_Dialog_ThemCV.setText("");
                edtThemTG_Dialog_ThemCV.setText("");
                edtThemDD_Dialog_ThemCV.setText("");
                vitri++;
            }
        });

        btnHuy_Dialog_ThemCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ThemCV.dismiss();
            }
        });


        //nhan giu tren listview --> tuy chon
        lvSunday_timetable.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long k) {
                id=position;
                dialog_optionlv.show();
                return false;
            }
        });
        //xoa 1 dong khoi listview
        imgbtndelete_Dialog_optionlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayListSunday_timetable.remove(id);
                adapterSunday_timetable.notifyDataSetChanged();
                lvSunday_timetable.setAdapter(adapterSunday_timetable);

                db.querydata("DELETE FROM ListSunday WHERE id = '"+id+"'");

                dialog_optionlv.dismiss();
            }
        });
        //chinh sua noi dung 1 dong
        imgbtnedit_Dialog_optionlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_capnhatcongviec.show();
                dialog_optionlv.dismiss();
                //cap nhat
                btnCapNhat_Dialog_CNCV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cv = edtCapNhatCV_Dialog_CNCV.getText().toString();
                        tg = edtCapNhatTG_Dialog_CNCV.getText().toString();
                        dd = edtCapNhatDD_Dialog_CNCV.getText().toString();

                        arrayListSunday_timetable.set(id,new CongViec(id,cv,tg,dd));
                        adapterSunday_timetable.notifyDataSetChanged();
                        lvSunday_timetable.setAdapter(adapterSunday_timetable);
                        db.querydata("UPDATE ListSunday SET congviec='"+cv+"',thoigian = '"+tg+"',diadiem = '"+dd+"'WHERE id = '"+id+"'");
                        edtCapNhatCV_Dialog_CNCV.setText("");
                        edtCapNhatTG_Dialog_CNCV.setText("");
                        edtCapNhatDD_Dialog_CNCV.setText("");
                        dialog_capnhatcongviec.dismiss();
                    }
                });
                btnHuy_Dialog_CNCV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_capnhatcongviec.dismiss();
                    }
                });
            }
        });

        imgbtnremind_Dialog_optionlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_themnhacnho.show();
                dialog_optionlv.dismiss();
            }
        });

        btnOK_dialogthemnhacnho.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(Timetable_Sunday.this,AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(Timetable_Sunday.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),(alarmManager.INTERVAL_DAY)*7,pendingIntent);
                dialog_themnhacnho.dismiss();
            }
        });
        btnHuy_dialogthemnhacnho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_themnhacnho.dismiss();
            }
        });
    }

    private void Create() {
        //dialog them cong viec
        dialog_ThemCV = new Dialog(Timetable_Sunday.this);
        dialog_ThemCV.setContentView(R.layout.dialog_themcongviec);
        dialog_ThemCV.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //edt
        edtThemCV_Dialog_ThemCV = (EditText) dialog_ThemCV.findViewById(R.id.edtthemcongviec_Dialog_THEMCONGVIEC);
        edtThemTG_Dialog_ThemCV = (EditText) dialog_ThemCV.findViewById(R.id.edtthemthoigian_Dialog_THEMCONGVIEC);
        edtThemDD_Dialog_ThemCV = (EditText) dialog_ThemCV.findViewById(R.id.edtthemdiadiem_Dialog_THEMCONGVIEC);
        //btn
        btnAdd_Dialog_ThemCV = (Button) dialog_ThemCV.findViewById(R.id.btnthem_Dialog_THEMCONGVIEC);
        btnHuy_Dialog_ThemCV = (Button) dialog_ThemCV.findViewById(R.id.btnhuy_Dialog_THEMCONGVIEC);

        //dialog option listview
        dialog_optionlv = new Dialog(Timetable_Sunday.this);
        dialog_optionlv.setContentView(R.layout.dialog_optionlistview);
        dialog_optionlv.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //imgbtn
        imgbtndelete_Dialog_optionlv = (ImageButton) dialog_optionlv.findViewById(R.id.imgbtndelete_dialogoptionlv);
        imgbtnedit_Dialog_optionlv = (ImageButton) dialog_optionlv.findViewById(R.id.imgbtnedit_dialogoptionlv);
        imgbtnremind_Dialog_optionlv = (ImageButton) dialog_optionlv.findViewById(R.id.imgbtnremind_dialogoptionlv);
        //dialog capnhat cong viec
        dialog_capnhatcongviec = new Dialog(Timetable_Sunday.this);
        dialog_capnhatcongviec.setContentView(R.layout.dialog_capnhatcongviec);
        dialog_capnhatcongviec.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //edt
        edtCapNhatCV_Dialog_CNCV = (EditText) dialog_capnhatcongviec.findViewById(R.id.edtcapnhatcongviec_Dialog_CNCONGVIEC);
        edtCapNhatTG_Dialog_CNCV = (EditText) dialog_capnhatcongviec.findViewById(R.id.edtcapnhatthoigian_Dialog_CNCONGVIEC);
        edtCapNhatDD_Dialog_CNCV = (EditText) dialog_capnhatcongviec.findViewById(R.id.edtcapnhatdiadiem_Dialog_CNCONGVIEC);
        //btn
        btnCapNhat_Dialog_CNCV = (Button) dialog_capnhatcongviec.findViewById(R.id.btncapnhat_Dialog_CNCONGVIEC);
        btnHuy_Dialog_CNCV = (Button) dialog_capnhatcongviec.findViewById(R.id.btnhuy_Dialog_CNCONGVIEC);

        //giao dien
        arrayListSunday_timetable = new ArrayList<>();
        adapterSunday_timetable = new CongViecAdapter(this,R.layout.dong_listview,arrayListSunday_timetable);
        lvSunday_timetable = (ListView) findViewById(R.id.lvSunday_timetable);
        btnAddSunday_timetable = (ImageButton) findViewById(R.id.btnAddSunday_timetable);

        //dialog them nhac nho
        dialog_themnhacnho = new Dialog(Timetable_Sunday.this);
        dialog_themnhacnho.setContentView(R.layout.dialog_themnhacnho);
        dialog_themnhacnho.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        timePicker = (TimePicker) dialog_themnhacnho.findViewById(R.id.tp_dialogthemnhacnho);
        btnOK_dialogthemnhacnho = (Button) dialog_themnhacnho.findViewById(R.id.btnOK_dialogthemnhacnho);
        btnHuy_dialogthemnhacnho = (Button) dialog_themnhacnho.findViewById(R.id.btnHUY_dialogthemnhacnho);

        db = new MyDBHelper(Timetable_Sunday.this,"quanlycongviec",null,1);
        db.querydata("CREATE TABLE IF NOT EXISTS ListSunday(id INTEGER PRIMARY KEY,congviec VARCHAR,thoigian VARCHAR,diadiem VARCHAR)");

        Cursor cursor = db.getdata("SELECT * FROM ListSunday");
        arrayListSunday_timetable.clear();

        //dua du lieu vao arraylist-->listview
        while(cursor.moveToNext())
        {

            cv = cursor.getString(1);
            tg = cursor.getString(2);
            dd = cursor.getString(3);
            id = cursor.getInt(0);
            if(!(cv.equals("")&&tg.equals("")&&dd.equals("")))
            {
                arrayListSunday_timetable.add(new CongViec(idnew, cv, tg, dd));
                adapterSunday_timetable.notifyDataSetChanged();
                lvSunday_timetable.setAdapter(adapterSunday_timetable);
                idnew++;
            }
        }
        //xoa toan bo du lieu trong bang
        db.querydata("DELETE FROM ListSunday");

        //dua du lieu tu arraylist -> table
        cursor.moveToFirst();
        while(vitri!=idnew)
        {
            cv=arrayListSunday_timetable.get(vitri).getCongviec();
            tg=arrayListSunday_timetable.get(vitri).getThoigian();
            dd=arrayListSunday_timetable.get(vitri).getDiadiem();
            id=arrayListSunday_timetable.get(vitri).getId();
            db.querydata("INSERT INTO ListSunday VALUES('"+id+"','"+cv+"','"+tg+"','"+dd+"')");
            vitri=id;
            vitri++;

        }
    }
}
