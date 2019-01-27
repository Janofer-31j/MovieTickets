package info.androidhive.nec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class Results extends AppCompatActivity {
    private TextView txtName, txtDuration, txtDirector, txtGenre, txtRating, txtPrice, txtError;
    private ImageView imgPoster;
    private Button btnBuy,btnsms;
    private ProgressBar progressBar;
    private TicketView ticketView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = findViewById(R.id.name);
        txtDirector = findViewById(R.id.director);
        txtDuration = findViewById(R.id.duration);
        txtPrice = findViewById(R.id.price);
        txtRating = findViewById(R.id.rating);
        imgPoster = findViewById(R.id.poster);
        txtGenre = findViewById(R.id.genre);
        btnBuy = findViewById(R.id.btn_buy);
        imgPoster = findViewById(R.id.poster);
        txtError = findViewById(R.id.txt_error);
        ticketView = findViewById(R.id.layout_ticket);
        progressBar = findViewById(R.id.progressBar);
        btnsms=(Button) findViewById(R.id.btn_sms);

        String barcode = getIntent().getStringExtra("code");

        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
            finish();
        }

        // search the barcode
        searchBarcode(barcode);
    }
    private void searchBarcode(String barcode) {
        DBHelper1 db=new DBHelper1(this);
    Staff s=db.getStaff(barcode);
    setresult(s);
    }
    private void showNoTicket() {
        txtError.setVisibility(View.VISIBLE);
        ticketView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void setresult(final Staff s) {
        try {

            // converting json to movie object


            if (s != null) {
                txtName.setText(s.getName());
                txtDirector.setText("Department Of "+s.getDept());
                txtDuration.setText(s.getQfn());
                txtGenre.setText(s.getDsn());
                txtRating.setText(s.getPhno());
                txtPrice.setText(s.getMail());
                imgPoster.setImageResource(R.drawable.nt);
               // Glide.with(this).load(movie.getPoster()).into(imgPoster);
                 btnsms.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         if(s.getPhno()==null)
                         {
                             Toast.makeText(getApplicationContext(),"There is No Phone Number You Cant Call OR SMS",Toast.LENGTH_LONG).show();
                         }
                         else
                         {


                         Dialog dialog=new Dialog();

                         dialog.showdialog(s.getName(), s.getPhno(),s.getMail(),Results.this);
                     }}
                 });

                    btnBuy.setText(getString(R.string.btn_buy_now));
                    btnBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(s.getPhno()==null)
                            {
                                Toast.makeText(getApplicationContext(),"There is No Phone Number You Cant Call OR SMS",Toast.LENGTH_LONG).show();
                            }
                            else
                            {


                                Dialog dialog=new Dialog();

                                dialog.showdialog(s.getName(), s.getPhno(),s.getMail(),Results.this);
                            }
                        }
                    });
                    btnBuy.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));


                ticketView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } else {
                // movie not found
                showNoTicket();
            }
        } catch (Exception e) {
            // exception
            showNoTicket();
            Toast.makeText(getApplicationContext(), "Error occurred. Check your LogCat for full report", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		   /* mPaint.setXfermode(null);
		    mPaint.setAlpha(0xFF);*/

        switch (item.getItemId()) {
            case R.id.action_settings:
                //Do Some Task
                Intent intent = new Intent(getApplicationContext(),Main4Activity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}




