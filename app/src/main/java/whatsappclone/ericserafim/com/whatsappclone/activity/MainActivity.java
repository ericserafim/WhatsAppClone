package whatsappclone.ericserafim.com.whatsappclone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import whatsappclone.ericserafim.com.whatsappclone.R;
import whatsappclone.ericserafim.com.whatsappclone.adapter.TabAdapter;
import whatsappclone.ericserafim.com.whatsappclone.application.ConfiguracaoFirebase;
import whatsappclone.ericserafim.com.whatsappclone.helper.Base64Custom;
import whatsappclone.ericserafim.com.whatsappclone.helper.Preferencias;
import whatsappclone.ericserafim.com.whatsappclone.helper.SlidingTabLayout;
import whatsappclone.ericserafim.com.whatsappclone.model.Contato;
import whatsappclone.ericserafim.com.whatsappclone.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;

    private String identificadorContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        tabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        tabLayout.setDistributeEvenly(true);
        tabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //Configura o adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sair :
                deslogarUsuario();
                return true;
            case R.id.item_adicionar :
                abrirCadastroContato();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deslogarUsuario() {
        ConfiguracaoFirebase.getFirebaseAuth().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirCadastroContato() {
        //Configurações do Dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Novo Contato");
        dialog.setMessage("E-mail do usuário");
        dialog.setCancelable(false);

        //Cria campo de texto no Dialog
        final EditText editText = new EditText(this);
        dialog.setView(editText);

        //Definir o botão positivo
        dialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emailContato = editText.getText().toString();

                if (emailContato.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Preencha o e-mail", Toast.LENGTH_LONG).show();
                } else {
                    identificadorContato = Base64Custom.converterBase64(emailContato);

                    //Verificar se o contato já existe no Firebase
                    DatabaseReference dataBase = ConfiguracaoFirebase.getDatabase();
                    dataBase = dataBase.child("usuarios").child(identificadorContato);

                    dataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Verifica se tem dados
                            if (dataSnapshot.getValue() != null) {
                                //Recuperar dados do contato que será adicionado
                                Usuario usuarioContato = new Usuario();
                                usuarioContato = dataSnapshot.getValue(Usuario.class);

                                //Recuperar dados do usuário logado
                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();

                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario(identificadorContato);
                                contato.setEmail(usuarioContato.getEmail());
                                contato.setNome(usuarioContato.getNome());


                                //Salvar no Firebase
                                DatabaseReference dataBase = ConfiguracaoFirebase.getDatabase()
                                                                                 .child("contatos")
                                                                                 .child(identificadorUsuarioLogado)
                                                                                 .child(identificadorContato);
                                dataBase.setValue(contato);

                            } else {
                                Toast.makeText(MainActivity.this, "Usuário não possui cadsatro no App", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        //Definir o botão negativo
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.create();
        dialog.show();
    }

}
