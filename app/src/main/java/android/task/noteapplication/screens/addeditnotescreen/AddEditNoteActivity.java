package android.task.noteapplication.screens.addeditnotescreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.task.noteapplication.R;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "android.task.noteapp.screens.addnotescreen.EXTRA_ID";
    public static final String EXTRA_TITLE = "android.task.noteapp.screens.addnotescreen.EXTRA_TITLE";
    public static final String EXTRA_Content = "android.task.noteapp.screens.addnotescreen.EXTRA_Content";
    EditText noteTitleET;
    EditText noteContentET;
    TextView screenTitle;
   // Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        initComponents();

    }

    private void initComponents() {
        noteTitleET = findViewById(R.id.editTextTitle);
        noteContentET = findViewById(R.id.editTextContent);
        screenTitle = findViewById(R.id.text_view_screen_title);

       /* toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            screenTitle.setText(R.string.editNote);
            noteTitleET.setText(intent.getStringExtra(EXTRA_TITLE));
            noteContentET.setText(intent.getStringExtra(EXTRA_Content));
        } else {
            screenTitle.setText(R.string.addNewNote);
        }
    }

    public void saveNote() {

        String title = noteTitleET.getText().toString();
        String content = noteContentET.getText().toString();
        if (title.trim().isEmpty() || content.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_Content, content);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId())
       {
           case R.id.save_note:
               saveNote();
               return true;
           default:
               return super.onOptionsItemSelected(item);

       }
    }*/

    public void saveNoteAction(View view) {
        saveNote();
    }
}
