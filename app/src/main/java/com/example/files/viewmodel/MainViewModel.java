package com.example.files.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.files.model.FileModel;
import com.example.files.model.ProgressState;
import com.example.files.repository.FilesRepository;
import com.example.files.repository.impl.ExternalFilesRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends ViewModel {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private FilesRepository filesRepository = new ExternalFilesRepository();

    private MutableLiveData<List<FileModel>> filesLiveData = new MutableLiveData<>();
    private MutableLiveData<ProgressState> getFilesProgressState = new MutableLiveData<>();

    public LiveData<List<FileModel>> getFiles() {
        getFilesProgressState.postValue(ProgressState.IN_PROGRESS);

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
}