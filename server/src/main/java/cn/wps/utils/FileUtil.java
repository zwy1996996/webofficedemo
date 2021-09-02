package cn.wps.utils;

/**
 * TODO
 *
 * @author Administrator
 * @date 2021/9/2
 */
public class FileUtil {
    // office
    private static String[] office = {"word", "excel", "ppt"};

    // excel
    private static String[] etExts = {"et", "xls", "xlt", "xlsx", "xlsm", "xltx", "xltm", "csv"};

    // word
    private static String[] wpsExts = {"doc", "docx", "txt", "dot", "wps", "wpt", "dotx", "docm", "dotm"};

    // ppt
    private static String[] wppExts = {"ppt", "pptx", "pptm", "pptm", "ppsm", "pps", "potx", "potm", "dpt", "dps"};

    // pdf
    private static String[] pdfExts = {"pdf"};

    public static String getFileTypeCode(String fileType) {
        for (String et : etExts) {
            if (et.equalsIgnoreCase(fileType)) {
                return "s";
            }
        }
        for (String et : wpsExts) {
            if (et.equalsIgnoreCase(fileType)) {
                return "w";
            }
        }
        for (String et : wppExts) {
            if (et.equalsIgnoreCase(fileType)) {
                return "p";
            }
        }
        for (String et : pdfExts) {
            if (et.equalsIgnoreCase(fileType)) {
                return "f";
            }
        }
        return null;
    }

    public static boolean checkCode(String fileType) {
        for (String et : office) {
            if (et.equalsIgnoreCase(fileType)) {
                return true;
            }
        }
        return false;
    }



}
