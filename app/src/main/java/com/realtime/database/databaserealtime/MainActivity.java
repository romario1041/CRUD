package com.realtime.database.databaserealtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private RecyclerView artists;
    private DatabaseReference artistsRef;
    private ArtistAdapter adapter;
    private Button botaoInserir;
    private Button botaoExcluir;
    EditText id;
    EditText nome;
    EditText nacionalidade;
    EditText influenciado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        artists = findViewById(R.id.artists);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        artists.setLayoutManager(layout);
        adapter = new ArtistAdapter(this);
        artists.setAdapter(adapter);

        botaoInserir = findViewById(R.id.enviar);
        botaoExcluir = findViewById(R.id.excluir);
        id = findViewById(R.id.id);
        nome = findViewById(R.id.nome);
        nacionalidade = findViewById(R.id.nacionalidade);
        influenciado = findViewById(R.id.influenciado);
        artistsRef = database.getReference("artists");

        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layout.getOrientation());
        artists.addItemDecoration(dividerItemDecoration);
        */

        //Botão pra inserir dados no banco
        botaoInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            inserirNovoDado(id.getText().toString(),nome.getText().toString(),nacionalidade.getText().toString(),influenciado.getText().toString());
            Toast.makeText(MainActivity.this, "Usuário adicionado", Toast.LENGTH_SHORT).show();
            id.setText(null);
            nome.setText(null);
            nacionalidade.setText(null);
            influenciado.setText(null);
            }
        });

        //Botão para excluir dados do banco
        //É necessário preencher na tela o ID do cadastro que deseja excluri
        botaoExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirDado(id.getText().toString());
                Toast.makeText(MainActivity.this, "Usuário excluído", Toast.LENGTH_SHORT).show();
                id.setText(null);
                nome.setText(null);
                nacionalidade.setText(null);
                influenciado.setText(null);

            }
        });


        //Método que atualiza o recyclerView quando dados são inseridos ou excluídos do BD
        artistsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                for (DataSnapshot artist : snapshotIterable) {
                    adapter.addArtist(artist.getValue(Artist.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    //Método para inserir novo cadastro de artista
    private void inserirNovoDado(String userId, String name, String nacionalidade, String influenciado) {
        Artist artist = new Artist(userId, name, nacionalidade, influenciado);

        artistsRef.child(userId).setValue(artist);
    }
    //Método para excluir cadastro de artista existente com base no ID
    //Você passa o ID do artista e ao clicar no botão Excluir dado o artista é deletado
    private void excluirDado(String userId) {

        artistsRef.child(userId).removeValue();
    }
}
