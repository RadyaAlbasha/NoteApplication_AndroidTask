package android.task.noteapplication.screens.notedetailsscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.task.noteapplication.R;
import android.task.noteapplication.screens.addeditnotescreen.AddEditNoteActivity;
import android.widget.TextView;

public class NoteDetailsActivity extends AppCompatActivity {

    TextView noteTitleTV;
    TextView noteContentTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        initComponents();
    }

    private void initComponents() {

        noteTitleTV = findViewById(R.id.textView_title);
        noteContentTV = findViewById(R.id.textView_content);

        Intent intent = getIntent();
        if (intent.hasExtra(AddEditNoteActivity.EXTRA_ID)) {
            noteTitleTV.setText(intent.getStringExtra(AddEditNoteActivity.EXTRA_TITLE));
            noteContentTV.setText(intent.getStringExtra(AddEditNoteActivity.EXTRA_Content));
        }
    }
}
