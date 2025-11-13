package com.example.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText text_id,text_name,text_number;
    Button button_insert,button_update,button_delete,button_query;
    ListView lv;
    ListView lv1;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        text_id = findViewById(R.id.text_id);
        text_name = findViewById(R.id.text_name);
        text_number = findViewById(R.id.text_number);

        button_delete = findViewById(R.id.button_delete);
        button_insert = findViewById(R.id.button_insert);
        button_update = findViewById(R.id.button_update);
        button_query = findViewById(R.id.button_query);

        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,mylist);
        lv.setAdapter(myadapter);

        mydatabase = openOrCreateDatabase("quanlysinhvien.db",MODE_PRIVATE,null);
        try{
            String sql = "CREATE TABLE tbclass(classId TEXT primary key, className TEXT, classNumber INTEGER)";
            mydatabase.execSQL(sql);
        }catch (Exception e){
            Log.e("Error","Bảng đã tồn tại");
        }
        button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String malop = text_id.getText().toString();
            String tenlop = text_name.getText().toString();
            int siso = Integer.parseInt(text_number.getText().toString());
                ContentValues myvalue = new ContentValues();
                myvalue.put("classID",malop);
                myvalue.put("className",tenlop);
                myvalue.put("classNumber",siso);
                String msg = "";
                if(mydatabase.insert("tbclass",null,myvalue)==-1){
                    msg = "Fail to Insert!";
                }else{
                    msg = "Insert successful!";
                }
                button_query.callOnClick();

            }
        });
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String malop = text_id.getText().toString();
                ContentValues myvalue = new ContentValues();
                String tenlop = text_name.getText().toString();
                int siso = Integer.parseInt(text_number.getText().toString());
                myvalue.put("className",tenlop);
                myvalue.put("classNumber",siso);
                int n = mydatabase.update("tbclass",myvalue,"classId=?",new String[]{malop});
                String msg = "";
                if(n==0){
                    msg = "Fail to Update!";
                }else {
                    msg = "Update successful!";
                }
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                button_query.callOnClick();
            }

        });
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String malop = text_id.getText().toString();
                int n = mydatabase.delete("tbclass","classId=?",new String[]{malop});
                String msg = "";
                if(n==0){
                    msg = "Fail to Delete!";
                }else {
                    msg = "Delete successful!";
                }
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
               button_query.callOnClick();
            }

        });
        button_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mylist.clear();
                Cursor c = mydatabase.query("tbclass",null,null,null,null,null,null);
                c.moveToFirst();
                String data = "";
                while(c.isAfterLast() == false){
                    data = c.getString(0) + " - "+ c.getString(1)+ " - "+ c.getString(2);
                    mylist.add(data);
                    c.moveToNext();
                }
                c.close();
                myadapter.notifyDataSetChanged();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}