package com.coremacasia.learnatadmin.commons.offlineData;

import com.coremacasia.learnatadmin.helpers.MentorHelper;

public class XtraHelper {


    public static MentorHelper getAddCourseMentorHelper() {
        return addCourseMentorHelper;
    }

    public static void setAddCourseMentorHelper(MentorHelper addCourseMentorHelper) {
        XtraHelper.addCourseMentorHelper = addCourseMentorHelper;
    }

    private static MentorHelper addCourseMentorHelper;

    private MentorHelper selectedMentorHelper;

    public MentorHelper getSelectedMentorHelper() {
        return selectedMentorHelper;
    }

    public void setSelectedMentorHelper(MentorHelper selectedMentorHelper) {
        this.selectedMentorHelper = selectedMentorHelper;
    }
}
