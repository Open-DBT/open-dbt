package com.highgo.opendbt.progress.model;

import java.util.ArrayList;
import java.util.List;

public class StudentStatProgress {

	// 学习进度
	private List<KnowledgeExerciseCount> studyProgress = new ArrayList<KnowledgeExerciseCount>();
	// 努力程度
	private List<KnowledgeExerciseCount> effortProgress = new ArrayList<KnowledgeExerciseCount>();

	public List<KnowledgeExerciseCount> getStudyProgress() {
		return studyProgress;
	}

	public void setStudyProgress(List<KnowledgeExerciseCount> studyProgress) {
		this.studyProgress = studyProgress;
	}

	public List<KnowledgeExerciseCount> getEffortProgress() {
		return effortProgress;
	}

	public void setEffortProgress(List<KnowledgeExerciseCount> effortProgress) {
		this.effortProgress = effortProgress;
	}

}
