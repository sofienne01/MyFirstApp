package com.example.myfirstapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog.Builder;

    public class MainActivity extends Activity implements OnClickListener {

        EditText edtNom, edtPrenom, edtNum;
        Button btnAfficher, btnSupprimer, btnAjouter, btnAffichertout;
        Button btnSuivant, btnPrecedent, btnPremier, btnDernier;
        SQLiteDatabase bd;
        Cursor c1;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            edtNom = (EditText)findViewById(R.id.edtNom);
            edtPrenom = (EditText) findViewById(R.id.edtPrenom);
            edtNum = (EditText) findViewById(R.id.edtNum);
            btnAjouter = (Button) findViewById(R.id.btnAjouter);
            btnSupprimer = (Button) findViewById(R.id.btnSupprimer);
            btnSuivant = (Button) findViewById(R.id.btnSuivant);
            btnPrecedent = (Button) findViewById(R.id.btnPrecedent);
            btnPremier = (Button) findViewById(R.id.btnPremier);
            btnDernier = (Button) findViewById(R.id.btnDernier);
            btnAffichertout = (Button) findViewById(R.id.btnAffichertout);
            btnAfficher = (Button) findViewById(R.id.btnAfficher);

            btnAjouter.setOnClickListener(this);
            btnSupprimer.setOnClickListener(this);
            btnAffichertout.setOnClickListener(this);
            btnAfficher.setOnClickListener(this);
            btnPremier.setOnClickListener(this);
            btnSuivant.setOnClickListener(this);
            btnPrecedent.setOnClickListener(this);
            btnDernier.setOnClickListener(this);


            bd = openOrCreateDatabase("GestionJoueurs", Context.MODE_PRIVATE,null);
            bd.execSQL("CREATE TABLE IF NOT EXISTS Joueurs(num integer primary key autoincrement, nom VARCHAR, prenom VARCHAR);");
        }

        public void onClick(View view){
            if (view == btnAjouter){
                if (edtNom.getText().toString().trim().length() == 0 || edtPrenom.getText().toString().trim().length() == 0){
                    showMessage("ERROR","Enter your values");
                    return;
                }
                bd.execSQL("INSERT INTO Joueurs(nom,prenom) VALUES('" + edtNom.getText() +"','"+ edtPrenom.getText() + "');");
                showMessage("Success","Information Added");
                clearText();
            }

            if (view == btnAffichertout){
                Cursor c = bd.rawQuery("SELECT * FROM Joueurs",null);
                if (c.getCount()==0){
                    showMessage("ERROR","No Data");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(c.moveToNext()){
                    buffer.append("Numéro: "+c.getInt(0)+"\n");
                    buffer.append("Nom: "+c.getString(1)+"\n");
                    buffer.append("Prénom: "+c.getString(2)+"\n");
                }
                c.close();
                showMessage("Players",buffer.toString());
            }

            if (view == btnSupprimer){
                if (edtNum.getText().toString().trim().length() == 0){
                    showMessage("ERROR","Enter number of player");
                    return;
                }
                Cursor c = bd.rawQuery("SELECT * FROM Joueurs WHERE num="+ edtNum.getText(),null);
                if (c.moveToFirst()){
                    bd.execSQL("DELETE FROM Joueurs WHERE num=" + edtNum.getText());
                    showMessage("SUCCESS","Information Destroyed");
                }
                else
                    showMessage("ERROR","Invalid Number");
                clearText();
            }

            if (view == btnPremier){
                try{
                    c1 = bd.rawQuery("SELECT * FROM Joueurs",null);
                    if (c1.getCount()==0){
                        showMessage("ERROR","No Data");
                        return;
                    }
                    if(c1.moveToFirst()){
                        edtNom.setText(c1.getString(1));
                        edtPrenom.setText(c1.getString(2));
                    }
                }
                catch(Exception se){
                    Toast.makeText(MainActivity.this,"message"+ se.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }

            if (view == btnDernier){
                try{
                    c1 = bd.rawQuery("SELECT * FROM Joueurs",null);
                    if (c1.getCount()==0){
                        showMessage("ERROR","No Data");
                        return;
                    }
                    if(c1.moveToLast()){
                        edtNom.setText(c1.getString(1));
                        edtPrenom.setText(c1.getString(2));
                    }
                }
                catch(Exception se){
                    Toast.makeText(MainActivity.this,"message"+ se.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }

            if (view == btnSuivant){
                if (c1.moveToNext()) {
                    edtNom.setText(c1.getString(1));
                    edtPrenom.setText(c1.getString(2));
                }
            }

            if (view == btnPrecedent){
                if (c1.moveToPrevious()){
                    edtNom.setText(c1.getString(1));
                    edtPrenom.setText(c1.getString(2));
                }
            }

            if (view == btnAfficher){
                Cursor c = bd.rawQuery("SELECT * FROM Joueurs WHERE num="+ edtNum.getText(),null);
                if (c.getCount()==0){
                    showMessage("ERROR","No Data");
                    return;
                }
                if(c.moveToFirst()){
                    edtNom.setText(c.getString(1));
                    edtPrenom.setText(c.getString(2));
                }
            }
        }

        public void showMessage(String title,String message){
            Builder builder = new Builder(this);
            builder.setCancelable(true);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.show();
        }

        public void clearText(){
            edtNom.setText("");
            edtNum.setText("");
            edtPrenom.setText("");
        }



    }

