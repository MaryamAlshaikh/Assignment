package com.example.assignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
//declaration of edittexts, textsviews and buttons
    TextInputEditText ids,names,dayss;
    TextView price;
    DatabaseHelper DB;
    Button add,delete,update,calc,clear,view;

    @Override
    //method to calculate hostingg all functionss
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hides title bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_booking);

        //creating an instance of DatabaseHelper class
        DB = new DatabaseHelper(this);

        List<String> states= Arrays.asList("Twin room - 20 OMR", "Deluxe Twin room - 30 OMR", "King room - 40 OMR", "Deluxe King Room - 50 OMR", "Grand Executive Suite - 60 OMR");
        //initializing strings to the array


        final Spinner spinner = findViewById(R.id.spinnerr); //getting spinner id

        ArrayAdapter myAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, states);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter); //setting adapter to display the chosen item from spinner



        //get the Id's of buttons to click and edittext to enter values
       ids =  findViewById(R.id.ids);
       names =  findViewById(R.id.names);
       dayss =  findViewById(R.id.dayss);
       add =  findViewById(R.id.add);
       delete =  findViewById(R.id.delete);
       update =  findViewById(R.id.update);
       calc =  findViewById(R.id.calculate);
       clear =  findViewById(R.id.clear);
       view =  findViewById(R.id.view);
       price = findViewById(R.id.price);

       //define user defines methods
        deleteData();
        viewData();

//When clear button is clicked, variables initialized to empty.
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ids.setText("");
                names.setText("");
                dayss.setText("");
                price.setText("");


            }
        });

        //when Add button is clicked, it will add the data to the DB
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //converting values to string
                    String state = spinner.getSelectedItem().toString();
                    String total = (price.getText().toString());
                    boolean added = DB.insertData(names.getText().toString(), dayss.getText().toString(), state, total);
                    //if condition to check if fields are empty or not.
                    if(added == true && total != "" && names.getText().toString() != "" && dayss.getText().toString() != "")
                        Toast.makeText(BookingActivity.this,"Data inserted",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(BookingActivity.this,"Data not inserted, Please enter the details and calculate",Toast.LENGTH_SHORT).show();
                    //displaying error msg
                }
            });

//when update button is clicked, shall update a specific data in DB
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //converting values to string
                String state = spinner.getSelectedItem().toString();
                String total = (price.getText().toString());
                String getid = ids.getText().toString();
                boolean update = DB.UpdateData(getid, names.getText().toString(), dayss.getText().toString(), state, total);
                //if condition to check if id is written or not and if the Id is less than 100
                if(getid.matches("")){
                    Toast.makeText(BookingActivity.this,"Data not Updated, Please enter the ID",Toast.LENGTH_SHORT).show();
                } else if(Integer.parseInt(getid) > 100) {
                    //displaying error msg
                    Toast.makeText(BookingActivity.this, "Please enter a value less than 100", Toast.LENGTH_SHORT).show();
                } else if(update == true && getid != "" ) {
                    Toast.makeText(BookingActivity.this, "Data Update", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        //calculation of room price when calc button is clicked
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String state = spinner.getSelectedItem().toString();
                //if condition to get string from spinner to the corresponding price and to calculate number of days
                if(state == "Twin room - 20 OMR") {
                    int days = Integer.parseInt(dayss.getText().toString());
                    int total = days * 20; //number of input days multiply the room price
                    price.setText(total + " OMR"); //setting price to the calculated price
                } else if(state == "Deluxe Twin room - 30 OMR") {
                    int days = Integer.parseInt(dayss.getText().toString());
                    int total = days * 30;  //number of input days multiply the room price
                    price.setText(total + " OMR"); //setting price to the calculated price
                } else if(state == "King room - 40 OMR") {
                    int days = Integer.parseInt(dayss.getText().toString());
                    int total = days * 40; //number of input days multiply the room price
                    price.setText(total + " OMR"); //setting price to the calculated price
                } else if(state == "Deluxe King Room - 50 OMR") {
                    Toast.makeText(BookingActivity.this,"Room Unavailable",Toast.LENGTH_LONG).show(); //if user chooses this room, a msg will display that it is unavailable
                } else if(state == "Grand Executive Suite - 60 OMR") {
                    int days = Integer.parseInt(dayss.getText().toString());
                    int total = days * 60;  //number of input days multiply the room price
                    price.setText(total + " OMR"); //setting price to the calculated price
                }
            }
        });

    }


//viewing data from DB when button view is clicked
    public void viewData() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor r = DB.getAllData();
                if(r.getCount() == 0) //if and while conditionn to check the statementt and run it
                {
                    showMessage("Error","Nothing found"); //error msg if no data is found
                    return;
                }

                StringBuffer b= new StringBuffer();
                while(r.moveToNext())
                { //settings and initializing rows
                    b.append("ID:" + r.getString(0) + "\n");
                    b.append("Name:" + r.getString(1) + "\n");
                    b.append("Days:" + r.getString(2) + "\n");
                    b.append("Type of Room:" + r.getString(3)+"\n");
                    b.append("Price:" + r.getString(4)+"\n");
                }
                showMessage("Customer Details",b.toString()); //dispplaying customer details
            }

        });
    }

    public void showMessage(String title, String msg) { //method of showing msg
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setCancelable(true);
        ad.setTitle(title);
        ad.setMessage(msg);
        ad.show();
    }

    //deleting a specific data when clicking delete button
    public void deleteData() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer delete = DB.deleteData(ids.getText().toString());
                //if condition to check if id is written or not and if the Id is less than 100
                if (ids.getText().toString().matches("")) {
                    Toast.makeText(BookingActivity.this, "Data not Deleted, Please enter the ID", Toast.LENGTH_SHORT).show();
                } else if (delete > 0 && ids.getText().toString() != "") {
                    Toast.makeText(BookingActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(ids.getText().toString()) > 100) { //displaying error msg
                    Toast.makeText(BookingActivity.this, "Please enter a value less than 100", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}