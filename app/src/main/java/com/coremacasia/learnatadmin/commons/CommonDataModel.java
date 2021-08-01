package com.coremacasia.learnatadmin.commons;

import com.coremacasia.learnatadmin.helpers.CategoryHelper;
import com.coremacasia.learnatadmin.helpers.CourseHelper;
import com.coremacasia.learnatadmin.helpers.MentorHelper;
import com.coremacasia.learnatadmin.helpers.SubjectHelper;

import java.util.ArrayList;

public class CommonDataModel {
    public CommonDataModel() {
    }

    public ArrayList<CategoryHelper> getCategory() {
        return category;
    }

    private ArrayList<CategoryHelper> category=new ArrayList<>();

    private ArrayList<String> course_id = new ArrayList<>();
    private ArrayList<String> trending = new ArrayList<>();

    public ArrayList<String> getTrending() {
        return trending;
    }

    public ArrayList<String> getPopular() {
        return popular;
    }

    private ArrayList<String> popular = new ArrayList<>();

    public ArrayList<String> getSubject_id() {
        return subject_id;
    }

    private ArrayList<String> subject_id=new ArrayList<>();

    public ArrayList<String> getCourse_id() {
        return course_id;
    }

    public ArrayList<CourseHelper> getAll_courses() {
        return all_courses;
    }

    private ArrayList<CourseHelper> all_courses = new ArrayList<>();

    private ArrayList<MentorHelper> mentors = new ArrayList<>();

    private ArrayList<String> course_lang = new ArrayList<>();

    public void setCourse_lang(ArrayList<String> course_lang) {
        this.course_lang = course_lang;
    }

    public ArrayList<String> getCourse_lang() {
        return course_lang;
    }

    private ArrayList<SubjectHelper> all_subjects=new ArrayList<>();

    public ArrayList<SubjectHelper> getAll_subjects() {
        return all_subjects;
    }

    public ArrayList<MentorHelper> getMentors() {
        return mentors;
    }
    private ArrayList<String> mentor_id=new ArrayList<>();

    public ArrayList<String> getMentor_id() {
        return mentor_id;
    }
}
