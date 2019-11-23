package in.mrasif.app.qispeechtotext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import in.mrasif.app.qispeechtotext.adapters.ItemAdapter;
import in.mrasif.app.qispeechtotext.databinding.ActivityMainBinding;
import in.mrasif.app.qispeechtotext.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity implements RecognitionListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_RECORD_PERMISSION = 100;
    private MainViewModel model;
    private ItemAdapter adapter;
    private ActivityMainBinding binding;
    private ProgressDialog progressDialog;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        model= ViewModelProviders.of(this).get(MainViewModel.class);
        adapter=new ItemAdapter(this);
        binding.setAdapter(adapter);
        progressDialog=new ProgressDialog(this);

        model.isLoading().observe(this,aBoolean -> {
            if (aBoolean){
                progressDialog.setMessage("Loading dictionary...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            else {
                progressDialog.dismiss();
            }
        });

        model.getItems().observe(this,items -> {
            adapter.update(items);
        });

        model.getItem().observe(this, item -> {
            binding.setItem(item);
        });

        binding.pbRecognization.setVisibility(View.INVISIBLE);
        speech=SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        binding.tbSpeak.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.i(TAG, "onCreate: isChecked=true");
                binding.pbRecognization.setVisibility(View.VISIBLE);
                binding.pbRecognization.setIndeterminate(true);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions
                            (MainActivity.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                    REQUEST_RECORD_PERMISSION);
                }
                else {
                    speech.startListening(recognizerIntent);
                }

            } else {
                Log.i(TAG, "onCreate: isChecked=false");
                binding.pbRecognization.setIndeterminate(false);
                binding.pbRecognization.setVisibility(View.INVISIBLE);
                speech.stopListening();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast
                            .LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
            Log.i(TAG, "destroy");
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOfSpeech");
        binding.pbRecognization.setIndeterminate(false);
        binding.pbRecognization.setMax(10);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(TAG, "onRmsChanged: " + rmsdB);
        binding.pbRecognization.setProgress((int) rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech");
        binding.pbRecognization.setIndeterminate(true);
        binding.tbSpeak.setChecked(false);
    }

    @Override
    public void onError(int error) {
        String errorMessage = getErrorText(error);
        Log.d(TAG, "FAILED " + errorMessage);
        model.changeVoiceOutput(null);
        binding.tbSpeak.setChecked(false);
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        String text = "";
//        for (String result : matches) {
//            text += result + "\n";
//        }
        String text=matches.get(0);
        Log.i(TAG, "onResults: "+text);
        model.changeVoiceOutput(text);
        model.updateUI(text);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.i(TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(TAG, "onEvent");
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
