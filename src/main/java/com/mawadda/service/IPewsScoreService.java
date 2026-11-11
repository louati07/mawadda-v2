package com.mawadda.service;

import com.mawadda.entity.PewsScore;

import java.util.List;

public interface IPewsScoreService {
    PewsScore add(PewsScore pewsScore);
    PewsScore modify(Integer id, PewsScore pewsScore);
    PewsScore show(Integer id);
    List<PewsScore> showAll();
    List<PewsScore> showByAdmission(Integer admissionId);
    PewsScore markAsError(Integer id, String reason);
    void delete(Integer id);
    int calculatePewsScore(int behavior, int cardio, int resp);
}
