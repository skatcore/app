package com.mettwurst.skatdb;

import android.database.Cursor;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Task implements Runnable {
    Cursor cursor;
    String datum;
    public Task(Cursor cursor, String datum) {
        this.cursor = cursor;
        this.datum = datum;
    }

    @Override
    public void run() {
        exportToExcel();
    }

    /**
     * Exports the cursor value to an excel sheet.
     * Recommended to call this method in a separate thread,
     * especially if you have more number of threads.
     */
    private void exportToExcel() {
        final String fileName;
        if (datum == null) {
            fileName = "SkatList.xls";
        } else {
            fileName = "SkatList_" +datum.replace(":", "-") +".xls";
        }

        //Saving file in external storage
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath());
        if(!directory.isDirectory()){
            //noinspection ResultOfMethodCallIgnored
            directory.mkdirs();
        }
        File file = new File(directory, fileName);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);

            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("MySkatList", 0);

            try {
                sheet.addCell(new Label(0, 0, DBContract.Entry.COL_DATUM)); // column and row
                sheet.addCell(new Label(1, 0, DBContract.Entry.COL_SPIELER1));
                sheet.addCell(new Label(2, 0, DBContract.Entry.COL_SPIELER2));
                sheet.addCell(new Label(3, 0, DBContract.Entry.COL_SPIELER3));
                sheet.addCell(new Label(4, 0, DBContract.Entry.COL_SPIELER4));
                sheet.addCell(new Label(5, 0, DBContract.Entry.COL_SPIELER5));
                sheet.addCell(new Label(6, 0, DBContract.Entry.COL_SPIELERZAHL));
                sheet.addCell(new Label(7, 0, DBContract.Entry.COL_GEBER));
                sheet.addCell(new Label(8, 0, DBContract.Entry.COL_SOLIST));
                sheet.addCell(new Label(9, 0, DBContract.Entry.COL_BUBEN));
                sheet.addCell(new Label(10, 0, DBContract.Entry.COL_HANDSPIEL));
                sheet.addCell(new Label(11, 0, DBContract.Entry.COL_OUVERT));
                sheet.addCell(new Label(12, 0, DBContract.Entry.COL_SCHNEIDER_ANGESAGT));
                sheet.addCell(new Label(13, 0, DBContract.Entry.COL_SCHWARZ_ANGESAGT));
                sheet.addCell(new Label(14, 0, DBContract.Entry.COL_REIZWERT));
                sheet.addCell(new Label(15, 0, DBContract.Entry.COL_SPIEL));
                sheet.addCell(new Label(16, 0, DBContract.Entry.COL_SCHNEIDER_GESPIELT));
                sheet.addCell(new Label(17, 0, DBContract.Entry.COL_SCHWARZ_GESPIELT));
                sheet.addCell(new Label(18, 0, DBContract.Entry.COL_PUNKTE_RE));
                sheet.addCell(new Label(19, 0, DBContract.Entry.COL_SOLIST_GEWONNEN));
                sheet.addCell(new Label(20, 0, DBContract.Entry.COL_KONTRA));
                sheet.addCell(new Label(21, 0, DBContract.Entry.COL_RE));
                sheet.addCell(new Label(22, 0, DBContract.Entry.COL_BOCK));
                sheet.addCell(new Label(23, 0, DBContract.Entry.COL_PUNKTE_SP1));
                sheet.addCell(new Label(24, 0, DBContract.Entry.COL_PUNKTE_SP2));
                sheet.addCell(new Label(25, 0, DBContract.Entry.COL_PUNKTE_SP3));
                sheet.addCell(new Label(26, 0, DBContract.Entry.COL_PUNKTE_SP4));
                sheet.addCell(new Label(27, 0, DBContract.Entry.COL_PUNKTE_SP5));
                sheet.addCell(new Label(28, 0, DBContract.Entry.COL_GESAMT_SP1));
                sheet.addCell(new Label(29, 0, DBContract.Entry.COL_GESAMT_SP2));
                sheet.addCell(new Label(30, 0, DBContract.Entry.COL_GESAMT_SP3));
                sheet.addCell(new Label(31, 0, DBContract.Entry.COL_GESAMT_SP4));
                sheet.addCell(new Label(32, 0, DBContract.Entry.COL_GESAMT_SP5));
                sheet.addCell(new Label(33, 0, DBContract.Entry.COL_JUNGFRAU_ANGESAGT1));
                sheet.addCell(new Label(34, 0, DBContract.Entry.COL_JUNGFRAU_ANGESAGT2));
                sheet.addCell(new Label(35, 0, DBContract.Entry.COL_JUNGFRAU_ANGESAGT3));
                sheet.addCell(new Label(36, 0, DBContract.Entry.COL_JUNGFRAU_ANGESAGT4));
                sheet.addCell(new Label(37, 0, DBContract.Entry.COL_JUNGFRAU_ANGESAGT5));
                sheet.addCell(new Label(38, 0, DBContract.Entry.COL_SPIELWERT));

                if (cursor.moveToFirst()) {
                    do {
                        int i = cursor.getPosition() + 1;

                        sheet.addCell(new Label(0, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_DATUM))));
                        sheet.addCell(new Label(1, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELER1))));
                        sheet.addCell(new Label(2, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELER2))));
                        sheet.addCell(new Label(3, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELER3))));
                        sheet.addCell(new Label(4, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELER4))));
                        sheet.addCell(new Label(5, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELER5))));
                        sheet.addCell(new Label(6, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELERZAHL))));
                        sheet.addCell(new Label(7, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_GEBER))));
                        sheet.addCell(new Label(8, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SOLIST))));
                        sheet.addCell(new Label(9, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_BUBEN))));
                        sheet.addCell(new Label(10, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_HANDSPIEL))));
                        sheet.addCell(new Label(11, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_OUVERT))));
                        sheet.addCell(new Label(12, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SCHNEIDER_ANGESAGT))));
                        sheet.addCell(new Label(13, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SCHWARZ_ANGESAGT))));
                        sheet.addCell(new Label(14, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_REIZWERT))));
                        sheet.addCell(new Label(15, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIEL))));
                        sheet.addCell(new Label(16, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SCHNEIDER_GESPIELT))));
                        sheet.addCell(new Label(17, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SCHWARZ_GESPIELT))));
                        sheet.addCell(new Label(18, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_PUNKTE_RE))));
                        sheet.addCell(new Label(19, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SOLIST_GEWONNEN))));
                        sheet.addCell(new Label(20, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_KONTRA))));
                        sheet.addCell(new Label(21, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_RE))));
                        sheet.addCell(new Label(22, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_BOCK))));
                        sheet.addCell(new Label(23, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_PUNKTE_SP1))));
                        sheet.addCell(new Label(24, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_PUNKTE_SP2))));
                        sheet.addCell(new Label(25, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_PUNKTE_SP3))));
                        sheet.addCell(new Label(26, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_PUNKTE_SP4))));
                        sheet.addCell(new Label(27, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_PUNKTE_SP5))));
                        sheet.addCell(new Label(28, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_GESAMT_SP1))));
                        sheet.addCell(new Label(29, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_GESAMT_SP2))));
                        sheet.addCell(new Label(30, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_GESAMT_SP3))));
                        sheet.addCell(new Label(31, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_GESAMT_SP4))));
                        sheet.addCell(new Label(32, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_GESAMT_SP5))));
                        sheet.addCell(new Label(33, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_JUNGFRAU_ANGESAGT1))));
                        sheet.addCell(new Label(34, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_JUNGFRAU_ANGESAGT2))));
                        sheet.addCell(new Label(35, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_JUNGFRAU_ANGESAGT3))));
                        sheet.addCell(new Label(36, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_JUNGFRAU_ANGESAGT4))));
                        sheet.addCell(new Label(37, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_JUNGFRAU_ANGESAGT5))));
                        sheet.addCell(new Label(38, i, cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELWERT))));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();

            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}