package com.flashcard.controller.quiz.dto;

public class QuizRowDTO {

    private String soru;
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String cevap;
    private String quizAdi;
    private String quizTipi;

    public QuizRowDTO() {}

    public QuizRowDTO(String soru, String a, String b, String c, String d, String e,
                      String cevap, String quizAdi, String quizTipi) {
        this.soru = soru;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.cevap = cevap;
        this.quizAdi = quizAdi;
        this.quizTipi = quizTipi;
    }

    public String getSoru() { return soru; }
    public void setSoru(String soru) { this.soru = soru; }

    public String getA() { return a; }
    public void setA(String a) { this.a = a; }

    public String getB() { return b; }
    public void setB(String b) { this.b = b; }

    public String getC() { return c; }
    public void setC(String c) { this.c = c; }

    public String getD() { return d; }
    public void setD(String d) { this.d = d; }

    public String getE() { return e; }
    public void setE(String e) { this.e = e; }

    public String getCevap() { return cevap; }
    public void setCevap(String cevap) { this.cevap = cevap; }

    public String getQuizAdi() { return quizAdi; }
    public void setQuizAdi(String quizAdi) { this.quizAdi = quizAdi; }

    public String getQuizTipi() { return quizTipi; }
    public void setQuizTipi(String quizTipi) { this.quizTipi = quizTipi; }
}