package android.task.noteapplication.screens.homescreen;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.task.noteapplication.R;
import android.task.noteapplication.model.Note;
import android.task.noteapplication.model.NoteAdapter;
import android.task.noteapplication.model.services.UserSharedPerferences;
import android.task.noteapplication.screens.addeditnotescreen.AddEditNoteActivity;
import android.task.noteapplication.screens.loginscreen.LoginActivity;
import android.task.noteapplication.screens.notedetailsscreen.NoteDetailsActivity;
import android.task.noteapplication.viewmodel.NoteViewModel;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;
    private UserSharedPerferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPref = UserSharedPerferences.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_note);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView .setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                //update RecyclerView
                noteAdapter.submitList(notes);
                // noteAdapter.setNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

               /* if(i == ItemTouchHelper.LEFT){

                }
                else if (i == ItemTouchHelper.RIGHT){
                   // Log.i("Swipe direction : ","Right");
                }*/

                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(HomeActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                //view details
                Intent intent = new Intent(HomeActivity.this , NoteDetailsActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getNoteTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_Content,note.getNoteBody());
                startActivity(intent);
            }

            @Override
            public void onEditBtnClick(Note note) {
                //edit Note
                Intent intent = new Intent(HomeActivity.this , AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getNoteTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_Content,note.getNoteBody());
                startActivityForResult(intent,EDIT_NOTE_REQUEST);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbarID);
        CircleImageView userPic = findViewById(R.id.toolbarPic);
        TextView userNameTV = findViewById(R.id.toolbarName);
        Intent intent = getIntent();
        if (intent.hasExtra(LoginActivity.EXTRA_ID)) {
            Glide.with(HomeActivity.this)
                    .load(intent.getStringExtra(LoginActivity.EXTRA_IMG))
                    .placeholder(R.drawable.ic_person)//this would be your default image (like default profile or logo etc). it would be loaded at initial time and it will replace with your loaded image once glide successfully load image using url.
                    .error(R.drawable.ic_person)//in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //using to load into cache then second time it will load fast.
                    .into(userPic);
            userNameTV.setText(intent.getStringExtra(LoginActivity.EXTRA_NAME));
        }
        setSupportActionBar(toolbar);

        FloatingActionButton fab_addNote = findViewById(R.id.fab_add_note);
        fab_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
                Intent intent = new Intent(HomeActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent,ADD_NOTE_REQUEST);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
           // FirebaseAuth.getInstance().signOut();
            sharedPref.saveISLogged_IN(HomeActivity.this, false);
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            HomeActivity.this.finish();
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.action_deleteAllNotes)
        {
            noteViewModel.deleteAllNotes();
            Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String content = data.getStringExtra(AddEditNoteActivity.EXTRA_Content);

            Note note = new Note(title,content);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }else  if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);
            if(id == -1){
                Toast.makeText(this, "Note can't be Updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String content = data.getStringExtra(AddEditNoteActivity.EXTRA_Content);

            Note note = new Note(title,content);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
