package com.example.files.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.files.model.FileModel;
import com.example.files.model.ProgressState;
import com.example.files.repository.FilesRepository;
import com.example.files.repository.impl.ExternalFilesRepository;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends ViewModel {

    private static final String FILES_LIST_FILENAME = "files.txt";

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private FilesRepository filesRepository = new ExternalFilesRepository();

    private MutableLiveData<List<FileModel>> filesLiveData = new MutableLiveData<>();

    private MutableLiveData<ProgressState> getFilesProgressState = new MutableLiveData<>();
    private MutableLiveData<ProgressState> saveFilesListProgressState = new MutableLiveData<>();

    public LiveData<List<FileModel>> getFiles() {
        getFilesProgressState.setValue(ProgressState.IN_PROGRESS);

        executorService.submit(() -> {
            List<FileModel> files = filesRepository.getAllFiles();
            filesLiveData.postValue(files);

            getFilesProgressState.postValue(ProgressState.DONE);
        });

        return filesLiveData;
    }

    public LiveData<ProgressState> getGetFilesProgressState() {
        return getFilesProgressState;
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

    public MutableLiveData<ProgressState> getSaveFilesListProgressState() {
        return saveFilesListProgressState;
    }

    private String getFilesListJson() {
        List<FileModel> files = filesLiveData.getValue();

        if (files != null) {
            JSONArray jsonArray = new JSONArray();

            for (FileModel file : files) {
                jsonArray.put(file.toJson());
            }

            return jsonArray.toString();
        } else {
            return "";
        }
    }
}