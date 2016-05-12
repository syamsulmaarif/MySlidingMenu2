package com.caunk94.myslidingmenu2;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DataProcurement extends AppCompatActivity implements View.OnClickListener{
    private Payment pay =null;
    private DBManager dm = null;
    private EditText textID = null;
    private EditText textIDPAYMENT = null;
    private EditText textTANGGALPAYMENT = null;
    private EditText textSUPPLIER = null;
    private EditText textTOTALBAYAR = null;
    private int state = 0;
    private long _id=0;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_procurement);

        dm = new DBManager(this);
        dm.Open();
        inisial();
        findViewsByIdDatePicker();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateTimeField();
    }

    private void inisial(){
        textID = (EditText)findViewById(R.id.EditTextId);
        textIDPAYMENT = (EditText)findViewById(R.id.EditTextIDPayment);
        textTANGGALPAYMENT = (EditText)findViewById(R.id.EditTextTanggalP);
        textSUPPLIER = (EditText)findViewById(R.id.EditTextSuplier);
        textTOTALBAYAR = (EditText)findViewById(R.id.EditTextTottalb);


        state = getIntent().getIntExtra("STATE", 0);
        if (state == 0) {
            pay = new Payment();
        }else {
            _id = getIntent().getLongExtra("ID", 0);
            pay = dm.getDataByID(_id);
            populate();

        }
    }

    public void buttonBatalClick(View v){
        DataProcurement.this.finish();
    }

    public void butonSimpanclick(View v){
        PopulateObjectData();
        if (state ==0) {
            dm.insert(pay);
        }
        dm.update(pay);

        DataProcurement.this.finish();
    }

    private void populate() {
        if (pay !=null) {
            textID.setText(pay.get_id()+ "");
            textIDPAYMENT.setText(pay.getIdpayment());
            textTANGGALPAYMENT.setText(pay.getTanggalpayment());
            textSUPPLIER.setText(pay.getSupplier());
            textTOTALBAYAR.setText(pay.getTotalbayar().toString());
        }

    }

    private void PopulateObjectData() {
        // TODO Auto-generated method stub
        pay.setIdpayment(textIDPAYMENT.getText().toString());
        pay.setTanggalpayment(textTANGGALPAYMENT.getText().toString());
        pay.setSupplier(textSUPPLIER.getText().toString());
        pay.setTotalbayar(Double.valueOf(textTOTALBAYAR.getText().toString()));
    }


    private void findViewsByIdDatePicker() {
        textTANGGALPAYMENT = (EditText) findViewById(R.id.EditTextTanggalP);
        textTANGGALPAYMENT.setInputType(InputType.TYPE_NULL);
        //textTANGGALPAYMENT.requestFocus();


    }

    private void setDateTimeField() {
        textTANGGALPAYMENT.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                textTANGGALPAYMENT.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    @Override
    public void onClick(View v) {
        if(v == textTANGGALPAYMENT) {
            fromDatePickerDialog.show();
        }
    }
}
