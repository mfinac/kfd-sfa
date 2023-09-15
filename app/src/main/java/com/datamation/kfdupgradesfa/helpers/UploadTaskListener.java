package com.datamation.kfdupgradesfa.helpers;


import com.datamation.kfdupgradesfa.api.TaskTypeUpload;

import java.util.List;

public interface UploadTaskListener {
    void onTaskCompleted(TaskTypeUpload taskType, List<String> list);
    void onTaskCompleted(List<String> list);
}