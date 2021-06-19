package com.coremacasia.learnatadmin.commons;

import com.coremacasia.learnatadmin.menus.category.CategoryHelper;
import com.coremacasia.learnatadmin.menus.courses.CourseHelper;
import com.coremacasia.learnatadmin.menus.courses.Courses;
import com.coremacasia.learnatadmin.menus.mentors.MentorHelper;
import com.coremacasia.learnatadmin.menus.subjects.SubjectHelper;

import java.util.ArrayList;

public class CommonDataModel {
    public CommonDataModel(){}

    public ArrayList<CategoryHelper> getCategory() {
        return category;
    }

    private ArrayList<CategoryHelper> category;
    private ArrayList<CourseHelper> courses;

    public ArrayList<CourseHelper> getCourses() {
        return courses;
    }
/*    public ArrayList<SubjectHelper> getSubjects() {
        return subjects;
    }

    private ArrayList<SubjectHelper> subjects;
    private ArrayList<MentorHelper> mentors;


    public ArrayList<MentorHelper> getMentors() {
        return mentors;
    }*/
}
