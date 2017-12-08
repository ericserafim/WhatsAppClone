package whatsappclone.ericserafim.com.whatsappclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import whatsappclone.ericserafim.com.whatsappclone.R;
import whatsappclone.ericserafim.com.whatsappclone.application.ConfiguracaoFirebase;
import whatsappclone.ericserafim.com.whatsappclone.helper.Base64Custom;
import whatsappclone.ericserafim.com.whatsappclone.helper.Preferencias;
import whatsappclone.ericserafim.com.whatsappclone.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaLogar;
    private Usuario usuario;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = ConfiguracaoFirebase.getFirebaseAuth();

        email = (EditText) findViewById(R.id.edit_login_email);
        senha = (EditText) findViewById(R.id.edit_login_senha);
        botaLogar = (Button) findViewById(R.id.bt_logar);

        botaLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                validarLogin();
            }
        });

        verificarUsuarioLogado();
    }

    private void validarLogin() {
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(
                        LoginActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    usuario.setId(Base64Custom.converterBase64(usuario.getEmail()));

                                    DatabaseReference dataBase = ConfiguracaoFirebase.getDatabase()
                                                                    .child("usuarios")
                                                                    .child(usuario.getId());
                                    dataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            usuario = dataSnapshot.getValue(Usuario.class);
                                            usuario.setId(Base64Custom.converterBase64(usuario.getEmail()));

                                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                                            preferencias.salvarDados(usuario.getId(), usuario.getNome());
                                            abrirTelaPrincipal();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(LoginActivity.this, "Usuário ou senha inválida", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                );
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    private void verificarUsuarioLogado() {
        if (auth.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }
}
