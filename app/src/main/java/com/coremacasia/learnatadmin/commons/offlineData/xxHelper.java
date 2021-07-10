package com.coremacasia.learnatadmin.commons.offlineData;

public  class xxHelper {
    public XtraHelper getHelper() {
        return helper;
    }

    private XtraHelper helper;

    private static XtraHelper xtraHelper;

    public static XtraHelper getXtraHelper() {
        return xtraHelper;
    }

    public static void setXtraHelper(XtraHelper xtraHelper) {
        xxHelper.xtraHelper = xtraHelper;
    }
}
