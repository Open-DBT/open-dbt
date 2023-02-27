package com.highgo.opendbt.feedback.mapper;

import com.highgo.opendbt.feedback.model.Feedback;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackMapper {

	public Integer add(Feedback feedback);

	public List<Feedback> getFeedbackList();

}
