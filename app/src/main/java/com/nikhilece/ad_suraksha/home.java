package com.nikhilece.ad_suraksha;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class home extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    CardView a,b,c,d,e;
    Locale myLocale;
    private RequestQueue mQueue;
    int Position;
    TextView con, rec, det, act,mcon, mrec, mdet, mact;
    private static final int REQUEST_CALL = 1;
    ViewFlipper v_flipper;
    int MY_PERMISSIONS=101;
    Spinner spinner;
    Context context;
    FirebaseDatabase database;
    FirebaseUser fuser;
    BarChart barChart;
    BarData barData;
    Button button;
    BarDataSet barDataSet;
    TextView textView;
    ArrayList<BarEntry> barEntries;
    ImageView dot;


    private static final String SET_LABEL = "CASES IN INDIA";
    private static final String[] DAYS = { "","", "", "" };


    @SuppressLint({"WrongThread", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkConnection();

        setContentView(R.layout.activity_home);
        database = FirebaseDatabase.getInstance();

        con = findViewById(R.id.confirm);
        rec = findViewById(R.id.recover);
        det = findViewById(R.id.death);
        act = findViewById(R.id.active);
        dot = findViewById(R.id.notificationdot);
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("notificationdot");
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String use = dataSnapshot.child("message").getValue(String.class);
                assert use != null;
                if (use.equals("false")) {
                    dot.setVisibility(View.INVISIBLE);
                } else {
                    dot.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference z = FirebaseDatabase.getInstance().getReference("seenlist");
        z.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    fuser = FirebaseAuth.getInstance().getCurrentUser();
                    assert key != null;
                    assert fuser != null;

                    if (key.equals(fuser.getUid())){
                        dot.setVisibility(View.INVISIBLE);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mcon = findViewById(R.id.confirmM);
        mrec = findViewById(R.id.recoverM);
        mdet = findViewById(R.id.deathM);
        mact = findViewById(R.id.activeM);
        barChart = findViewById(R.id.pieChart);
        barChart.setNoDataText("loading...");
        Paint p = barChart.getPaint(Chart.PAINT_INFO);
        p.setTextSize(60f);
        p.setColor(R.color.black);
        status();

        spinner = (Spinner) findViewById(R.id.spin);

        List<String> Categories = new ArrayList<>();

        Categories.add(0, "Choose State");
        Categories.add(1,"Maharashtra");
        Categories.add(2,"Tamil Nadu");
        Categories.add(3,"Delhi");
        Categories.add(4,"Gujarat");
        Categories.add(5,"Karnataka");
        Categories.add(6,"Uttar Pradesh");
        Categories.add(7,"Telangana");
        Categories.add(8,"Andhra Pradesh");
        Categories.add(9,"West Bengal");
        Categories.add(10,"Rajasthan");
        Categories.add(11,"Haryana");
        Categories.add(12,"Bihar");
        Categories.add(13,"Madhya Pradesh");
        Categories.add(14,"Assam");
        Categories.add(15,"Odisha");
        Categories.add(16,"Jammu and Kashmir");
        Categories.add(17,"punjab");
        Categories.add(18, "Kerala");
        Categories.add(19, "chhattisgarh");
        Categories.add(20,"Jharkhand");
        Categories.add(21, "Uttarakhand");
        Categories.add(22, "Goa");
        Categories.add(23, "unassigned statet");
        Categories.add(24,"Tripura");
        Categories.add(25,"Manipur");
        Categories.add(26,"Pondicherry");
        Categories.add(27,"Himachal Pradesh");
        Categories.add(28,"Ladakh");
        Categories.add(29,"Nagaland");
        Categories.add(30,"Chandigar");
        Categories.add(31,"Daman Diu Dadra and Nagar Haveli");
        Categories.add(32,"Arunachal Pradesh");
        Categories.add(33,"Meghalaya");
        Categories.add(34,"Mizoram");
        Categories.add(35,"Andaman & Nicobar");
        Categories.add(36,"Sikkim");
        Categories.add(37,"Lakshadweep");


        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(home.this, android.R.layout.simple_spinner_dropdown_item,Categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter((dataAdapter));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                if (position>0) {

                    mQueue = Volley.newRequestQueue(home.this);
                    home.this.Position = spinner.getSelectedItemPosition() -1;
                    jsonParse(Position);


}
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        LinearLayout emergency = (LinearLayout) findViewById (R.id.emergency);
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                builder.setMessage(R.string.alertcall)
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callbutton();
                            }
                        }).show();
            }
        });
        LinearLayout labourshelter = (LinearLayout) findViewById (R.id.labour);
        labourshelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, mapsmarker.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout funds = (LinearLayout) findViewById (R.id.pmfunds);
        funds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, pmnrf.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });

        LinearLayout count = (LinearLayout) findViewById (R.id.statastics);
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, count.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout symptons = (LinearLayout) findViewById (R.id.symp);
        symptons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, sympdf.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout contactus = (LinearLayout) findViewById (R.id.contactus);
        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, MessagesActivity.class));
                Animatoo.animateSlideLeft(home.this);

            }
        });
        LinearLayout faq = (LinearLayout) findViewById (R.id.faq);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, faq.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout dosdont = (LinearLayout) findViewById (R.id.dosdonts);
        dosdont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, dosdonts.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });

        LinearLayout volreg = (LinearLayout) findViewById (R.id.volreg);
        volreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, volreg.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout cnlv = (LinearLayout) findViewById (R.id.counselling);
        cnlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, youVi.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout hosp = (LinearLayout) findViewById (R.id.hospital);
        hosp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.customview, viewGroup, false);
                LinearLayout hos = (LinearLayout) dialogView.findViewById (R.id.hos);
                hos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inten = new Intent(home.this, hospital.class);
                        inten.putExtra("idd", "https://www.google.com/search?q=near+by+hospital&rlz=1C1CHBF_enIN880IN880&oq=near+by+&aqs=chrome.1.69i57j0l7.4809j0j7&sourceid=chrome&ie=UTF-8");
                        startActivity(inten);
                        Animatoo.animateSlideLeft(home.this);

                    }
                });

                LinearLayout med = (LinearLayout)dialogView. findViewById (R.id.med);
                med.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inte = new Intent(home.this, hospital.class);
                        inte.putExtra("idd", "https://www.google.com/search?rlz=1C1CHBF_enIN880IN880&sxsrf=ALeKk00a5u47yNqNiToDaMMwyAIUCMgeVQ%3A1593276987993&ei=O3r3Xq6rPKOa4-EPm_qDkAI&q=near+by+medical+shop&oq=near+by+med&gs_lcp=CgZwc3ktYWIQAxgAMgIIADICCAAyAggAMgIIADICCAAyAggAMgIIADICCAAyAggAMgIIADoECAAQRzoECCMQJzoHCAAQFBCHAjoFCAAQkQJQpKYTWKC6E2CdwxNoAHABeACAAbkBiAGiDpIBBDAuMTGYAQCgAQGqAQdnd3Mtd2l6&sclient=psy-ab");
                        startActivity(inte);
                        Animatoo.animateSlideLeft(home.this);

                    }
                });
                LinearLayout lab = (LinearLayout) dialogView.findViewById (R.id.lab);
                lab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(home.this, hospital.class);
                        i.putExtra("idd", "https://www.google.com/search?rlz=1C1CHBF_enIN880IN880&sxsrf=ALeKk004RX9ymSQ6IHu_bxUDNwycQe4-Gg%3A1600231577783&ei=mZhhX9evL46e4-EPj9C0mAI&q=covid+test+centre+near+me&oq=covid+test+ce&gs_lcp=CgZwc3ktYWIQAxgAMggIABCxAxCRAjICCAAyAggAMgIIADICCAAyAggAMgIIADICCAAyAggAMgIIADoHCCMQ6gIQJzoECCMQJzoECAAQQzoHCAAQsQMQQzoICAAQsQMQgwE6CggAELEDEIMBEEM6BQgAELEDOgoIABCxAxAUEIcCOgUIABCSA1DQF1i7OWDQR2gBcAB4AIAB0QGIAdAQkgEGMC4xMi4xmAEAoAEBqgEHZ3dzLXdperABCsABAQ&sclient=psy-ab");
                        startActivity(i);
                        Animatoo.animateSlideLeft(home.this);
                    }
                });
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }

        });
        LinearLayout req = (LinearLayout) findViewById (R.id.volreq);
        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this,volrequest.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout NN = (LinearLayout) findViewById (R.id.msme);
        NN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(home.this, pdf.class);
                inten.putExtra("id", "msme");
                startActivity(inten);
                Animatoo.animateSlideLeft(home.this);
            }
        });

        LinearLayout onler = (LinearLayout) findViewById (R.id.onlineler);
        onler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, onler.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout freegrocery = (LinearLayout) findViewById (R.id.freegroceryoutlets);
        freegrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, grocery.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout transit = (LinearLayout) findViewById (R.id.transitworkers);
        transit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, transitworkers.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout sos = (LinearLayout) findViewById (R.id.sos);
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, sos.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout notify = (LinearLayout) findViewById (R.id.notifications);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, ImagesActivity.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout near = (LinearLayout) findViewById (R.id.nearus);
        near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, nearbycases.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout mani = (LinearLayout) findViewById (R.id.drm);
        mani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, manipur.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });
        LinearLayout epass = (LinearLayout) findViewById (R.id.epass);
        epass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(home.this, ePASS.class));
                Animatoo.animateSlideLeft(home.this);
            }
        });

        LinearLayout first = (LinearLayout) findViewById (R.id.first);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.customviewfirst, viewGroup, false);
                LinearLayout fir = (LinearLayout) dialogView.findViewById (R.id.fir);
                fir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                        builder.setMessage(R.string.alertcallfire)
                                .setNegativeButton(R.string.no, null)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String number = "101";
                                        if (number.trim().length() > 0) {
                                            if (ContextCompat.checkSelfPermission(home.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                ActivityCompat.requestPermissions(home.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                                            } else {
                                                String dial = "tel:" + number;
                                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                                            }
                                        }
                                    }
                                }).show();
                    }
                });

                LinearLayout pol = (LinearLayout) dialogView.findViewById (R.id.pol);
                pol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                        builder.setMessage(R.string.alertcallpolice)
                                .setNegativeButton(R.string.no, null)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String number = "100";
                                        if (number.trim().length() > 0) {
                                            if (ContextCompat.checkSelfPermission(home.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                ActivityCompat.requestPermissions(home.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                                            } else {
                                                String dial = "tel:" + number;
                                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                                            }
                                        }
                                    }
                                }).show();
                    }
                });
                LinearLayout amb = (LinearLayout)dialogView. findViewById (R.id.amb);
                amb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                        builder.setMessage(R.string.alertcallamb)
                                .setNegativeButton(R.string.no, null)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String number = "108";
                                        if (number.trim().length() > 0) {
                                            if (ContextCompat.checkSelfPermission(home.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                ActivityCompat.requestPermissions(home.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                                            } else {
                                                String dial = "tel:" + number;
                                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                                            }
                                        }
                                    }
                                }).show();
                    }
                });
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();



            }

        });
        a=findViewById(R.id.hotel);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(home.this, pdf.class);
                inten.putExtra("id", "hotel");
                startActivity(inten);
                Animatoo.animateSlideLeft(home.this);
            }
        });
        b=findViewById(R.id.reg);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(home.this, pdf.class);
                inten.putExtra("id", "religious");
                startActivity(inten);
                Animatoo.animateSlideLeft(home.this);
            }
        });
        c=findViewById(R.id.rest);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(home.this, pdf.class);
                inten.putExtra("id", "restaurants");
                startActivity(inten);
                Animatoo.animateSlideLeft(home.this);
            }
        });
        d=findViewById(R.id.office);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(home.this, pdf.class);
                inten.putExtra("id", "office");
                startActivity(inten);
                Animatoo.animateSlideLeft(home.this);
            }
        });
        e=findViewById(R.id.air);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(home.this, pdf.class);
                inten.putExtra("id", "air");
                startActivity(inten);
                Animatoo.animateSlideLeft(home.this);
            }
        });



        LinearLayout assess = (LinearLayout) findViewById(R.id.assess);
        assess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inten = new Intent(home.this, Healthassess.class);
                startActivity(inten);
                Animatoo.animateSlideLeft(home.this);


            }
        });
        FirebaseApp.initializeApp(home.this);
        Log.d("Instance ID ", FirebaseInstanceId.getInstance().getId());

        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(home.this, login.class));
                    finish();
                }
            }
        };

        int Permission_All = 1;

        String[] Permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};
        if (!hasPermissions(home.this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

        int[] imageos = {
                R.drawable.stayhome,
                R.drawable.a,
                R.drawable.covpng,
                R.drawable.b,
                R.drawable.c,
                R.drawable.d,
                R.drawable.e,
                R.drawable.f,
                R.drawable.g,
                R.drawable.h,
                R.drawable.i,
                R.drawable.j,
                R.drawable.covidlogo,

        };
        v_flipper = findViewById(R.id.v_flipper);
        for (int i = 0; i < imageos.length; i++) {
            flip_image(imageos[i]);
        }
        configureChartAppearance();
        DatabaseReference re = FirebaseDatabase.getInstance().getReference("casesinindia");
        re.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String i = ds.child("confirmed").getValue(String.class);
                    String j = ds.child("deaths").getValue(String.class);
                    String k = ds.child("active").getValue(String.class);
                    String l = ds.child("recovered").getValue(String.class);
                    assert i != null;
                    String m = i.replace(",","");
                    assert j != null;
                    String n = j.replace(",","");
                    assert k != null;
                    String o = k.replace(",","");
                    assert l != null;
                    String p = l.replace(",","");

                    int a = 0;
                    int b = 0;
                    int c = 0;
                    int d = 0;

                    try {
                        a = Integer.parseInt(Objects.requireNonNull(m));
                        b = Integer.parseInt(Objects.requireNonNull(n));
                        c = Integer.parseInt(Objects.requireNonNull(o));
                        d = Integer.parseInt(Objects.requireNonNull(p));
                    } catch(NumberFormatException nfe) {
                        System.out.println("Could not parse " + nfe);
                    }

                    BarDataSet barDataSet1 = new BarDataSet(dataValues1(a,d,c,b), "DataSet 1");
                    BarData barData =new BarData();
                    barData.addDataSet(barDataSet1);

                    barChart.setData(barData);
                    barDataSet1.setColors(ColorTemplate.JOYFUL_COLORS);
                    barDataSet1.setValueTextColor(Color.BLACK);
                    barDataSet1.setValueTextSize(10f);

                    barDataSet1.setColors(
                            ContextCompat.getColor(barChart.getContext(), R.color.blue),
                            ContextCompat.getColor(barChart.getContext(),R.color.green),
                            ContextCompat.getColor(barChart.getContext(), R.color.red),
                            ContextCompat.getColor(barChart.getContext(), R.color.black)

                    );



                    barChart.getAxisRight().setEnabled(false);
                    barChart.getLegend().setEnabled(false);
                    barChart.setDrawBarShadow(false);
                    barChart.setPinchZoom(false);
                    barChart.setDrawGridBackground(false);
                    barData.setBarWidth(0.65f);
                    barChart.getDescription().setEnabled(false);
                    barChart.animateY(5000);
                    barChart.animateX(6000);
                    barChart.invalidate();


                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void configureChartAppearance() {
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();

        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int) value];
            }
        });
    }


    private List<BarEntry> dataValues1(int a, int d, int c, int b) {

        ArrayList<BarEntry>dataVals=new ArrayList<>();

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("CONFIRMED");
        xAxisLabel.add("RECOVERED");
        xAxisLabel.add("ACTIVE");
        xAxisLabel.add("DEATHS");


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        ValueFormatter formatter = new ValueFormatter() {


            @Override
            public String getFormattedValue(float value) {
                return xAxisLabel.get((int) value);
            }
        };

        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        dataVals.add(new BarEntry(0,a));
        dataVals.add(new BarEntry(1,d));
        dataVals.add(new BarEntry(2,c));
        dataVals.add(new BarEntry(3,b));
        return dataVals;


    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void checkConnection() {
        if (!isOnline()) {

            Toast.makeText(home.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public CountryStatus parse_data(String raw_data){
        String[] idata_array = raw_data.split("│");
        String[] data_array =new String[100];
        int j=0;
        String str = "";
        for(int i=0; i<11; i++) {
            if(!idata_array[i].trim().equals(""))
                data_array[j++] = idata_array[i].trim();
            else
                data_array[j++] = "-";
            //   str+= idata_array[i].trim() + "\n";
        }
        //textView.setText(str);
        return new CountryStatus(data_array[1],data_array[2], data_array[3], data_array[4],data_array[5], data_array[6], data_array[7],data_array[8], data_array[9]);
    }


    private void flip_image(int i) {
        ImageView view=new ImageView(this);
        view.setBackgroundResource(i);
        v_flipper.addView(view);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);
        v_flipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this,android.R.anim.slide_out_right);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {
            signOut();
        }
        if (id == R.id.profile) {
            startActivity ( new Intent(home.this, profile.class));
            Animatoo.animateSlideLeft(home.this);

        }
        if (id == R.id.lng) {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(home.this);
            alertDialog.setTitle("Choose language");
            final String[] types = {"English", "हिन्दी","తెలుగు","தமிழ்"};
            int checkedItem = -1;
            alertDialog.setSingleChoiceItems(types, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            setLocale("english");
                            break;
                        case 1:
                            setLocale("hi");
                            break;
                        case 2:
                            setLocale("te");
                            break;
                        case 3:
                            setLocale("tm");
                            break;
                    }
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(true);
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }
    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, home.class);
        startActivity(refresh);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.exit)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
    @Override
    public void onStart() {
        super.onStart();

        auth.addAuthStateListener(authListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }


    private void callbutton() {

        String number = "1075";
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(home.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(home.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }

    }
    public void status(){
        RequestQueue queue = Volley.newRequestQueue(this);
        //change country name in the end of url to get updates for different country
        String url ="https://corona-stats.online/india";



// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String str_short = response.substring(1300,1720);
                        char car = response.charAt(0);
                        char[] new_str = new char[1000];
                        int j=0;
                        for(int i=0; i<str_short.length(); i++){
                            if(str_short.charAt(i)!='║'  && str_short.charAt(i)!='─' && str_short.charAt(i)!='═'
                                    && str_short.charAt(i)!='╚' && str_short.charAt(i)!='╟' && str_short.charAt(i)!='┼' &&
                                    str_short.charAt(i)!='╢' && str_short.charAt(i)!='╧'){
                                new_str[j++] = str_short.charAt(i);
                            }
                        }

                        CountryStatus india = parse_data(String.copyValueOf(new_str));
                        Log.d("OUTPUT",str_short);

                        ((TextView) findViewById(R.id.confirm)).setText(india.getTotal_cases());

                        ((TextView) findViewById(R.id.death)).setText(india.getTotal_deaths());

                        ((TextView) findViewById(R.id.recover)).setText(india.getRecovered());
                        ((TextView) findViewById(R.id.active)).setText(india.getActive());

                        reference = FirebaseDatabase.getInstance().getReference("casesinindia").child("cases");
                        String m=act.getText().toString();
                        String n=con.getText().toString();
                        String o=rec.getText().toString();
                        String p =det.getText().toString();

                        if (m.equals("")){
                            return;
                        }
                        if (n.equals("")){
                            return;
                        }
                        if (o.equals("")){
                            return;
                        }
                        if (p.equals("")){
                            return;
                        }

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("active", m);
                        hashMap.put("confirmed", n);
                        hashMap.put("recovered", o);
                        hashMap.put("deaths", p);
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    private void jsonParse(final int Position) {
        final String url = "https://api.rootnet.in/covid19-in/unofficial/covid19india.org/statewise";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            JSONObject json = response.getJSONObject("data");
                            JSONArray jsonArray = json.getJSONArray("statewise");

                            JSONObject info = jsonArray.getJSONObject(Position);
                            String day = info.getString("state");
                            String act = info.getString("active");
                            mact.setText(act);
                            String con = info.getString("confirmed");
                            mcon.setText(con);
                            String rec = info.getString("recovered");
                            mrec.setText(rec);
                            String det = info.getString("deaths");
                            mdet.setText(det);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public static boolean hasPermissions(home context, String... permissions){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && context!=null && permissions!=null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){
                    return  false;
                }
            }
        }
        return true;
    }
}