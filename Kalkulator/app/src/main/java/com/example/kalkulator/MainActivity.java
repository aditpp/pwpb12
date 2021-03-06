//package com.example.kalkulator;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}

package com.example.kalkulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle; 
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class MainActivity extends AppCompatActivity {
    // ID dari tombol numerik
    private int[] numericButtons = {R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine};
    // ID tombol tombol operator
    private int[] operatorButtons = {R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide};
    // TextView yg berfungsi untuk menampilkan
    private TextView txtScreen;
    // Mewakili apakah tombol yang terakhir ditekan adlah numerik atau bukan
    private boolean lastNumeric;
    // Mewakili bahwa keadaan saat ini salah atau tidak
    private boolean stateError;
    // Jika benar, jangan izinkan untuk menambahkan DOT lain
    private boolean lastDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mencari textview
        this.txtScreen = (TextView) findViewById(R.id.txtScreen);
        // Mencari dan menyetel OnClickListener ke tombol numerik
        setNumericOnClickListener();
        // Mencari dan setel OnClickListener ke tombol operator, tombol sama dengan dan tombol titik desimal
        setOperatorOnClickListener();
    }

    /**
     * Find and set OnClickListener to numeric buttons.
     */
    private void setNumericOnClickListener() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setel teks tombol yang diklik
                Button button = (Button) v;
                if (stateError) {
                    // cek kondisi error
                    txtScreen.setText(button.getText());
                    stateError = false;
                } else {
                    // jika tidak error lanjut ditambahkan
                    txtScreen.append(button.getText());
                }
                lastNumeric = true;
            }
        };
        // Menetapkan listener ke semua tombol numerik
        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    /**
     * setel OnClickListener ke tombol operator, tombol sama dengan dan tombol titik desimal.
     */
    private void setOperatorOnClickListener() {
        // membuat  OnClickListener untuk operator
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jika status saat ini adalah Error, jangan tambahkan operator
                // Jika input terakhir hanya angka, tambahkan operator
                if (lastNumeric && !stateError) {
                    Button button = (Button) v;
                    txtScreen.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;
                }
            }
        };
        // Assign the listener to all the operator buttons
        for (int id : operatorButtons) {
            findViewById(id).setOnClickListener(listener);
        }
        // Decimal point
        findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError && !lastDot) {
                    txtScreen.append(".");
                    lastNumeric = false;
                    lastDot = true;
                }
            }
        });
        // Clear button
        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtScreen.setText("");  // membersihkan layar
                // reset semua operator
                lastNumeric = false;
                stateError = false;
                lastDot = false;
            }
        });

        findViewById(R.id.btnEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });
    }

    /**
     * Logic to calculate the solution.
     */
    private void onEqual() {
        // jika erorr tidak ada ygbisa dilanjutkan
        //  jika inout terakhir adalah angka, solusi dapat ditemukan
        if (lastNumeric && !stateError) {
            // Read the expression
            String txt = txtScreen.getText().toString();
            // Create an Expression (A class from exp4j library)
            Expression expression = new ExpressionBuilder(txt).build();
            try {
                // hitung hasil dan tampilkan
                double result = expression.evaluate();
                txtScreen.setText(Double.toString(result));

            } catch (ArithmeticException ex) {
                // tampilan pesan erorr
                txtScreen.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }
}