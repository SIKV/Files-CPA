package com.example.files.viewmodel;

import android.app.Application;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.files.R;
import com.example.files.model.FileItem;
import com.example.files.model.FileModel;
import com.example.files.model.ProgressState;
import com.example.files.repository.FilesRepository;
import com.example.files.repository.impl.ExternalFilesRepository;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {

    private static final String FILES_LIST_FILENAME = "files.txt";

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private FilesRepository filesRepository = new ExternalFilesRepository();

    private MutableLiveData<List<FileItem>> files = new MutableLiveData<>();

    private MutableLiveData<ProgressState> fetchFilesProgressState = new MutableLiveData<>();
    private MutableLiveData<ProgressState> saveFilesListProgressState = new MutableLiveData<>();

    private int filenameNormalColor = ContextCompat.getColor(getApplication(), R.color.filenameNormalColor);
    private int filenameHighlightColor = ContextCompat.getColor(getApplication(), R.color.filenameHighlightColor);

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        // TODO
        executorService.shutdownNow();
    }

    public LiveData<List<FileItem>> files() {
        return files;
    }

    public LiveData<ProgressState> fetchFilesProgressState() {
        return fetchFilesProgressState;
    }

    public MutableLiveData<ProgressState> saveFilesListProgressState() {
        return saveFilesListProgressState;
    }

    public void fetchFiles() {
        fetchFilesProgressState.setValue(ProgressState.IN_PROGRESS);

        executorService.submit(() -> {
            List<FileModel> fetchedFiles = filesRepository.getAllFiles();

            List<FileItem> fileItems = new ArrayList<>(fetchedFiles.size());

            for (FileModel fileModel : fetchedFiles) {
                String filename = fileModel.getFilename();

                fileItems.add(new FileItem(
                        fileModel,
                        createSpannable(filename, filenameNormalColor, 0, filename.length()))
                );
            }

            this.files.postValue(fileItems);

            fetchFilesProgressState.postValue(ProgressState.DONE);
        });
    }

    public void highlightFiles(String filenameContains) {
        if (filenameContains == null || filenameContains.trim().isEmpty()) {
            fetchFiles();
            return;
        }
        executorService.submit(() -> {
            List<FileItem> currentFiles = files.getValue();

            if (currentFiles != null) {
                List<FileItem> fileItems = new ArrayList<>();

                for (FileItem fileItem : currentFiles) {
                    String filename = fileItem.getFileModel().getFilename();

                    int highlightStartIndex = filename.toLowerCase().indexOf(filenameContains.toLowerCase());
                    int highlightEndIndex = highlightStartIndex + filenameContains.length();

                    int filenameColor = filenameHighlightColor;

                    if (highlightStartIndex == -1) {
                        highlightStartIndex = 0;
                        highlightEndIndex = filename.length();

                        filenameColor = filenameNormalColor;
                    }

                    fileItems.add(new FileItem(
                            fileItem.getFileModel(),
                            createSpannable(filename, filenameColor, highlightStartIndex, highlightEndIndex))
                    );
                }

                this.files.postValue(fileItems);
            }
        });
    }

    public LiveData<String> saveFilesList() {
        MutableLiveData<String> savedFilePath = new MutableLiveData<>();

        saveFilesListProgressState.setValue(ProgressState.IN_PROGRESS);

        executorService.submit(() -> {
            String content = getFilesListJson();
            String path = filesRepository.saveFile(FILES_LIST_FILENAME, content);

            savedFilePath.postValue(path);

            if (path != null) {
                saveFilesListProgressState.postValue(ProgressState.DONE);
            } else {
                saveFilesListProgressState.postValue(ProgressState.ERROR);
            }
        });

        return savedFilePath;
    }

    private Spannable createSpannable(String text, int color, int start, int end) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    private String getFilesListJson() {
        List<FileItem> currentFiles = files.getValue();

        if (currentFiles != null) {
            JSONArray jsonArray = new JSONArray();

            for (FileItem file : currentFiles) {
                jsonArray.put(file.getFileModel().toJson());
            }

            return jsonArray.toString();
        } else {
            return "";
        }
    }
}