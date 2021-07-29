package com.coremacasia.learnatadmin.commons;

import com.coremacasia.learnatadmin.helpers.CategoryHelper;
import com.coremacasia.learnatadmin.helpers.CourseHelper;
import com.coremacasia.learnatadmin.helpers.MentorHelper;
import com.coremacasia.learnatadmin.helpers.SubjectHelper;

import java.util.ArrayList;

public class CommonDataModel {
    public CommonDataModel(){}

    public ArrayList<CategoryHelper> getCategory() {
        return category;
    }

    private ArrayList<CategoryHelper> category;

    private ArrayList<String> course_id;

    public ArrayList<String> getSubject_id() {
        return subject_id;
    }

    private ArrayList<String> subject_id;

    public ArrayList<String> getCourse_id() {
        return course_id;
    }

    public ArrayList<CourseHelper> getAll_courses() {
        return all_courses;
    }

    private ArrayList<CourseHelper> all_courses;

    private ArrayList<MentorHelper> mentors;

    private ArrayList<String> course_lang=new ArrayList<>();

    public void setCourse_lang(ArrayList<String> course_lang) {
        this.course_lang = course_lang;
    }

    public ArrayList<String> getCourse_lang() {
        return course_lang;
    }

    private ArrayList<SubjectHelper> all_subjects;

    public ArrayList<SubjectHelper> getAll_subjects() {
        return all_subjects;
    }

    public ArrayList<MentorHelper> getMentors() {
        return mentors;
    }
}
